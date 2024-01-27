package pl.gienius.sknera.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

@Entity
@Table(name = "bids")
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private LocalDateTime bidTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id")
    private Auction auction;

    @PrePersist
    public void prePersist() {
        if (bidTime == null) { // Ustaw startDate tylko jeśli nie została już ustawiona
            bidTime = LocalDateTime.now(); // Ustaw aktualną datę i czas jako datę rozpoczęcia
        }

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDateTime getBidTime() {
        return bidTime;
    }

    public void setBidTime(LocalDateTime bidTime) {
        this.bidTime = bidTime;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }
}

