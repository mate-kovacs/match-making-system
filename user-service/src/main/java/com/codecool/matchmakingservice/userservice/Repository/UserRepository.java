package com.codecool.matchmakingservice.userservice.Repository;

import com.codecool.matchmakingservice.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    User findByEmail(String email);

    List<User> findAllByNameOrderByIdAscNameAsc(String name);

    List<User> findAllByEloBetweenOrderByIdAscEloAsc(int minElo, int maxElo);
}
