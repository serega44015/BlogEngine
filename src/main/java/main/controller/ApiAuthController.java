package main.controller;

import main.dto.api.request.LoginRequest;
import main.dto.api.request.RegisterRequest;
import main.dto.api.request.RestoreRequest;
import main.dto.api.response.*;
import main.model.repositories.UserRepository;
import main.service.CaptchaService;
import main.service.CheckService;
import main.service.EmailService;
import main.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final CheckService checkService;
    private final CaptchaService captchaService;
    private final RegisterService registerService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Autowired
    public ApiAuthController(CheckService checkService, CaptchaService captchaService, RegisterService registerService, EmailService emailService, AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.checkService = checkService;
        this.captchaService = captchaService;
        this.registerService = registerService;
        this.emailService = emailService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @GetMapping("/check")
    public ResponseEntity<LoginResponse> check(Principal principal) {
        if (principal == null) {
            return ResponseEntity.ok(new LoginResponse());
        }

        return ResponseEntity.ok(checkService.getLoginResponse(principal.getName()));
    }

    @GetMapping("/captcha")
    private CaptchaResponse captcha() throws IOException {
        return captchaService.getCaptcha();
    }

    @PostMapping("/register")
    private RegisterResponse register(@RequestBody RegisterRequest registerRequest) {
        return registerService.registration(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = (User) auth.getPrincipal();

        return ResponseEntity.ok(checkService.getLoginResponse(user.getUsername()));
    }

    @GetMapping("/logout")
    @PreAuthorize("hasAuthority('user:write')")
    public LogoutResponse logout() {
        LogoutResponse logoutResponse = new LogoutResponse();
        logoutResponse.setResult(true);
        SecurityContextHolder.getContext().setAuthentication(null);
        return logoutResponse;
    }

    @PostMapping("/restore")
    public PasswordRestoreResponse passwordRestore(@RequestBody RestoreRequest restoreRequest) {
        System.out.println("А контролле?");
        return emailService.restore(restoreRequest);
    }


}
