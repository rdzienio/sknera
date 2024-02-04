package pl.gienius.sknera.service;

import org.springframework.stereotype.Service;
import pl.gienius.sknera.entity.Product;
import pl.gienius.sknera.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }
    public List<Product> getSortedProducts() {
        return productRepository.findAllSortedById();
    }


    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
