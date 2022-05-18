package main.service;

import main.dto.api.response.CheckResponse;
import main.dto.UserLoginDTO;
import main.dto.api.response.LoginResponse;
import main.model.User;
import main.model.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CheckService {

    private final UserRepository userRepository;

    public CheckService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginResponse getLoginResponse(String email) {
        main.model.User currentUser = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setEmail(currentUser.getEmail());
        userLoginDTO.setName(currentUser.getName());
        userLoginDTO.setModeration(currentUser.getIsModerator() == 1);
        userLoginDTO.setId(currentUser.getId());

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setResult(true);
        loginResponse.setUserLoginDTO(userLoginDTO);
        return loginResponse;
    }

}
