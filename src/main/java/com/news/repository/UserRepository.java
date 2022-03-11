package com.news.repository;

import com.news.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT entity FROM User entity WHERE entity.email = ?1")
    Optional<User> findByEmail(String email);

    public boolean existsByEmail(String email);

    public User findOneByEmail(String email);
}
