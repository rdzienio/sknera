package pl.gienius.sknera.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.gienius.sknera.controller.RoleController;
import pl.gienius.sknera.entity.Role;
import pl.gienius.sknera.entity.User;
import pl.gienius.sknera.repository.RoleRepository;

@Configuration
public class InitialDataConfig {

    @Autowired
    private RoleController roleController;

    @Bean
    CommandLineRunner initDatabase(RoleRepository roleRepository) {
        return args -> {
            initRoles(roleRepository);
        };
    }

    private void initRoles(RoleRepository roleRepository) {
        for (Role.Types type : Role.Types.values()) {
            if (roleRepository.findByType(type) == null) {
                roleRepository.save(new Role(type));
            }
        }
    }


    @Bean
    public User userBean(){
        return new User("Marek", "admin");
    }
    @Bean
    public Role roleBean(){
        return new Role(Role.Types.ROLE_ADMIN);
    }

}
