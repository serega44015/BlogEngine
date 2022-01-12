package main.service;

import main.api.response.CheckResponse;
import org.springframework.stereotype.Service;

@Service
public class CheckService {
    public CheckResponse getResult(){
        CheckResponse checkResponse = new CheckResponse();
        return checkResponse;
    }
}
