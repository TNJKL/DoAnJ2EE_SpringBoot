package com.WebSiteChoiGame.DoAnJ2EE.service;

import com.WebSiteChoiGame.DoAnJ2EE.repository.ScoreBoardRepository;
import com.WebSiteChoiGame.DoAnJ2EE.entity.ScoreBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class ScoreBoardServiceImpl implements ScoreBoardService {
    
    @Autowired
    private ScoreBoardRepository scoreBoardRepository;
    
    @Override
    public List<Map<String, Object>> getTopPlayers(int limit) {
        List<ScoreBoard> leaderboard = scoreBoardRepository.findTopByOrderByScoreDesc(PageRequest.of(0, limit));
        
        return leaderboard.stream()
            .map(score -> {
                Map<String, Object> player = new HashMap<>();
                player.put("userID", score.getUser().getUserID());
                player.put("username", score.getUser().getUsername());
                player.put("email", score.getUser().getEmail());
                player.put("highScore", score.getHighScore());
                return player;
            })
            .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getTopPlayersByGame(Integer gameId, int limit) {
        List<ScoreBoard> gameLeaderboard = scoreBoardRepository.findByGame_GameIDOrderByHighScoreDesc(gameId, PageRequest.of(0, limit));
        
        return gameLeaderboard.stream()
            .map(score -> {
                Map<String, Object> player = new HashMap<>();
                player.put("userID", score.getUser().getUserID());
                player.put("username", score.getUser().getUsername());
                player.put("email", score.getUser().getEmail());
                player.put("highScore", score.getHighScore());
                return player;
            })
            .collect(Collectors.toList());
    }
} 