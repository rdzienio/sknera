package pl.gienius.sknera.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
    private OrderService orderService;

    private EmailService emailService;

    /*8@Autowired
    private FileServiceImpl fileServiceImpl;*/

    private BidService bidService;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserRepository reps, AuctionService auctionService, AddressService addressService, ProductService productService, CategoryService categoryService, BidService bidService, OrderService orderService, EmailService emailService){
        this.repository=reps;
        passwordEncoder=new BCryptPasswordEncoder();
        this.auctionService=auctionService;
        this.addressService=addressService;
        this.productService=productService;
        this.categoryService=categoryService;
        this.bidService=bidService;
        this.orderService=orderService;
        this.emailService=emailService;

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
            address = new Address();
        }
        model.addAttribute("address", address);
        return "edit-address";
    }


    @PostMapping("/update-address")
    public String updateAddress(Principal principal, @ModelAttribute("address") Address address) {
        logger.info("POST updateAddress " + address);
        String username = principal.getName();
        User logged = repository.findByUsername(username);
        logged.setAddress(address);
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
        return "redirect:/panel";
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

        return "redirect:/auction-details/" + auctionId;
    }

    @GetMapping("/place-bid/{auctionId}")
    public String placeBid(@PathVariable Long auctionId, Model model) {
        model.addAttribute("auction", auctionService.getAuctionById(auctionId));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "bid-auction";
    }

    @GetMapping("/auction-details/{auctionId}")
    public String auctionDetails(@PathVariable Long auctionId, Model model, Principal principal) {
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


    @PostMapping("/update-auction")
    public String updateAuction(@Valid @ModelAttribute("auction") Auction auction, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "edit-auction";
        }
        auctionService.updateAuction(auction);
        redirectAttributes.addFlashAttribute("successMessage", "Aukcja została pomyślnie zaktualizowana.");
        return "redirect:/auction-details/" + auction.getId();
    }

    @GetMapping("/update-auction/{id}")
    public String showEditAuctionForm(@PathVariable("id") Long id, Model model) {
        Auction auction = auctionService.findById(id);
        model.addAttribute("auction", auction);
        return "edit-auction";
    }

    @GetMapping("/view-orders")
    public String getUserOrders(Principal principal, Model model){
        String username = principal.getName();
        User logged = repository.findByUsername(username);
        List<Order> orderList = new ArrayList<Order>();
        if(logged!=null){
            orderList = orderService.getOrdersByBuyer(logged.getId());
        }
        model.addAttribute("aukcje", orderList);
        return "userOrders";
    }

    @GetMapping("/view-sold")
    public String getSoldOrders(Principal principal, Model model){
        String username = principal.getName();
        User logged = repository.findByUsername(username);
        List<Order> orderList = new ArrayList<Order>();
        if(logged!=null){
            orderList = orderService.getOrdersBySeller(logged.getId());
        }
        model.addAttribute("aukcje", orderList);
        return "soldOrders";
    }

    @GetMapping("/order-edit/{id}")
    public String showOrderEditForm(@PathVariable("id") Long id, Model model) {
        Order order = orderService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));
        model.addAttribute("order", order);
        return "editOrder";
    }


    @PostMapping("/order-update")
    public String updateOrder(@ModelAttribute("order") Order order, RedirectAttributes redirectAttributes) {

        Order existingOrder = orderService.findById(order.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + order.getId()));

        orderService.updateStatus(existingOrder.getId(), order.getStatus());
        String content = "<h1>Zmiana statusu zamówienia</h1>" +
                "<p>Zamówienie " + existingOrder.getAuction().getTitle() + " zmieniło status na: " + order.getStatus() + "!</p>";
        emailService.sendOrderConfirmationEmail(existingOrder.getBuyer().getEmail(), "[Sknera] Potwierdzenie zamówienia", content);

        redirectAttributes.addFlashAttribute("successMessage", "Zamówienie zostało zaktualizowane.");
        return "redirect:/view-sold";
    }

    @GetMapping("/view-bids")
    public String getBids(Principal principal, Model model){
        String username = principal.getName();
        User logged = repository.findByUsername(username);
        List<Bid> bidList = new ArrayList<Bid>();
        if(logged!=null){
            bidList = bidService.getBidsByUser(logged);
        }
        model.addAttribute("oferty", bidList);
        return "showBids";
    }

}
