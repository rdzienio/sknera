package pl.gienius.sknera.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.gienius.sknera.entity.Role;
import pl.gienius.sknera.entity.User;
import pl.gienius.sknera.repository.RoleRepository;
import pl.gienius.sknera.repository.UserRepository;

@Controller
public class RegistrationController {

    Logger log = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleController roleController;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        log.info("GET Registration");
        model.addAttribute("user", new User());
        return "registerUser";
    }
    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("user") User user,
                                      @RequestParam(required = false) boolean isSeller) {
        log.info("POST Registration: " + user.getUsername());
        log.info(getUserRole().getType().toString());
        user.addRole(getUserRole());
        String encodedPassword = passwordEncoder.encode(user.getPassword()); //hash hasła
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return "redirect:/login";
    }

    public Role getUserRole(){
        for(Role userRole:roleRepository.findAll()){
            log.info("Role w bazie:" + userRole.getType());
            if(userRole.getType().toString().equals("ROLE_USER")){
                return userRole;
            }
        }
        return new Role();
    }

}

