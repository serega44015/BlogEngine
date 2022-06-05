package main.service;

import main.config.MailSender;
import main.dto.ErrorPasswordChangeDto;
import main.dto.api.request.ChangePasswordRequest;
import main.dto.api.request.RestoreRequest;
import main.dto.api.response.PasswordChangeResponse;
import main.dto.api.response.PasswordRestoreResponse;
import main.model.CaptchaCode;
import main.model.User;
import main.model.repositories.CaptchaCodeRepository;
import main.model.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.UUID;

@Service
public class EmailService {
  private final UserRepository userRepository;
  private final CaptchaCodeRepository captchaCodeRepository;
  private final MailSender mailSender;

  public EmailService(
      UserRepository userRepository,
      CaptchaCodeRepository captchaCodeRepository,
      MailSender mailSender) {
    this.userRepository = userRepository;
    this.captchaCodeRepository = captchaCodeRepository;
    this.mailSender = mailSender;
  }

  public PasswordRestoreResponse restore(RestoreRequest restoreRequest) {
    PasswordRestoreResponse response = new PasswordRestoreResponse();
    User user = userRepository.findByEmail(restoreRequest.getEmail()).get();

    if (Objects.isNull(user) || StringUtils.isEmpty(restoreRequest.getEmail())) {
      response.setResult(false);
      return response;
    }

    user.setCode(UUID.randomUUID().toString());
    userRepository.save(user);

    String message =
        String.format(
            "Hello, %s \n "
                + "Dear user, please follow the link: http://localhost:8080/login/change-password/%s to reset your password.",
            user.getName(), user.getCode());

    mailSender.sends(user.getEmail(), "Restore Password", message);
    response.setResult(true);

    return response;
  }

  public PasswordChangeResponse change(ChangePasswordRequest changePasswordRequest) {
    PasswordChangeResponse passwordChangeResponse = new PasswordChangeResponse();
    ErrorPasswordChangeDto errorPasswordChangeDto = new ErrorPasswordChangeDto();
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    passwordChangeResponse.setResult(true);

    User user = userRepository.findByCode(changePasswordRequest.getCode());

    if (Objects.isNull(user)) {
      passwordChangeResponse.setResult(true);
      errorPasswordChangeDto.setPassword(
          "Ссылка для восстановления пароля устарела.\n"
              + " <a href=\n"
              + " \\\"/auth/restore\\\">Запросить ссылку снова</a>");
    } else {
      if (changePasswordRequest.getPassword().length() >= 6) {
        passwordChangeResponse.setResult(true);
        user.setPassword(encoder.encode(changePasswordRequest.getPassword()));
      } else {
        passwordChangeResponse.setResult(false);
        errorPasswordChangeDto.setPassword("Пароль короче 6-ти символов");
      }

      CaptchaCode captchaCode =
          captchaCodeRepository.findByCode(changePasswordRequest.getCaptchaSecret());
      if (checkCaptcha(changePasswordRequest.getCaptcha(), captchaCode)){
          userRepository.save(user);
      } else {
        passwordChangeResponse.setResult(false);
        errorPasswordChangeDto.setCaptcha("Код с картинки введен неверно");
      }

    }
    passwordChangeResponse.setErrors(errorPasswordChangeDto);
    return passwordChangeResponse;
  }

  private boolean checkCaptcha(String captcha, CaptchaCode repoCaptcha) {
    Boolean check = true;
    Boolean captchaCorrect = captcha.equals(repoCaptcha.getCode());

    if (!captchaCorrect) {
      check = false;
    }
    return check;
  }
}
