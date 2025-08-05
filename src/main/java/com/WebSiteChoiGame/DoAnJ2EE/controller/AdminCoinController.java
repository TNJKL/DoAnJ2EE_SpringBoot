package com.WebSiteChoiGame.DoAnJ2EE.controller;

import com.WebSiteChoiGame.DoAnJ2EE.service.CoinService;
import com.WebSiteChoiGame.DoAnJ2EE.dto.UserCoinsDTO;
import com.WebSiteChoiGame.DoAnJ2EE.dto.CoinTransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/coins")
public class AdminCoinController {

    @Autowired
    private CoinService coinService;

    // Lấy danh sách coin của tất cả user
    @GetMapping("/users")
    public List<UserCoinsDTO> getAllUserCoins() {
        return coinService.getAllUserCoins();
    }

    // Lấy thông tin coin của một user
    @GetMapping("/users/{userId}")
    public UserCoinsDTO getUserCoins(@PathVariable Integer userId) {
        return coinService.getUserCoinsByUserID(userId);
    }

    // Cập nhật coin của user (admin)
    @PutMapping("/users/{userId}")
    public UserCoinsDTO updateUserCoins(@PathVariable Integer userId, @RequestBody Map<String, Object> request) {
        Integer newAmount = (Integer) request.get("coinAmount");
        String reason = (String) request.get("reason");
        if (reason == null) reason = "Admin cập nhật";
        
        return coinService.adminUpdateUserCoins(userId, newAmount, reason);
    }

    // Thêm coin cho user
    @PostMapping("/users/{userId}/add")
    public UserCoinsDTO addUserCoins(@PathVariable Integer userId, @RequestBody Map<String, Object> request) {
        Integer amount = (Integer) request.get("amount");
        String reason = (String) request.get("reason");
        if (reason == null) reason = "Admin thêm coin";
        
        return coinService.addCoins(userId, amount, reason);
    }

    // Trừ coin của user
    @PostMapping("/users/{userId}/subtract")
    public UserCoinsDTO subtractUserCoins(@PathVariable Integer userId, @RequestBody Map<String, Object> request) {
        Integer amount = (Integer) request.get("amount");
        String reason = (String) request.get("reason");
        if (reason == null) reason = "Admin trừ coin";
        
        return coinService.subtractCoins(userId, amount, reason);
    }

    // Lấy lịch sử giao dịch coin của user
    @GetMapping("/users/{userId}/transactions")
    public List<CoinTransactionDTO> getUserTransactions(@PathVariable Integer userId) {
        return coinService.getUserTransactions(userId);
    }
} 