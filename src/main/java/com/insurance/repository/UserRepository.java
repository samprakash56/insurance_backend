package com.insurance.repository;

import com.insurance.entity.UsersEntity;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UsersEntity,Long> {

    Optional<UsersEntity> findByUsername(String username);

}
