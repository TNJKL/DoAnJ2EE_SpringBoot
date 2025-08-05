package com.WebSiteChoiGame.DoAnJ2EE.service;

import java.util.List;
import java.util.Map;

public interface UserCoinsService {
    List<Map<String, Object>> getTopCoinUsers(int limit);
} 