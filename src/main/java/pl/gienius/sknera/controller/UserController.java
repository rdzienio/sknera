package pl.gienius.sknera.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import pl.gienius.sknera.entity.User;
import pl.gienius.sknera.repository.UserRepository;

@Controller
public class UserController {
    @Autowired
    private final UserRepository repository;
    private PasswordEncoder passwordEncoder;

    public UserController(UserRepository reps){
        this.repository=reps;
        passwordEncoder=new BCryptPasswordEncoder();

    }
    public void saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
    }
    public Iterable<User> returnAll(){
        return repository.findAll();
    }
    public User findByUsername(String name){
        return repository.findByUsername(name);
    }


}
