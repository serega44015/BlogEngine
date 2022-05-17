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

    //он тоже должен быть в сервисе
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


//    public CheckResponse getResult(){
//        CheckResponse checkResponse = new CheckResponse();
//        checkResponse.setResult(false);
//
//
//        UserLoginDTO userLoginDTO = new UserLoginDTO();
//        Optional<User> userOptional = userRepository.findByEmail("serega44015@gmail.com");
//
//        userLoginDTO.setId(userOptional.get().getId());
//        userLoginDTO.setName(userOptional.get().getName());
//        userLoginDTO.setPhoto(userOptional.get().getPhoto());
//        userLoginDTO.setEmail(userOptional.get().getEmail());
//        userLoginDTO.setModeration(false);
//        userLoginDTO.setModerationCount(userOptional.get().getIsModerator());
//        userLoginDTO.setSettings(true);
//
//        checkResponse.setUserLoginDTO(userLoginDTO);
//
//        return checkResponse;
//    }
}
