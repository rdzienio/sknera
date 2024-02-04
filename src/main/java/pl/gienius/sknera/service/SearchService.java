package pl.gienius.sknera.service;

import org.springframework.stereotype.Service;
import pl.gienius.sknera.entity.Auction;
import pl.gienius.sknera.repository.AuctionRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SearchService {

    private AuctionRepository auctionRepository;

    public SearchService(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    public List<Auction> searchByTitle(String searchQuery) {
        return auctionRepository.findByTitleContainingIgnoreCase(searchQuery);
    }

    public List<Auction> searchByDescription(String searchQuery) {
        return auctionRepository.findByDescriptionContainingIgnoreCase(searchQuery);
    }

    public List<Auction> searchByUser(String searchQuery) {
        return auctionRepository.findByUserUsernameContainingIgnoreCase(searchQuery);
    }

    public List<Auction> searchByDateOlderThan(LocalDate searchDate) {
        LocalDateTime endOfDay = searchDate.atStartOfDay();
        return auctionRepository.findByEndDateBefore(endOfDay);
    }

    public List<Auction> searchByDateNewerThan(LocalDate searchDate) {
        LocalDateTime startOfDay = searchDate.plusDays(1).atStartOfDay();
        return auctionRepository.findByEndDateAfter(startOfDay);
    }
}
