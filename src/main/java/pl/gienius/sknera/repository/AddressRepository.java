package pl.gienius.sknera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.gienius.sknera.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    public Address getAddressById(Long id);
}
