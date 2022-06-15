package main.service;

import main.dto.ErrorProfileDto;
import main.dto.api.request.ProfileRequest;
import main.dto.api.response.ProfileResponse;
import main.model.User;
import main.model.repositories.UserRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
import java.util.UUID;

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
    main.model.User currentUser = userRepository.findByEmail(principal.getName());
    // currentUser.setPhoto(store(photo, "upload/ab/cd/ef/", currentUser.getPhoto()));
    addImage(photo, request, principal);
    return editProfile(name, email, currentUser, password, removePhoto);
  }

  public void addImage(byte[] photo, HttpServletRequest request, Principal principal) {
    String path = "upload/" + principal.hashCode() + ".jpg";
    String realPath = request.getServletContext().getRealPath(path);
    System.out.println("path " + path);
    System.out.println("realpath " + realPath);
    File file = new File(realPath);
    ByteArrayInputStream inputStream = new ByteArrayInputStream(photo);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    try {
      BufferedImage bufferedImage = ImageIO.read(inputStream);
      BufferedImage resizedImage = Scalr.resize(bufferedImage, 36);
      ImageIO.write(resizedImage, "jpg", outputStream);
      byte[] bytes = outputStream.toByteArray();
      FileUtils.writeByteArrayToFile(file, bytes);
    } catch (IOException e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }

    User user = userRepository.findByEmail(principal.getName());
    user.setPhoto(path);
    userRepository.save(user);
    System.out.println(userRepository.findByEmail(principal.getName()).getPhoto());
  }

  public ProfileResponse getJsonEditProfile(ProfileRequest profileRequest, Principal principal) {
    main.model.User currentUser = userRepository.findByEmail(principal.getName());
    String email = profileRequest.getEmail();
    String password = profileRequest.getPassword();
    String name = profileRequest.getName();
    String photo = profileRequest.getPhoto(); // TODO после фронта посмотреть
    Integer removePhoto = profileRequest.getRemovePhoto();

    return editProfile(name, email, currentUser, password, removePhoto);
  }

  private ProfileResponse editProfile(
      String name, String email, User currentUser, String password, Integer removePhoto) {
    ProfileResponse profileResponse = new ProfileResponse();
    ErrorProfileDto errorProfileDto = new ErrorProfileDto();

    while (true) {
      if (email.equals(currentUser.getEmail())) {
        break;
      } else if (Objects.isNull(userRepository.findByEmail(email))) {
        currentUser.setEmail(email);
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
      String encodedPassword = passwordEncoder.encode(password);
      currentUser.setPassword(encodedPassword);
    }

    if (Objects.nonNull(name)) {
      currentUser.setName(name);
    }

    if (Objects.nonNull(removePhoto) && removePhoto == 1) {
      // TODO remove is database userRepositoryDell(currentUser.getPhoto()); Если разберусь, что с
      // фронтом
      currentUser.setPhoto("");
    }

    userRepository.save(currentUser);
    return profileResponse;
  }

  //  public String store(byte[] photo, String uploadPath, String oldFile) {
  //
  //    if (file.isEmpty()) {
  //      return null;
  //    }
  //
  //    try {
  //      Path uploadPathNext = Paths.get(uploadPath);
  //      if (!Files.exists(uploadPathNext)) {
  //        Files.createDirectories(uploadPathNext);
  //      }
  //
  //      String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
  //      String fileName = String.format("%s.%s", UUID.randomUUID(), fileExtension);
  //      String filePath = String.format("%s/%s", uploadPath, fileName).replaceAll("//", "/");
  //
  //      if (Objects.nonNull(oldFile) && !Objects.equals(oldFile, "")) {
  //        Path oldFileNext = Path.of(oldFile.substring(1));
  //        if (Files.exists(oldFileNext)) Files.delete(oldFileNext);
  //      }
  //
  //      ImageIO.write(
  //          resizeImage(ImageIO.read(file.getInputStream()), 36, 36), "jpg", new File(filePath));
  //
  //      return "/" + filePath;
  //    } catch (Exception e) {
  //      return null;
  //    }
  //  }

  private BufferedImage resizeImage(
      BufferedImage originalImage, int targetWidth, int targetHeight) {
    return Scalr.resize(
        originalImage,
        Scalr.Method.ULTRA_QUALITY,
        Scalr.Mode.FIT_EXACT,
        targetWidth,
        targetHeight,
        Scalr.OP_ANTIALIAS);
  }
}
