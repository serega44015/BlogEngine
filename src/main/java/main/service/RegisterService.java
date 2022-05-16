package main.service;

import main.dto.ErrorsDTO;
import main.dto.api.request.RegisterRequest;
import main.dto.api.response.RegisterResponse;
import main.model.User;
import main.model.repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Date;
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
        ErrorsDTO errorsDTO = new ErrorsDTO();
        User user = new User();

        String email = registerRequest.getEmail();
        String name = registerRequest.getName();
        String password = registerRequest.getPassword();
        String captcha = registerRequest.getCaptcha();
        String secretCaptcha = registerRequest.getCaptchaSecret();

        boolean validEmail = userRepository.findByEmail(email).isPresent() ? false : true;
        boolean validName = isValidName(name);
        boolean validPassword = isValidPassword(password);
        boolean validCaptcha = captcha.equals(secretCaptcha);

        registerResponse.setResult(false);

        if (!validEmail) {
            errorsDTO.setEmail("Этот e-mail уже зарегистрирован");
        } else if (!validName) {
            errorsDTO.setName("Имя указано неверно");
        } else if (!validPassword) {
            errorsDTO.setPassword("Пароль короче 6-ти символов");
        } else if (!validCaptcha) {
            errorsDTO.setCaptcha("Код с картинки введён неверно");
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

        registerResponse.setErrorsDTO(errorsDTO);

        return registerResponse;
    }


    private boolean isValidName(String username) {
        String USERNAME_PATTERN = "^[\\p{L} .'-]+$";
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6 ? true : false;
    }
}
