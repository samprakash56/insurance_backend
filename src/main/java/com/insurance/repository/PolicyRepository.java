package com.insurance.repository;

import com.insurance.entity.Policy;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Repository
public interface PolicyRepository extends JpaRepository<Policy,Long> {

    Page<Policy> findByLob(String lob, Pageable pageable);

    Optional<Policy> findByPolicyNumber(String policyNumber);

}
