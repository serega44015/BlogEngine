package main.service;

import main.api.response.CheckResponse;
import main.dto.UserLoginDTO;
import main.model.User;
import main.model.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CheckService {

    private final UserRepository userRepository;

    public CheckService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CheckResponse getResult(){
        CheckResponse checkResponse = new CheckResponse();
        checkResponse.setResult(false);


        UserLoginDTO userLoginDTO = new UserLoginDTO();
        Optional<User> userOptional = userRepository.findByEmail("serega44015@gmail.com");

        userLoginDTO.setId(userOptional.get().getId());
        userLoginDTO.setName(userOptional.get().getName());
        userLoginDTO.setPhoto(userOptional.get().getPhoto());
        userLoginDTO.setEmail(userOptional.get().getEmail());
        userLoginDTO.setModeration(false);
        userLoginDTO.setModerationCount(userOptional.get().getIsModerator());
        userLoginDTO.setSettings(true);

        checkResponse.setUser(userLoginDTO);

        return checkResponse;
    }
}
