package pl.gienius.sknera.service;

import org.springframework.stereotype.Service;
import pl.gienius.sknera.entity.Auction;
import pl.gienius.sknera.entity.Bid;
import pl.gienius.sknera.entity.User;
import pl.gienius.sknera.repository.BidRepository;

import java.util.List;

@Service
public class BidService {

    private BidRepository bidRepository;

    public BidService(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }

    public void addBid(Bid bid) {
        bidRepository.save(bid);
    }

    public List<Bid> getBidsByAuction(Auction auction){
        return bidRepository.findByAuction(auction);
    }

    public List<Bid> getBidsByUser(User user){
        return bidRepository.findByUser(user);
    }
}
