package pl.gienius.sknera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.gienius.sknera.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
