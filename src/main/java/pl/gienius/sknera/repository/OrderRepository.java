package pl.gienius.sknera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.gienius.sknera.entity.Auction;
import pl.gienius.sknera.entity.Order;
import pl.gienius.sknera.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    public Order findOrderById(Long orderId);

    public List<Order> findOrdersById(Long buyerId);

    List<Order> findByAuction_User_Id(Long userId);

    List<Order> findByBuyer_Id(Long buyerId);

    @Query("SELECT a FROM Order a WHERE a.buyer.id = :user")
    List<Order> findOrdersByUser(Long user);
}
