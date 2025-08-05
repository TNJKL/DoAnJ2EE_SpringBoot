package com.WebSiteChoiGame.DoAnJ2EE.controller;

import com.WebSiteChoiGame.DoAnJ2EE.dto.UserCoinsDTO;
import com.WebSiteChoiGame.DoAnJ2EE.dto.CoinTransactionDTO;
import com.WebSiteChoiGame.DoAnJ2EE.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/coins")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserCoinController {
    
    @Autowired
    private CoinService coinService;

    // Lấy thông tin coin của user hiện tại
    @GetMapping("/my-coins")
    public UserCoinsDTO getMyCoins(@RequestParam String username) {
        return coinService.getUserCoinsByUsername(username);
    }

    // Lấy lịch sử giao dịch coin của user hiện tại
    @GetMapping("/my-transactions")
    public List<CoinTransactionDTO> getMyTransactions(@RequestParam String username) {
        return coinService.getUserTransactionsByUsername(username);
    }
} 