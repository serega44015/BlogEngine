package main.service;

import main.dto.ErrorProfileDto;
import main.dto.api.request.ProfileRequest;
import main.dto.api.response.ModerationResponse;
import main.dto.api.response.ProfileResponse;
import main.model.repositories.UserRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
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
import java.util.UUID;

@Service
public class ProfileService {
    private final UserRepository userRepository;
    private final Integer LIMIT_SIZE_PHOTO = 5242880;

    public ProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public ProfileResponse getEditProfile
            (MultipartFile photo, String name, String email, String password, Integer removePhoto, Principal principal) {
        ProfileResponse profileResponse = new ProfileResponse();
        ErrorProfileDto errorProfileDto = new ErrorProfileDto();
        main.model.User currentUser = userRepository.findByEmail(principal.getName()).get();
        //TODO с фронтом траблы какие то, фотку не подтягивает
        currentUser.setPhoto(store(photo, "upload/", currentUser.getPhoto()));

        while (true) {
            //TODO Потом получать просто User, а не Optional и попробовать убрать этот колхоз
            if (email.equals(currentUser.getEmail())) {
                break;
            } else if (userRepository.findByEmail(email).isEmpty()) {
                currentUser.setEmail(email);
                break;
            } else {
                errorProfileDto.setEmail("Этот e-mail уже зарегистрирован");
                profileResponse.setErrors(errorProfileDto);
                profileResponse.setResult(false);
                return profileResponse;
            }
        }


        if (password != null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
            String encodedPassword = passwordEncoder.encode(password);
            currentUser.setPassword(encodedPassword);
        }

        if (name != null) {
            currentUser.setName(name);
        }

        if (removePhoto != null && removePhoto == 1) {
            //TODO как сделаю фотки, посмотреть будет ли работать
            System.out.println("В фотки зашли?");
            currentUser.setPhoto("");
        }


        userRepository.save(currentUser);
        return profileResponse;
    }


    public ProfileResponse getJsonEditProfile(ProfileRequest profileRequest, Principal principal) {
        ProfileResponse profileResponse = new ProfileResponse();
        main.model.User currentUser = userRepository.findByEmail(principal.getName()).get();
        // TODO Не Optional a User
        ErrorProfileDto errorProfileDto = new ErrorProfileDto();

        String email = profileRequest.getEmail();
        String password = profileRequest.getPassword();
        String name = profileRequest.getName();
        String photo = profileRequest.getPhoto();
        Integer delPhoto = profileRequest.getRemovePhoto();

        while (true) {
            //TODO Потом получать просто User, а не Optional и попробовать убрать этот колхоз
            if (email.equals(currentUser.getEmail())) {
                break;
            } else if (userRepository.findByEmail(email).isEmpty()) {
                currentUser.setEmail(email);
                break;
            } else {
                errorProfileDto.setEmail("Этот e-mail уже зарегистрирован");
                profileResponse.setErrors(errorProfileDto);
                profileResponse.setResult(false);
                return profileResponse;
            }
        }


        if (password != null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
            String encodedPassword = passwordEncoder.encode(password);
            currentUser.setPassword(encodedPassword);
        }

        if (name != null) {
            currentUser.setName(name);
        }

        if (delPhoto != null && delPhoto == 1) {
            //TODO как сделаю фотки, посмотреть будет ли работать
            System.out.println("В фотки зашли?");
            currentUser.setPhoto("");
        }
        userRepository.save(currentUser);
        //TODO после кнопки сохранить сделать обновление страницы
        return profileResponse;
    }

    public String store(MultipartFile file, String uploadPath, String oldFile) {

        if (file.isEmpty()) {
            return null;
        }

        try {
            Path uploadPathNext = Paths.get(uploadPath);
            if (!Files.exists(uploadPathNext)) {
                Files.createDirectories(uploadPathNext);
            }

            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileName = String.format("%s.%s", UUID.randomUUID(), fileExtension);
            String filePath = String.format("%s/%s", uploadPath, fileName).replaceAll("//", "/");

            if (oldFile != null) {
                Path oldFileNext = Path.of(oldFile.substring(1));
                if (Files.exists(oldFileNext))
                    Files.delete(oldFileNext);
            }

            ImageIO.write(resizeImage(ImageIO.read(file.getInputStream()), 36, 36), "jpg", new File(filePath));
            return "/" + filePath;
        } catch (Exception e) {
            return null;
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        return Scalr.resize(originalImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT, targetWidth, targetHeight, Scalr.OP_ANTIALIAS);
    }
}

