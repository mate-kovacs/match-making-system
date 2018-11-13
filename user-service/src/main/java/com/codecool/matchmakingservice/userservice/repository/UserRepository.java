package com.codecool.matchmakingservice.userservice.repository;

import com.codecool.matchmakingservice.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>{

    User findByEmail(String email);

    List<User> findAllByNameContainingOrderByIdAscNameAsc(String name);

    List<User> findAllByEloBetweenOrderByIdAscEloAsc(int minElo, int maxElo);
}
