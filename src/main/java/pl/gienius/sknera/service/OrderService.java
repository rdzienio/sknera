package pl.gienius.sknera.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.gienius.sknera.entity.Auction;
import pl.gienius.sknera.entity.Bid;
import pl.gienius.sknera.entity.Order;
import pl.gienius.sknera.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private OrderRepository orderRepository;

    Logger logger = LoggerFactory.getLogger(OrderService.class);

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void addOrder(Order order) {
        logger.info("New order by user: " + order.getBuyer().getUsername());
        orderRepository.save(order);
    }
    public void createOrderFromBid(Auction auction, Bid bid) {
        Order newOrder = new Order();
        newOrder.setBuyer(bid.getUser());
        newOrder.setStatus(Order.OrderStatus.CREATED);
        newOrder.setAuction(auction);
        newOrder.setOrderDate(auction.getEndDate());
        addOrder(newOrder);
    }

    public void updateStatus(Long orderId, Order.OrderStatus status) {
        Order order = orderRepository.findOrderById(orderId);
        order.setStatus(status);
        logger.info("Updated order with ID: " + order.getId() + ", to: "+ order.getStatus().toString());
        orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Integer orderCount() {
        return getAllOrders().size();
    }

    public List<Order> getOrdersByBuyer(Long buyerId) {
        return orderRepository.findOrdersByUser(buyerId);
    }

    public List<Order> getOrdersBySeller(Long sellerId){
        return orderRepository.findByAuction_User_Id(sellerId);
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }
}
