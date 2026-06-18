package com.insurance.service;

import com.insurance.dto.*;
import com.insurance.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PolicyService {

    // (/policy/all)
    Page<PolicyResponseDTO> getAllPolicies(
            int page,
            int size,
            String username);

    // (/policy/get/{policyName}
    PolicyResponseDTO getPolicy(String policyNumber);

    // (/policy/updateDetails)
    String updatePolicy(PolicyRequestDTO dto,String username,String policyNumber);

    //(/policy/create)
    String createPolicy(PolicyRequestDTO dto, String username);

    // (/policy/delete/{policyName}
    String deletePolicy(String policyNumber,String username);

    List<ProductResponseDTO> getAllProducts();

}
