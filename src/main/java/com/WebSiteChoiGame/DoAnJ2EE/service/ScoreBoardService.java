package com.WebSiteChoiGame.DoAnJ2EE.service;

import java.util.List;
import java.util.Map;

public interface ScoreBoardService {
    List<Map<String, Object>> getTopPlayers(int limit);
    List<Map<String, Object>> getTopPlayersByGame(Integer gameId, int limit);
} 