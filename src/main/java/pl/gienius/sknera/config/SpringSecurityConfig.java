package pl.gienius.sknera.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,HandlerMappingIntrospector introspector) throws Exception {


        var mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);


        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(
                                mvcMatcherBuilder.pattern("/"),
                                mvcMatcherBuilder.pattern("/resources/static"),
                                mvcMatcherBuilder.pattern("/signup"),
                                mvcMatcherBuilder.pattern("/register"),
                                mvcMatcherBuilder.pattern("/flightList"),
                                mvcMatcherBuilder.pattern("/index**"),
                                mvcMatcherBuilder.pattern("/resources/**")

                        ).permitAll()
                        .requestMatchers(
                                mvcMatcherBuilder.pattern("/admin/**"),
                                mvcMatcherBuilder.pattern("/test")
                        ).hasRole("ADMIN")
                        .requestMatchers(
                                mvcMatcherBuilder.pattern("/db/**")
                        ).access(new WebExpressionAuthorizationManager("hasRole('ADMIN') and hasRole('DBA')"))
                        .anyRequest()
                        .authenticated()
                );

        http.
                formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                );

        http.exceptionHandling((config)-> config.accessDeniedPage("/url_error403"));

        http
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        /// http.logout((logout) -> logout.permitAll());
        http.csrf(config -> config.disable());

        return http.build();

    }
}
