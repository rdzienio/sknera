package pl.gienius.sknera.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.gienius.sknera.entity.Auction;
import pl.gienius.sknera.entity.Bid;
import pl.gienius.sknera.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

    List<Bid> findByAuction(Auction auction);
    List<Bid> findByUser(User user);
    @Query("SELECT MAX(b.price) FROM Bid b WHERE b.auction.id = :auctionId AND b.user.id = :userId")
    Optional<Double> findHighestBidPriceByAuctionIdAndUserId(Long auctionId, Long userId);

    @Query("SELECT MAX(b.price) FROM Bid b WHERE b.auction.id = :auctionId")
    Optional<Double> findHighestBidPriceByAuctionId(Long auctionId);

    @Query("SELECT b FROM Bid b WHERE b.auction.id = :auctionId ORDER BY b.price DESC")
    List<Bid> findHighestBidByAuctionId(@Param("auctionId") Long auctionId, Pageable pageable);

}
