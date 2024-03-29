package pl.gienius.sknera.service;

import jakarta.persistence.Column;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.gienius.sknera.entity.Auction;
import pl.gienius.sknera.entity.Bid;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class AuctionChecker {

    Logger logger = LoggerFactory.getLogger(AuctionChecker.class);

    private final AuctionService auctionService;
    private final OrderService orderService;
    private EmailService emailService;

    public AuctionChecker(AuctionService auctionService, OrderService orderService, EmailService emailService) {
        this.auctionService = auctionService;
        this.orderService = orderService;
        this.emailService = emailService;
    }

    @Scheduled(fixedRate = 60000) // Uruchamia zadanie co 60 sekund
    @Transactional
    public void checkAuctions() {
        List<Auction> endedAuctions = auctionService.getEndedAuctions(LocalDateTime.now());
        for (Auction auction : endedAuctions) {
            Bid highestBid = auctionService.getHighestBid(auction.getId());
            if (highestBid != null) {
                logger.info("Zakończono aukcję: " + auction.getTitle() + " z ceną " + highestBid.getPrice());
                orderService.createOrderFromBid(auction, highestBid);
                auctionService.markAuctionAsProcessed(auction);
                String content = "<h1>Potwierdzenie zamówienia</h1>" +
                        "<p>Właśnie wygrałeś aukcję: " + auction.getTitle() + "!</p>";
                emailService.sendOrderConfirmationEmail(highestBid.getUser().getEmail(), "[Sknera] Potwierdzenie zamówienia", content);
            }
        }
    }
}
