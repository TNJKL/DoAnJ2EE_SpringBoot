package com.WebSiteChoiGame.DoAnJ2EE.repository;

import com.WebSiteChoiGame.DoAnJ2EE.entity.CoinTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoinTransactionRepository extends JpaRepository<CoinTransaction, Integer> {
    List<CoinTransaction> findByUser_UserIDOrderByCreatedAtDesc(Integer userID);
    List<CoinTransaction> findByUser_UsernameOrderByCreatedAtDesc(String username);
    
    @Query("SELECT ct FROM CoinTransaction ct WHERE ct.user.username = :username ORDER BY ct.createdAt DESC")
    List<CoinTransaction> findByUsernameOrderByCreatedAtDesc(@Param("username") String username);
    
    List<CoinTransaction> findByTransactionType(String transactionType);
    
    // Kiểm tra duplicate transaction
    @Query("SELECT ct FROM CoinTransaction ct WHERE ct.user.userID = :userID AND ct.transactionType = :transactionType AND ct.amount = :amount AND ct.description = :description AND ct.createdAt >= :createdAt")
    List<CoinTransaction> findByUser_UserIDAndTransactionTypeAndAmountAndDescriptionAndCreatedAtAfter(
        @Param("userID") Integer userID, 
        @Param("transactionType") String transactionType, 
        @Param("amount") Integer amount, 
        @Param("description") String description, 
        @Param("createdAt") java.time.LocalDateTime createdAt);
} 