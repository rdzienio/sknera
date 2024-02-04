package pl.gienius.sknera.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.gienius.sknera.entity.Auction;
import pl.gienius.sknera.entity.Category;
import pl.gienius.sknera.entity.Product;
import pl.gienius.sknera.service.AuctionService;
import pl.gienius.sknera.service.CategoryService;
import pl.gienius.sknera.service.ProductService;

import java.util.List;

@Controller
public class HomeController {

    private CategoryService categoryService;
    private ProductService productService;

    private AuctionService auctionService;

    public HomeController(CategoryService categoryService, ProductService productService, AuctionService auctionService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.auctionService = auctionService;
    }

    @GetMapping({"/", "/index"})
    public String homePage(Model model) {
        List<Category> kategorie = categoryService.getAllCategories();
        //List<Product> produkty = productService.getProducts();
        List<Auction> aukcje = auctionService.getLatestAuctions();

        model.addAttribute("kategorie", kategorie);
        model.addAttribute("aukcje", aukcje);

        return "index"; // Nazwa szablonu Thymeleaf
    }

    @GetMapping("/category/{categoryId}")
    public String getAuctionsByCategory(@PathVariable Long categoryId, Model model) {
        List<Category> kategorie = categoryService.getAllCategories();
        List<Auction> aukcje = auctionService.getCurrentAuctionsForCategory(categoryId);
        model.addAttribute("kategorie", kategorie);
        model.addAttribute("aukcje", aukcje);
        model.addAttribute("tytul", categoryService.getCategory(categoryId).getNazwa());
        return "kategorie";
    }

    @GetMapping({"/all"})
    public String allAuctions(Model model) {
        List<Category> kategorie = categoryService.getAllCategories();
        List<Auction> aukcje = auctionService.getActiveAuctions();

        model.addAttribute("kategorie", kategorie);
        model.addAttribute("aukcje", aukcje);

        return "wszystkieAukcje";
    }
}
