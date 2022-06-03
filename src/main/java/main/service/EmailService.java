package main.service;

import main.config.EmailConfig;
import main.dto.api.request.RestoreRequest;
import main.dto.api.response.PasswordRestoreResponse;
import main.model.User;
import main.model.repositories.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class EmailService {
    private final UserRepository userRepository;
    private final EmailConfig emailConfig;

    public EmailService(UserRepository userRepository, EmailConfig emailConfig) {
        this.userRepository = userRepository;
        this.emailConfig = emailConfig;
    }

    public PasswordRestoreResponse restore(RestoreRequest restoreRequest){
        //TODO дома поделать, чёт тут мб не работает с этого компа
        PasswordRestoreResponse response = new PasswordRestoreResponse();
        String HASH = UUID.randomUUID().toString();

        Optional<User> optionalUser = userRepository.findByEmail(restoreRequest.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setCode(HASH);
            userRepository.save(user);

            String message = String.format(
                    "Hello, %s \n"
                            + "To restore password please link: https://movie-blog-java-skillbox.herokuapp.com/login/change-password/%s",
                    user.getName(), HASH
            );

            send(user.getEmail(), "Restore Password", message);

            response.setResult(true);
        }
        System.out.println("И дошли");
        return response;
    }

    public void send(String emailTo, String subject, String message) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(this.emailConfig.getHost());
        mailSender.setPort(this.emailConfig.getPort());
        mailSender.setUsername(this.emailConfig.getUsername());
        mailSender.setPassword(this.emailConfig.getPassword());

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(this.emailConfig.getUsername());
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}
