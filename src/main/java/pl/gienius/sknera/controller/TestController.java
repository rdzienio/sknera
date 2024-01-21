package pl.gienius.sknera.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.io.IOException;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Controller
public class TestController {

    @GetMapping("/")
    public ModelAndView serwerInfo() throws IOException {
        var mav = new ModelAndView( "index", "architektura", System.getProperty("os.arch"));
        mav.addObject("nazwa", System.getProperty("os.name"));
        mav.addObject("producentJRE", System.getProperty("java.vendor"));
        mav.addObject("wersjaJRE", System.getProperty("java.version"));
        return mav;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        var mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        http
                .exceptionHandling((config)-> config.accessDeniedPage("/url_error403"));
        http.
                csrf(csrf -> csrf.ignoringRequestMatchers(toH2Console()).disable());
        http.
                headers(headers -> headers.frameOptions(frameOptionsConfig -> frameOptionsConfig.sameOrigin()));


        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(
                                mvcMatcherBuilder.pattern("/"),
                                mvcMatcherBuilder.pattern("/*"),
                                //mvcMatcherBuilder.pattern("/accessDenied"),
                                mvcMatcherBuilder.pattern("/produkty")
                        ).permitAll()
                        .requestMatchers(
                                //mvcMatcherBuilder.pattern("/produkty/"),
                                mvcMatcherBuilder.pattern("/produkty/show**")
                        ).hasRole("USER")
                        .requestMatchers(
                                mvcMatcherBuilder.pattern("/admin/**"),
                                mvcMatcherBuilder.pattern("/produkty/edit**"),
                                mvcMatcherBuilder.pattern("/produkty/add**"),
                                mvcMatcherBuilder.pattern("/produkty/saveProduct")
                        ).hasRole("ADMIN")
                        .requestMatchers(
                                mvcMatcherBuilder.pattern("/db/**"),
                                mvcMatcherBuilder.pattern("/admin/**"),
                                mvcMatcherBuilder.pattern("/produkty/show**"),
                                mvcMatcherBuilder.pattern("/produkty/edit**"),
                                mvcMatcherBuilder.pattern("/produkty/add**"),
                                mvcMatcherBuilder.pattern("/produkty/saveProduct")
                        ).access(new WebExpressionAuthorizationManager("hasRole('ADMIN') and hasRole('USER')"))

                        .anyRequest()
                        .authenticated()//każde żądanie ma być uwierzytelnione
                );
        http. //uwierzytelnienie poprzez formularz logowania
                formLogin((form) -> form
                .loginPage("/login")//formularz dostępny jest pod URL
                .permitAll()
        );
        http.logout((logout) -> logout.permitAll());
        return http.build();
    }
}
