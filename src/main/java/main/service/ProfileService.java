package main.service;

import main.dto.ErrorProfileDto;
import main.dto.api.request.ProfileRequest;
import main.dto.api.response.ModerationResponse;
import main.dto.api.response.ProfileResponse;
import main.model.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class ProfileService {
  private final UserRepository userRepository;

  public ProfileService(UserRepository userRepository) {
    this.userRepository = userRepository;
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

    if (email != null && userRepository.findByEmail(email).get() != null) {
      currentUser.setEmail(email);
    } else {
      errorProfileDto.setEmail("Этот e-mail уже зарегистрирован");
      profileResponse.setErrors(errorProfileDto);
      profileResponse.setResult(false);
      return profileResponse;
    }

    if (password != null) {
      BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
      String encodedPassword = passwordEncoder.encode(password);
      currentUser.setPassword(encodedPassword);
    }

    if (name != null) {
      currentUser.setName(name);
    }

    if (delPhoto != null && delPhoto == 1){
      //TODO как сделаю фотки, посмотреть будет ли работать
      currentUser.setPhoto("");
    }
    userRepository.save(currentUser);
    System.out.println("Доходим?");
    //TODO завтра продолжим
    return profileResponse;
  }
}
