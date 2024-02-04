package pl.gienius.sknera.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.gienius.sknera.entity.Auction;
import pl.gienius.sknera.entity.Bid;
import pl.gienius.sknera.entity.User;
import pl.gienius.sknera.repository.BidRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BidService {

    private BidRepository bidRepository;

    public BidService(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }

    public void addBid(Bid bid) {
        bidRepository.save(bid);
    }

    public List<Bid> getBidsByAuction(Auction auction) {
        return bidRepository.findByAuction(auction);
    }

    public List<Bid> getBidsByUser(User user) {
        return bidRepository.findByUser(user);
    }

    public List<Bid> getHighestBid(Long auctionId) {
        Pageable pageable = PageRequest.of(0, 1);
        return bidRepository.findHighestBidByAuctionId(auctionId, pageable);
    }

    public List<Bid> getBids() {
        return bidRepository.findAll();
    }

    public Integer countBids() {
        return getBids().size();
    }
}
