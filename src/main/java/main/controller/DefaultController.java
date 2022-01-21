package main.controller;

import main.dto.api.response.InitResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultController {

    private final InitResponse initResponse;

    public DefaultController(InitResponse initResponse){
        this.initResponse = initResponse;
    }

    @RequestMapping("/")
    public String defaultController() {
        System.out.println("serega " + initResponse.getTitle());
        return "index.html";
    }

}
