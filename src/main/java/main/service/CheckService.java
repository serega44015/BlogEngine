package main.service;

import main.dto.UserLoginDto;
import main.dto.api.response.LoginResponse;
import main.model.User;
import main.model.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CheckService {

  private final UserRepository userRepository;

  public CheckService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public LoginResponse getLoginResponse(String email) {

    User user = userRepository.findByEmail(email);
    if (Objects.isNull(user)) {
      throw new UsernameNotFoundException("user" + email + " not found");
    }

    //TODO Логин в самое конце тогда в мапперы
    UserLoginDto userLoginDTO = new UserLoginDto();
    userLoginDTO.setEmail(user.getEmail());
    userLoginDTO.setName(user.getName());
    userLoginDTO.setModeration(user.getIsModerator() == 1);
    userLoginDTO.setId(user.getId());
    userLoginDTO.setPhoto(user.getPhoto());

    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setResult(true);
    loginResponse.setUserLoginDTO(userLoginDTO);
    return loginResponse;
  }
}
