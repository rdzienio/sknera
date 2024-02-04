package pl.gienius.sknera.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.gienius.sknera.entity.Address;
import pl.gienius.sknera.entity.Category;
import pl.gienius.sknera.entity.Product;
import pl.gienius.sknera.service.*;

import java.util.List;
import java.util.Optional;

@Controller()
@RequestMapping("/admin")
public class AdminController {

    private ProductService productService;
    private CategoryService categoryService;

    private AuctionService auctionService;

    private OrderService orderService;

    private BidService bidService;

    public AdminController(ProductService productService, CategoryService categoryService, AuctionService auctionService, OrderService orderService, BidService bidService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.auctionService = auctionService;
        this.orderService = orderService;
        this.bidService = bidService;
    }

    @GetMapping
    public String getAdminPage(Model model) {
        model.addAttribute("orderCount", orderService.orderCount());
        model.addAttribute("activeAuctionsCount", auctionService.countActiveAuctions());
        model.addAttribute("endedAuctionsCount", auctionService.countEndedAuctions());
        model.addAttribute("bidCount", bidService.countBids());
        return "adminPage";
    }

    @GetMapping("/products/add")
    public String showAddProductForm(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("product", new Product());
        return "add-product";
    }

    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute Product product) {
        productService.addProduct(product);
        return "redirect:/admin";
    }

    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getSortedProducts());
        return "produkty";
    }
    @GetMapping("/categories")
    public String getCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "categoryDetails";
    }

    // Metoda do wyświetlania formularza edycji
    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.findById(id).orElseGet(null);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "edit-product"; // Nazwa widoku formularza do edycji produktu
    }

    // Metoda do przetwarzania żądania edycji produktu
    @PostMapping("/products/edit/{id}")
    public String updateProduct(@PathVariable("id") Long id, @ModelAttribute("product") Product product) {
        productService.addProduct(product);
        return "redirect:/admin/products";
    }

    // Metoda do usuwania produktu
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteById(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/categories/add")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "add-category";
    }

    @PostMapping("/categories/add")
    public String addCategory(@ModelAttribute Category category) {
        categoryService.addCategory(category);
        return "redirect:/admin";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteById(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String showEditCategoryForm(@PathVariable("id") Long id, Model model) {
        Category category = categoryService.findById(id).orElseGet(null);
        model.addAttribute("category", category);
        return "edit-category";
    }

    // Metoda do przetwarzania żądania edycji produktu
    @PostMapping("/categories/edit/{id}")
    public String updateCategory(@PathVariable("id") Long id, @ModelAttribute("category") Category category) {
        categoryService.addCategory(category);
        return "redirect:/admin/categories";
    }
}
