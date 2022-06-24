package main.service;

import main.dto.UserLoginDto;
import main.dto.api.response.LoginResponse;
import main.mappers.UserMapper;
import main.model.User;
import main.model.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static main.mappers.converter.ResultValue.ONE;

@Service
public class CheckService {

  private final UserRepository userRepository;
  private final UserMapper userMapper = UserMapper.INSTANCE;

  public CheckService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public LoginResponse getLoginResponse(String email) {

    User user = userRepository.findByEmail(email);
    if (Objects.isNull(user)) {
      throw new UsernameNotFoundException("user" + email + " not found");
    }

    UserLoginDto userLoginDTO = userMapper.toUserLoginDto(user);
    userLoginDTO.setModeration(user.getIsModerator() == ONE);

    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setResult(true);
    loginResponse.setUserLoginDTO(userLoginDTO);
    return loginResponse;
  }
}
