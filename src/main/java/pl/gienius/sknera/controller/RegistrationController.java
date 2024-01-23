package pl.gienius.sknera.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.gienius.sknera.entity.User;
import pl.gienius.sknera.repository.UserRepository;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registerUser";
    }
    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("user") User user,
                                      @RequestParam(required = false) boolean isSeller) {
        if (isSeller) {
            user.setRole("SELLER");
        } else {
            user.setRole("USER");
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword()); //hash hasła
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return "redirect:/login";
    }

}

