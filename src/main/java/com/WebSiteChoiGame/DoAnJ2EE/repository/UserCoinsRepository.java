package com.WebSiteChoiGame.DoAnJ2EE.repository;

import com.WebSiteChoiGame.DoAnJ2EE.entity.UserCoins;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCoinsRepository extends JpaRepository<UserCoins, Integer> {
    Optional<UserCoins> findByUser_UserID(Integer userID);
    Optional<UserCoins> findByUser_Username(String username);
    
    @Query("SELECT uc FROM UserCoins uc WHERE uc.user.username = :username")
    Optional<UserCoins> findByUsername(@Param("username") String username);
    
    @Query("SELECT uc FROM UserCoins uc ORDER BY uc.coinAmount DESC")
    List<UserCoins> findTopByOrderByCoinAmountDesc(Pageable pageable);
} 