package pl.gienius.sknera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.gienius.sknera.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}

