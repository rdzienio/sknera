package pl.gienius.sknera.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.gienius.sknera.entity.Category;
import pl.gienius.sknera.service.CategoryService;
import pl.gienius.sknera.service.SearchService;

import java.util.List;

@Controller
public class SearchController {
    private SearchService searchService;
    private CategoryService categoryService;

    public SearchController(SearchService searchService, CategoryService categoryService) {
        this.searchService = searchService;
        this.categoryService = categoryService;
    }

    @GetMapping("/searchForm")
    public String getSearchForm(){
        return "searchForm";
    }

    @GetMapping("/search")
    public String search(
            @RequestParam("searchQuery") String searchQuery,
            @RequestParam("searchType") String searchType,
            Model model) {
        List<Category> kategorie = categoryService.getAllCategories();
        model.addAttribute("kategorie", kategorie);
        switch (searchType) {
            case "title":
                model.addAttribute("results", searchService.searchByTitle(searchQuery));
                break;
            case "description":
                model.addAttribute("results", searchService.searchByDescription(searchQuery));
                break;
            case "user":
                model.addAttribute("results", searchService.searchByUser(searchQuery));
                break;
            case "dateRange":
                // Przykład, zakładam że masz sposób na przekazanie dat jako dodatkowe parametry
                // model.addAttribute("results", searchService.searchByDateRange(startDate, endDate));
                break;
            default:
                // Domyślne działanie lub błąd
                model.addAttribute("results", "Nieznany typ wyszukiwania.");
                break;
        }
        return "searchResults"; // Nazwa widoku, który wyświetli wyniki
    }
}
