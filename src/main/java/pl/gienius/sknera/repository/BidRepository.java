package pl.gienius.sknera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.gienius.sknera.entity.Auction;
import pl.gienius.sknera.entity.Bid;
import pl.gienius.sknera.entity.User;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

    List<Bid> findByAuction(Auction auction);
    List<Bid> findByUser(User user);


}
