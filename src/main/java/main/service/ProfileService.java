package main.service;

import main.dto.api.errorDto.ErrorProfileDto;
import main.dto.api.request.ProfileRequest;
import main.dto.api.response.ProfileResponse;
import main.model.User;
import main.model.repositories.UserRepository;
import org.apache.commons.io.FileUtils;
import org.imgscalr.Scalr;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static main.mappers.converter.ResultValue.ONE;
import static main.mappers.converter.ResultValue.UPLOAD;

@Service
public class ProfileService {
  private final UserRepository userRepository;
  private final Integer LIMIT_SIZE_PHOTO = 5242880;

  public ProfileService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public ProfileResponse getEditProfile(
      byte[] photo,
      String name,
      String email,
      String password,
      Integer removePhoto,
      Principal principal,
      HttpServletRequest request) {

    if (photo.length > LIMIT_SIZE_PHOTO) {
      ProfileResponse profileResponse = new ProfileResponse();
      ErrorProfileDto errorProfileDto = new ErrorProfileDto();
      errorProfileDto.setPhoto("Фото слишком большое, нужно не более 5 Мб");
      profileResponse.setErrors(errorProfileDto);
      profileResponse.setResult(false);
      return profileResponse;
    }
    main.model.User user = userRepository.findByEmail(principal.getName());
    addImage(photo, request, principal, user);
    return editProfile(name, email, user, password, removePhoto, request);
  }

  public ProfileResponse getJsonEditProfile(
      ProfileRequest profileRequest, Principal principal, HttpServletRequest request) {
    main.model.User user = userRepository.findByEmail(principal.getName());

    return editProfile(
        profileRequest.getName(),
        profileRequest.getEmail(),
        user,
        profileRequest.getPassword(),
        profileRequest.getRemovePhoto(),
        request);
  }

  private ProfileResponse editProfile(
      String name,
      String email,
      User user,
      String password,
      Integer removePhoto,
      HttpServletRequest request) {
    ProfileResponse profileResponse = new ProfileResponse();
    ErrorProfileDto errorProfileDto = new ErrorProfileDto();
    System.out.println(name);
    if (Objects.isNull(name) || !isValidName(name)) {
      errorProfileDto.setName("Имя указано неверно");
      profileResponse.setErrors(errorProfileDto);
      profileResponse.setResult(false);
      return profileResponse;
    }

    if (Objects.nonNull(name) && isValidName(name)) {
      user.setName(name);
    }

    while (true) {
      if (Objects.isNull(email)) {
        errorProfileDto.setEmail("Поле e-mail не может быть пустым");
        profileResponse.setErrors(errorProfileDto);
        profileResponse.setResult(false);
        return profileResponse;
      } else if (email.equals(user.getEmail())) {
        break;
      } else if (Objects.isNull(userRepository.findByEmail(email))) {
        user.setEmail(email);
        break;
      } else {
        errorProfileDto.setEmail("Этот e-mail уже зарегистрирован");
        profileResponse.setErrors(errorProfileDto);
        profileResponse.setResult(false);
        return profileResponse;
      }
    }

    if (Objects.nonNull(password)) {
      BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
      user.setPassword(passwordEncoder.encode(password));
    }

    if (Objects.nonNull(removePhoto) && removePhoto == ONE && Objects.nonNull(user.getPhoto())) {
      user = deletePhoto(user, request);
    }
    userRepository.save(user);
    return profileResponse;
  }

  public void addImage(byte[] photo, HttpServletRequest request, Principal principal, User user) {
    if (Objects.nonNull(user.getPhoto())) {
      user = deletePhoto(user, request);
    }
    String path = UPLOAD + principal.hashCode() + ".jpg";
    String realPath = request.getServletContext().getRealPath(path);
    File file = new File(realPath);
    ByteArrayInputStream inputStream = new ByteArrayInputStream(photo);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    try {
      BufferedImage bufferedImage = ImageIO.read(inputStream);
      BufferedImage resizedImage = Scalr.resize(bufferedImage, 36);
      ImageIO.write(resizedImage, "jpg", outputStream);
      inputStream.close();
      outputStream.close();
      byte[] bytes = outputStream.toByteArray();
      FileUtils.writeByteArrayToFile(file, bytes);
      if (!Files.exists(Paths.get(UPLOAD))) {
        Files.createDirectories(Paths.get(UPLOAD));
      }
      Files.copy(file.toPath(), new File(path).toPath());
    } catch (IOException e) {
      e.printStackTrace();
    }
    user.setPhoto(path);
    userRepository.save(user);
  }

  private User deletePhoto(User user, HttpServletRequest request) {
    try {
      Files.delete(Path.of(user.getPhoto()));
      Files.delete(Path.of(request.getServletContext().getRealPath(user.getPhoto())));
      user.setPhoto(null);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return user;
  }

  private Boolean isValidName(String username) {
    String USERNAME_PATTERN =
        "^[a-zA-Zа-яА-Я0-9]([._-](?![._-])|[a-zA-Zа-яА-Я0-9]){0,18}[a-zA-Zа-яА-Я0-9]$";
    Pattern pattern = Pattern.compile(USERNAME_PATTERN);
    Matcher matcher = pattern.matcher(username);
    return matcher.matches();
  }
}
