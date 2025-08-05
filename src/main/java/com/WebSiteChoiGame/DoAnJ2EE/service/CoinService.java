package com.WebSiteChoiGame.DoAnJ2EE.service;

import com.WebSiteChoiGame.DoAnJ2EE.dto.UserCoinsDTO;
import com.WebSiteChoiGame.DoAnJ2EE.dto.CoinTransactionDTO;
import com.WebSiteChoiGame.DoAnJ2EE.entity.UserCoins;
import com.WebSiteChoiGame.DoAnJ2EE.entity.CoinTransaction;

import java.util.List;

public interface CoinService {
    // UserCoins methods
    UserCoinsDTO getUserCoinsByUserID(Integer userID);
    UserCoinsDTO getUserCoinsByUsername(String username);
    UserCoinsDTO updateUserCoins(Integer userID, Integer newAmount, String reason);
    UserCoinsDTO addCoins(Integer userID, Integer amount, String reason);
    UserCoinsDTO subtractCoins(Integer userID, Integer amount, String reason);
    
    // CoinTransaction methods
    List<CoinTransactionDTO> getUserTransactions(Integer userID);
    List<CoinTransactionDTO> getUserTransactionsByUsername(String username);
    CoinTransactionDTO createTransaction(Integer userID, String transactionType, Integer amount, String description);
    
    // Admin methods
    List<UserCoinsDTO> getAllUserCoins();
    UserCoinsDTO adminUpdateUserCoins(Integer userID, Integer newAmount, String reason);
} 