package main.controller;

import main.dto.api.request.RegisterRequest;
import main.dto.api.response.CaptchaResponse;
import main.dto.api.response.CheckResponse;
import main.dto.api.response.RegisterResponse;
import main.service.CaptchaService;
import main.service.CheckService;
import main.service.RegisterService;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final CheckService checkService;
    private final CaptchaService captchaService;
    private final RegisterService registerService;

    public ApiAuthController(CheckService checkService, CaptchaService captchaService, RegisterService registerService) {
        this.checkService = checkService;
        this.captchaService = captchaService;
        this.registerService = registerService;
    }

    @GetMapping("/check")
    private CheckResponse check() {
        return checkService.getResult();
    }

    @GetMapping("/captcha")
    private CaptchaResponse captcha() throws IOException{
        return captchaService.getCaptcha();
    }

    @PostMapping("/register")
    private RegisterResponse register(@RequestBody RegisterRequest registerRequest){
        return registerService.registration(registerRequest);
    }
}
