package pl.gienius.sknera.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import pl.gienius.sknera.entity.User;

import java.io.IOException;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Controller
public class TestController {

    /*@GetMapping("/")
    public ModelAndView serwerInfo() throws IOException {
        var mav = new ModelAndView( "index", "architektura", System.getProperty("os.arch"));
        mav.addObject("nazwa", System.getProperty("os.name"));
        mav.addObject("producentJRE", System.getProperty("java.vendor"));
        mav.addObject("wersjaJRE", System.getProperty("java.version"));
        return mav;
    }*/



    @GetMapping("/test")
    public String showRegistrationForm() {
        return "test";
    }
}
