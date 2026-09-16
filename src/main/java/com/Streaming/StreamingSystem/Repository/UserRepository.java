package com.Streaming.StreamingSystem.Repository;

import com.Streaming.StreamingSystem.Model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity>findByUsername(String username);
    Optional<UserEntity>findByEmail(String email);
}
