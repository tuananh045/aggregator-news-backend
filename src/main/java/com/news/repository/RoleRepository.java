package com.news.repository;

import com.news.model.ERole;
import com.news.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
    Optional<Role> findOneByName(ERole name);
    public Boolean existsByName(ERole name);

}
