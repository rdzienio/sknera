package pl.gienius.sknera.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.gienius.sknera.entity.Auction;
import pl.gienius.sknera.entity.User;
import pl.gienius.sknera.repository.UserRepository;
import pl.gienius.sknera.service.AuctionService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private final UserRepository repository;
    private PasswordEncoder passwordEncoder;

    private AuctionService auctionService;

    public UserController(UserRepository reps, AuctionService auctionService){
        this.repository=reps;
        passwordEncoder=new BCryptPasswordEncoder();
        this.auctionService=auctionService;

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

    @GetMapping("/panel")
    public String userPanel(Principal principal, Model model){
        String username = principal.getName();
        User logged = repository.findByUsername(username);
        List<Auction> auctionList = new ArrayList<Auction>();
        if(logged!=null){
            auctionList = auctionService.getActiveAuctionsForUser(logged);
        }
        model.addAttribute("aukcje", auctionList);
        return "client-panel";
    }


}
