package pl.gienius.sknera.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import pl.gienius.sknera.entity.Auction;
import pl.gienius.sknera.entity.Bid;

import java.time.LocalDateTime;
import java.util.List;

public class AuctionChecker {

    Logger logger = LoggerFactory.getLogger(AuctionChecker.class);

    private final AuctionService auctionService;
    private final OrderService orderService;

    public AuctionChecker(AuctionService auctionService, OrderService orderService) {
        this.auctionService = auctionService;
        this.orderService = orderService;
    }

    @Scheduled(fixedRate = 60000) // Uruchamia zadanie co 60 sekund
    public void checkAuctions() {
        List<Auction> endedAuctions = auctionService.getEndedAuctions(LocalDateTime.now());
        for (Auction auction : endedAuctions) {
            Bid highestBid = auctionService.getHighestBid(auction.getId());
            if (highestBid != null) {
                logger.info("Zakończono aukcję: " + auction.getTitle() + " z ceną " + highestBid.getPrice());
                orderService.createOrderFromBid(auction, highestBid);
                auctionService.markAuctionAsProcessed(auction);
            }
        }
    }
}
