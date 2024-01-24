package pl.gienius.sknera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.gienius.sknera.entity.Category;
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
