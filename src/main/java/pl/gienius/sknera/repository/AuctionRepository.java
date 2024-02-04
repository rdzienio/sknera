package pl.gienius.sknera.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.gienius.sknera.entity.Auction;
import pl.gienius.sknera.entity.Product;
import pl.gienius.sknera.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {

    public Auction getAuctionById(Long id);

    public List<Auction> findAuctionsByProduct(Product product);

    public List<Auction> findAuctionsByUser(User user);

    // Metoda do pobierania aktywnych aukcji
    @Query("SELECT a FROM Auction a WHERE a.endDate > ?1")
    List<Auction> findActiveAuctions(LocalDateTime now);

    // Metoda do pobierania 10 najnowszych aukcji
    @Query("SELECT a FROM Auction a ORDER BY a.endDate DESC")
    List<Auction> findTop10ByOrderByEndDateDesc(Pageable pageable);

    // Metoda do pobierania aktualnych aukcji dla danej kategorii
    @Query("SELECT a FROM Auction a WHERE a.category.id = :categoryId AND a.endDate > :now")
    List<Auction> findCurrentAuctionsByCategoryId(Long categoryId, LocalDateTime now);

    // Metoda do pobierania aktywnych aukcji dla danego użytkownika
    @Query("SELECT a FROM Auction a WHERE a.user = :user AND a.endDate > :now")
    List<Auction> findActiveAuctionsByUser(User user, LocalDateTime now);

    List<Auction> findByEndDateBeforeAndProcessedFalse(LocalDateTime endDate);

    //wyszukiwarka
    List<Auction> findByTitleContainingIgnoreCase(String title);

    List<Auction> findByUserUsernameContainingIgnoreCase(String username);
    List<Auction> findByDescriptionContainingIgnoreCase(String description);
    List<Auction> findByEndDateBefore(LocalDateTime endDate);
    List<Auction> findByEndDateAfter(LocalDateTime startDate);

    @Query("SELECT SUM(a.actualPrice) FROM Auction a")
    BigDecimal sumActualPrice();

    @Query("SELECT SUM(a.actualPrice) FROM Auction a WHERE a.processed = true")
    BigDecimal sumActualPriceForProcessedAuctions();
}
