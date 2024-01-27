package pl.gienius.sknera.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.gienius.sknera.entity.Order;
import pl.gienius.sknera.repository.OrderRepository;

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

    public void updateStatus(Long orderId, Order.OrderStatus status) {
        Order order = orderRepository.findOrderById(orderId);
        order.setStatus(status);
        logger.info("Updated order with ID: " + order.getId() + ", to: "+ order.getStatus().toString());
        orderRepository.save(order);
    }
}
