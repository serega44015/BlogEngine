package main.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import main.dto.api.response.CaptchaResponse;
import main.model.CaptchaCode;
import main.model.repositories.CaptchaCodeRepository;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
public class CaptchaService {

    private final CaptchaCodeRepository captchaRepository;

    public CaptchaService(CaptchaCodeRepository captchaRepository) {
        this.captchaRepository = captchaRepository;
    }

    public CaptchaResponse getCaptcha() throws IOException{
        captchaRepository.findAll().forEach(captcha -> {
            if (captcha.getTime().isBefore(LocalDateTime.now().minusHours(1))) {
                captchaRepository.delete(captcha);
            }
        });

        CaptchaResponse captchaResponse = new CaptchaResponse();
        CaptchaCode captchaCodes = new CaptchaCode();

        LocalDateTime time = LocalDateTime.now();
        Cage cage = new GCage();

        String captchaCode = cage.getTokenGenerator().next();
        String secretCode = cage.getTokenGenerator().next();

        OutputStream os = new FileOutputStream("captcha.jpg", false);
        cage.draw(captchaCode, os);
        os.flush();
        os.close();

        byte[] captcha = Files.readAllBytes(Paths.get("captcha.jpg"));
        Files.delete(Paths.get("captcha.jpg"));
        String encodedCaptcha = DatatypeConverter.printBase64Binary(captcha);
        String image = "data:image/png;base64, " + encodedCaptcha;

        captchaCodes.setTime(time);
        captchaCodes.setCode(captchaCode);
        captchaCodes.setSecretCode(secretCode);
        captchaRepository.save(captchaCodes);


        captchaResponse.setSecret(captchaCode);
        captchaResponse.setImage(image);

        return captchaResponse;
    }


}
