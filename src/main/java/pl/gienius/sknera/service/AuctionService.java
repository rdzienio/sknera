package pl.gienius.sknera.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.gienius.sknera.entity.Auction;
import pl.gienius.sknera.entity.Bid;
import pl.gienius.sknera.entity.User;
import pl.gienius.sknera.repository.AuctionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuctionService {

    Logger logger = LoggerFactory.getLogger(AuctionService.class);
    private AuctionRepository auctionRepository;
    private BidService bidService;

    public AuctionService(AuctionRepository auctionRepository, BidService bidService) {
        this.auctionRepository = auctionRepository;
        this.bidService = bidService;
    }

    @Transactional
    public List<Auction> getAuctions() {
        return auctionRepository.findAll();
    }

    @Transactional
    public List<Auction> getActiveAuctions() {
        LocalDateTime now = LocalDateTime.now();
        return auctionRepository.findActiveAuctions(now);
    }

    @Transactional
    public Auction getAuctionById(Long id) {
        return auctionRepository.getAuctionById(id);
    }


    public void addAuction(Auction auction) {
        logger.info("Adding new auction " + auction.getTitle());
        auctionRepository.saveAndFlush(auction);
    }

    public Auction updateAuction(Auction auction) {
        Auction toUpdate = auctionRepository.getAuctionById(auction.getId());
        toUpdate.setCategory(auction.getCategory());
        toUpdate.setProduct(auction.getProduct());
        toUpdate.setStartingPrice(auction.getStartingPrice());
        toUpdate.setEndDate(auction.getEndDate());
        toUpdate.setImage(auction.getImage());
        toUpdate.setDescription(auction.getDescription());
        toUpdate.setTitle(auction.getTitle());
        auctionRepository.save(toUpdate);
        logger.info("Updated from: " + auction.getTitle() + " to " + toUpdate.getTitle());
        return toUpdate;
    }

    @Transactional
    public List<Auction> getLatestAuctions() {
        return auctionRepository.findTop10ByOrderByEndDateDesc(PageRequest.of(0, 10));
    }

    @Transactional
    public List<Auction> getCurrentAuctionsForCategory(Long categoryId) {
        LocalDateTime now = LocalDateTime.now();
        return auctionRepository.findCurrentAuctionsByCategoryId(categoryId, now);
    }

    @Transactional
    public List<Auction> getActiveAuctionsForUser(User user) {
        LocalDateTime now = LocalDateTime.now();
        return auctionRepository.findActiveAuctionsByUser(user, now);
    }
    public void addBidToAuction(Auction auction, Bid bid){
        auction.getBids().add(bid);
        auction.setActualPrice(bid.getPrice());
        auctionRepository.save(auction);
    }

    public Bid getHighestBid(Long auctionId){
        List<Bid> bidList = bidService.getHighestBid(auctionId);
        if (!bidList.isEmpty()) {
            return bidList.get(0);
            // Wykonaj operacje na najwyższej ofercie
        }
        return null;
    }

    public List<Auction> getEndedAuctions(LocalDateTime date){
        return auctionRepository.findByEndDateBeforeAndProcessedFalse(date);
    }

    public void markAuctionAsProcessed(Auction auction){
        auction.setProcessed(true);
        auctionRepository.save(auction);

    }

    public Integer countActiveAuctions(){
        return getActiveAuctions().size();
    }

    public Integer countEndedAuctions(){
        return getEndedAuctions(LocalDateTime.now()).size();
    }

    public Auction findById(Long id) {
        return auctionRepository.getAuctionById(id);
    }
}
