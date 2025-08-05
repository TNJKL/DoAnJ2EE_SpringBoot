package com.WebSiteChoiGame.DoAnJ2EE.service;

import com.WebSiteChoiGame.DoAnJ2EE.dto.UserCoinsDTO;
import com.WebSiteChoiGame.DoAnJ2EE.dto.CoinTransactionDTO;
import com.WebSiteChoiGame.DoAnJ2EE.entity.UserCoins;
import com.WebSiteChoiGame.DoAnJ2EE.entity.CoinTransaction;
import com.WebSiteChoiGame.DoAnJ2EE.entity.User;
import com.WebSiteChoiGame.DoAnJ2EE.repository.UserCoinsRepository;
import com.WebSiteChoiGame.DoAnJ2EE.repository.CoinTransactionRepository;
import com.WebSiteChoiGame.DoAnJ2EE.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoinServiceImpl implements CoinService {
    
    @Autowired
    private UserCoinsRepository userCoinsRepository;
    
    @Autowired
    private CoinTransactionRepository coinTransactionRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserCoinsDTO getUserCoinsByUserID(Integer userID) {
        UserCoins userCoins = userCoinsRepository.findByUser_UserID(userID)
                .orElseGet(() -> createDefaultUserCoins(userID));
        return convertToDTO(userCoins);
    }

    @Override
    public UserCoinsDTO getUserCoinsByUsername(String username) {
        UserCoins userCoins = userCoinsRepository.findByUsername(username)
                .orElseGet(() -> createDefaultUserCoinsByUsername(username));
        return convertToDTO(userCoins);
    }

    @Override
    @Transactional
    public UserCoinsDTO updateUserCoins(Integer userID, Integer newAmount, String reason) {
        UserCoins userCoins = userCoinsRepository.findByUser_UserID(userID)
                .orElseGet(() -> createDefaultUserCoins(userID));
        
        Integer oldAmount = userCoins.getCoinAmount();
        Integer difference = newAmount - oldAmount;
        
        userCoins.setCoinAmount(newAmount);
        userCoins = userCoinsRepository.save(userCoins);
        
        // Tạo transaction record
        String transactionType = difference > 0 ? "admin_add" : "admin_subtract";
        createTransaction(userID, transactionType, difference, reason);
        
        return convertToDTO(userCoins);
    }

    @Override
    @Transactional
    public UserCoinsDTO addCoins(Integer userID, Integer amount, String reason) {
        UserCoins userCoins = userCoinsRepository.findByUser_UserID(userID)
                .orElseGet(() -> createDefaultUserCoins(userID));
        
        Integer newAmount = userCoins.getCoinAmount() + amount;
        userCoins.setCoinAmount(newAmount);
        userCoins = userCoinsRepository.save(userCoins);
        
        // Tạo transaction record
        createTransaction(userID, "admin_add", amount, reason);
        
        return convertToDTO(userCoins);
    }

    @Override
    @Transactional
    public UserCoinsDTO subtractCoins(Integer userID, Integer amount, String reason) {
        UserCoins userCoins = userCoinsRepository.findByUser_UserID(userID)
                .orElseGet(() -> createDefaultUserCoins(userID));
        
        if (userCoins.getCoinAmount() < amount) {
            throw new RuntimeException("Không đủ coin để thực hiện giao dịch!");
        }
        
        Integer newAmount = userCoins.getCoinAmount() - amount;
        userCoins.setCoinAmount(newAmount);
        userCoins = userCoinsRepository.save(userCoins);
        
        // Tạo transaction record
        createTransaction(userID, "admin_subtract", -amount, reason);
        
        return convertToDTO(userCoins);
    }

    @Override
    public List<CoinTransactionDTO> getUserTransactions(Integer userID) {
        return coinTransactionRepository.findByUser_UserIDOrderByCreatedAtDesc(userID)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CoinTransactionDTO> getUserTransactionsByUsername(String username) {
        return coinTransactionRepository.findByUsernameOrderByCreatedAtDesc(username)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CoinTransactionDTO createTransaction(Integer userID, String transactionType, Integer amount, String description) {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("User không tồn tại!"));
        
        UserCoins userCoins = userCoinsRepository.findByUser_UserID(userID)
                .orElseGet(() -> createDefaultUserCoins(userID));
        
        // Kiểm tra xem transaction tương tự đã tồn tại trong 5 giây gần đây không
        // Chỉ kiểm tra duplicate cho các transaction không phải finish challenge
        if (!transactionType.equals("win") && !transactionType.equals("lose") && !transactionType.equals("refund")) {
            LocalDateTime fiveSecondsAgo = LocalDateTime.now().minusSeconds(5);
            List<CoinTransaction> recentTransactions = coinTransactionRepository.findByUser_UserIDAndTransactionTypeAndAmountAndDescriptionAndCreatedAtAfter(
                userID, transactionType, amount, description, fiveSecondsAgo);
            
            if (!recentTransactions.isEmpty()) {
                System.out.println("Duplicate transaction detected, skipping: " + description);
                return convertToDTO(recentTransactions.get(0)); // Trả về transaction đã tồn tại
            }
        }
        
        CoinTransaction transaction = new CoinTransaction(user, transactionType, amount, 
                                                      userCoins.getCoinAmount(), description);
        transaction = coinTransactionRepository.save(transaction);
        
        return convertToDTO(transaction);
    }

    @Override
    public List<UserCoinsDTO> getAllUserCoins() {
        return userCoinsRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserCoinsDTO adminUpdateUserCoins(Integer userID, Integer newAmount, String reason) {
        return updateUserCoins(userID, newAmount, reason);
    }

    // Helper methods
    private UserCoins createDefaultUserCoins(Integer userID) {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("User không tồn tại!"));
        
        UserCoins userCoins = new UserCoins(user, 1000);
        return userCoinsRepository.save(userCoins);
    }

    private UserCoins createDefaultUserCoinsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại!"));
        
        UserCoins userCoins = new UserCoins(user, 1000);
        return userCoinsRepository.save(userCoins);
    }

    private UserCoinsDTO convertToDTO(UserCoins userCoins) {
        return new UserCoinsDTO(
            userCoins.getUserID(),
            userCoins.getUser().getUsername(),
            userCoins.getCoinAmount(),
            userCoins.getUpdatedAt()
        );
    }

    private CoinTransactionDTO convertToDTO(CoinTransaction transaction) {
        return new CoinTransactionDTO(
            transaction.getTransactionID(),
            transaction.getUser().getUserID(),
            transaction.getUser().getUsername(),
            transaction.getTransactionType(),
            transaction.getAmount(),
            transaction.getBalanceAfter(),
            transaction.getDescription(),
            transaction.getRelatedChallenge() != null ? transaction.getRelatedChallenge().getChallengeID() : null,
            transaction.getCreatedAt()
        );
    }
} 