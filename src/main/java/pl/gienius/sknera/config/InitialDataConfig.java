package pl.gienius.sknera.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.gienius.sknera.entity.Role;
import pl.gienius.sknera.entity.User;
import pl.gienius.sknera.repository.RoleRepository;

@Configuration
public class InitialDataConfig {

    @Bean
    public User userBean(){
        return new User("Marek", "admin");
    }
    @Bean
    public Role roleBean(){
        return new Role(Role.Types.ROLE_ADMIN);
    }
}
