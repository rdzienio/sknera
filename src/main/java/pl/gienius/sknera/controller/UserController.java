package pl.gienius.sknera.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.gienius.sknera.entity.Address;
import pl.gienius.sknera.entity.Auction;
import pl.gienius.sknera.entity.User;
import pl.gienius.sknera.repository.UserRepository;
import pl.gienius.sknera.service.*;
import pl.gienius.sknera.entity.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private final UserRepository repository;
    private PasswordEncoder passwordEncoder;

    private AuctionService auctionService;
    private AddressService addressService;
    private ProductService productService;
    private CategoryService categoryService;

    private BidService bidService;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserRepository reps, AuctionService auctionService, AddressService addressService, ProductService productService, CategoryService categoryService, BidService bidService){
        this.repository=reps;
        passwordEncoder=new BCryptPasswordEncoder();
        this.auctionService=auctionService;
        this.addressService=addressService;
        this.productService=productService;
        this.categoryService=categoryService;
        this.bidService=bidService;

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

    @GetMapping("/edit-address")
    public String editAddress(Principal principal, Model model){
        String username = principal.getName();
        User logged = repository.findByUsername(username);
        Address address =  logged.getAddress();
        if (address == null) {
            address = new Address(); // Nowy adres, jeśli użytkownik nie ma jeszcze adresu
        }
        model.addAttribute("address", address);
        return "edit-address";
    }

    // Metoda do przetwarzania formularza edycji adresu
    @PostMapping("/update-address")
    public String updateAddress(Principal principal, @ModelAttribute("address") Address address) {
        logger.info("POST updateAddress " + address);
        String username = principal.getName();
        User logged = repository.findByUsername(username);
        logged.setAddress(address);
        // Zaktualizuj adres użytkownika
        addressService.updateAddress(address);
        return "edit-address";
    }

    @GetMapping("/add-auction")
    public String addAuction(Model model){
        model.addAttribute("auction", new Auction());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("products", productService.getProducts());
        return "create-auction";
    }

    @PostMapping("/save-auction")
    public String createAuction(Principal principal, @ModelAttribute("auction") Auction auction, @RequestParam("obraz") MultipartFile obraz) {
        String filename = StringUtils.cleanPath(obraz.getOriginalFilename());
        try {
            Path targetLocation = Paths.get("src/main/resources/static/img").resolve(filename);
            Files.copy(obraz.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        String username = principal.getName();
        User logged = repository.findByUsername(username);
        auction.setUser(logged);
        auction.setImage(filename);
        auctionService.addAuction(auction);
        return "redirect:/panel"; // Przekieruj po pomyślnym utworzeniu aukcji
    }

    @PostMapping("/place-bid/{auctionId}")
    public String saveBid(@PathVariable Long auctionId, @RequestParam("bidAmount") Double bidAmount, Principal principal) {
        String username = principal.getName();
        User user = repository.findByUsername(username);
        Auction bidAuction = auctionService.getAuctionById(auctionId);
        Bid bid = new Bid();
        bid.setPrice(bidAmount);
        bid.setUser(user);
        bid.setAuction(bidAuction);
        bidService.addBid(bid);
        auctionService.addBidToAuction(bidAuction, bid);

        return "redirect:/auction-details/" + auctionId; // Przekierowanie po złożeniu oferty
    }

    @GetMapping("/place-bid/{auctionId}")
    public String placeBid(@PathVariable Long auctionId, Model model) {
        model.addAttribute("auction", auctionService.getAuctionById(auctionId));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "bid-auction";
    }

    @GetMapping("/auction-details/{auctionId}")
    public String auctionDetails(@PathVariable Long auctionId, Model model) {
        model.addAttribute("auction", auctionService.getAuctionById(auctionId));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "auction-details";
    }

    @GetMapping("/view-auctions")
    public String showAuctions(Principal principal, Model model){
        String username = principal.getName();
        User logged = repository.findByUsername(username);
        List<Auction> auctionList = new ArrayList<Auction>();
        if(logged!=null){
            auctionList = auctionService.getActiveAuctionsForUser(logged);
        }
        model.addAttribute("aukcje", auctionList);
        return "viewAuctions";
    }


}
