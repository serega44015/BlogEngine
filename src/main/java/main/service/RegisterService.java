package main.service;

import main.dto.ErrorDto;
import main.dto.api.request.RegisterRequest;
import main.dto.api.response.RegisterResponse;
import main.model.User;
import main.model.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RegisterService {
  private final UserRepository userRepository;

  public RegisterService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public RegisterResponse registration(RegisterRequest registerRequest) {
    RegisterResponse registerResponse = new RegisterResponse();
    ErrorDto errorDTO = new ErrorDto();
    User user = new User();

    String email = registerRequest.getEmail();
    String name = registerRequest.getName();
    String password = registerRequest.getPassword();
    String captcha = registerRequest.getCaptcha();
    String secretCaptcha = registerRequest.getCaptchaSecret();

    Boolean validEmail = Objects.nonNull(userRepository.findByEmail(email)) ? false : true;
    Boolean validName = isValidName(name);
    Boolean validPassword = isValidPassword(password);
    Boolean validCaptcha = captcha.equals(secretCaptcha);

    registerResponse.setResult(false);

    if (!validEmail) {
      errorDTO.setEmail("Этот e-mail уже зарегистрирован");
    } else if (!validName) {
      errorDTO.setName("Имя указано неверно");
    } else if (!validPassword) {
      errorDTO.setPassword("Пароль короче 6-ти символов");
    } else if (!validCaptcha) {
      errorDTO.setCaptcha("Код с картинки введён неверно");
    } else {
      user.setEmail(email);
      user.setName(name);
      user.setPassword(password);
      user.setRegTime(new Date());
      user.setIsModerator(0);
      user.setPhoto("img");
      userRepository.save(user);

      registerResponse.setResult(true);
    }

    registerResponse.setErrorDTO(errorDTO);

    return registerResponse;
  }

  private Boolean isValidName(String username) {
    String USERNAME_PATTERN = "^[\\p{L} .'-]+$";
    Pattern pattern = Pattern.compile(USERNAME_PATTERN);
    Matcher matcher = pattern.matcher(username);
    return matcher.matches();
  }

  private Boolean isValidPassword(String password) {
    return password.length() >= 6 ? true : false;
  }
}
