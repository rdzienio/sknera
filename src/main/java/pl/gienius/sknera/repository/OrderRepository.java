package pl.gienius.sknera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.gienius.sknera.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    public Order findOrderById(Long orderId);
}
