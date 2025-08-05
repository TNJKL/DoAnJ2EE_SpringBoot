package com.WebSiteChoiGame.DoAnJ2EE.service;

import com.WebSiteChoiGame.DoAnJ2EE.repository.UserCoinsRepository;
import com.WebSiteChoiGame.DoAnJ2EE.entity.UserCoins;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class UserCoinsServiceImpl implements UserCoinsService {
    
    @Autowired
    private UserCoinsRepository userCoinsRepository;
    
    @Override
    public List<Map<String, Object>> getTopCoinUsers(int limit) {
        List<UserCoins> topCoinUsers = userCoinsRepository.findTopByOrderByCoinAmountDesc(PageRequest.of(0, limit));
        
        return topCoinUsers.stream()
            .map(userCoins -> {
                Map<String, Object> user = new HashMap<>();
                user.put("userID", userCoins.getUser().getUserID());
                user.put("username", userCoins.getUser().getUsername());
                user.put("email", userCoins.getUser().getEmail());
                user.put("coinAmount", userCoins.getCoinAmount());
                return user;
            })
            .collect(Collectors.toList());
    }
} 