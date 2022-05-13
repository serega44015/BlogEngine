package main.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import main.dto.api.response.CaptchaResponse;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class CaptchaService {

    public CaptchaResponse getCaptcha() {
        CaptchaResponse captchaResponse = new CaptchaResponse();

        Cage cage = new GCage();
        String token = cage.getTokenGenerator().next();
        byte[] byteImage = cage.draw(token);
        String encoded = Base64
                .getEncoder()
                .encodeToString(byteImage);
        String image = "data:image/png;base64, " + encoded;
        String secret = String.valueOf(cage.getFormat().getBytes(StandardCharsets.UTF_8));

        captchaResponse.setImage(image);
        captchaResponse.setSecret(secret);

        return captchaResponse;
    }

}
