package com.codecool.matchmakingservice.userservice.repository;

import com.codecool.matchmakingservice.userservice.model.Status;
import com.codecool.matchmakingservice.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);

    List<User> findAllByNameContainingOrderByIdAscNameAsc(String name);

    List<User> findAllByEloBetweenOrderByIdAscEloAsc(int minElo, int maxElo);

    List<User> findAllByStatusOrderByIdAscNameAsc(Status status);
}
