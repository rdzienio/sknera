package pl.gienius.sknera.service;

import org.springframework.stereotype.Service;
import pl.gienius.sknera.entity.Product;
import pl.gienius.sknera.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }
}
