package com.insurance.serviceimpl;

import com.insurance.dto.PolicyRequestDTO;
import com.insurance.dto.PolicyResponseDTO;
import com.insurance.dto.ProductResponseDTO;
import com.insurance.entity.Policy;
import com.insurance.entity.Product;
import com.insurance.entity.UsersEntity;
import com.insurance.exception.PolicyNotFoundException;
import com.insurance.exception.UserNotFoundException;
import com.insurance.repository.PolicyRepository;
import com.insurance.repository.ProductRepository;
import com.insurance.repository.UserRepository;
import com.insurance.service.PolicyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Service
public class PolicyServiceImpl implements PolicyService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PolicyRepository policyRepository;
    public PolicyServiceImpl(UserRepository userRepository,PolicyRepository policyRepository,ProductRepository productRepository){
        this.productRepository=productRepository;
        this.userRepository =userRepository;
        this.policyRepository=policyRepository;
    }
    private static final Logger log =
            LoggerFactory.getLogger(
                    PolicyServiceImpl.class);


    @Override
    public Page<PolicyResponseDTO> getAllPolicies(int page, int size, String username) {

        log.info("Fetching policies for user {}", username);

        UsersEntity user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User Not Found"));

        log.info("Role={} LOB={}", user.getRole(), user.getLob());

        Page<Policy> policies;

        if ("ADMIN".equals(user.getRole())) {

            log.info("Admin {} requested all policies", username);

            policies = policyRepository.findAll(
                    PageRequest.of(page, size));

        } else {

            log.info("Fetching policies for LOB {}", user.getLob());

            policies = policyRepository.findByLob(
                    user.getLob(),
                    PageRequest.of(page, size));
        }

        List<PolicyResponseDTO> dtoList =
                policies.getContent()
                        .stream()
                        .map(this::mapToDTO)
                        .toList();

        log.info("Returned {} policies", dtoList.size());

        return new PageImpl<>(
                dtoList,
                policies.getPageable(),
                policies.getTotalElements());
    }

    @Override
    public PolicyResponseDTO getPolicy(String policyNumber) {

        log.info(
                "Fetching policy {}",
                policyNumber);

        Policy policy =
                policyRepository
                        .findByPolicyNumber(policyNumber)
                        .orElseThrow(() -> {

                            log.error(
                                    "Policy {} not found",
                                    policyNumber);

                            return new PolicyNotFoundException(
                                    "Invalid Policy Number");
                        });

        log.info(
                "Policy {} fetched successfully",
                policyNumber);

        return mapToDTO(policy);
    }

    @Override
    public String updatePolicy(PolicyRequestDTO dto, String username, String policyNumber) {

        log.info(
                "Update request received for policy {} by user {}",
                policyNumber,
                username);

        UsersEntity user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow(() -> {

                            log.error(
                                    "User {} not found",
                                    username);

                            return new UserNotFoundException(
                                    "User not found");
                        });

        Policy policy =
                policyRepository
                        .findByPolicyNumber(policyNumber)
                        .orElseThrow(() -> {

                            log.error(
                                    "Policy {} not found",
                                    policyNumber);

                            return new PolicyNotFoundException(
                                    "Policy not found");
                        });

        if (!"ADMIN".equals(user.getRole())
                && !user.getLob().equals(policy.getLob())) {

            log.error(
                    "Unauthorized update attempt by {} on policy {}",
                    username,
                    policyNumber);

            throw new RuntimeException(
                    "Unauthorized Access");
        }

        Product product =
                productRepository
                        .findById(dto.getProductId())
                        .orElseThrow(() -> {

                            log.error(
                                    "Product {} not found",
                                    dto.getProductId());

                            return new RuntimeException(
                                    "Product not found");
                        });

        policy.setFirstName(
                dto.getFirstName());

        policy.setLastName(
                dto.getLastName());

        policy.setDateOfBirth(
                dto.getDateOfBirth());

        policy.setProduct(product);

        if ("ADMIN".equals(user.getRole())
                && dto.getLob() != null
                && !dto.getLob().isBlank()) {

            policy.setLob(
                    dto.getLob());

            log.info(
                    "LOB changed to {} by admin {}",
                    dto.getLob(),
                    username);
        }

        policyRepository.save(policy);

        log.info(
                "Policy {} updated successfully",
                policyNumber);

        return "Policy Updated Successfully";
    }

    @Override
    public String createPolicy(PolicyRequestDTO dto, String username) {

        log.info(
                "Policy creation started by user {}",
                username);

        UsersEntity user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow(() -> {

                            log.error(
                                    "User {} not found",
                                    username);

                            return new UserNotFoundException(
                                    "User not found");
                        });

        Product product =
                productRepository
                        .findById(dto.getProductId())
                        .orElseThrow(() -> {

                            log.error(
                                    "Product {} not found",
                                    dto.getProductId());

                            return new RuntimeException(
                                    "Product not found");
                        });

        Policy policy = new Policy();

        policy.setFirstName(
                dto.getFirstName());

        policy.setLastName(
                dto.getLastName());

        policy.setDateOfBirth(
                dto.getDateOfBirth());

        policy.setIssueDate(
                LocalDate.now());

        policy.setUser(user);

        policy.setProduct(product);

        if ("ADMIN".equals(user.getRole())) {

            if (dto.getLob() == null) {

                log.error(
                        "LOB is mandatory for admin policy creation");

                throw new RuntimeException(
                        "LOB required");
            }

            log.info(
                    "Admin assigned LOB {}",
                    dto.getLob());

            policy.setLob(dto.getLob());

        } else {

            log.info(
                    "Assigned LOB {} from user profile",
                    user.getLob());

            policy.setLob(user.getLob());
        }

        policyRepository.save(policy);

        log.info(
                "Policy saved with ID {}",
                policy.getId());

        String policyNumber =
                "1" +
                        String.format(
                                "%08d",
                                policy.getId());

        policy.setPolicyNumber(
                policyNumber);

        policyRepository.save(policy);

        log.info(
                "Policy {} created successfully by {}",
                policyNumber,
                username);

        return "Policy Created Successfully, "
                + policyNumber;
    }

    @Override
    public String deletePolicy(String policyNumber, String username) {

        log.warn(
                "Delete request received for policy {} by {}",
                policyNumber,
                username);

        UsersEntity user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow(() ->
                                new UserNotFoundException(
                                        "User not found"));

        Policy policy =
                policyRepository
                        .findByPolicyNumber(policyNumber)
                        .orElseThrow(() ->
                                new PolicyNotFoundException(
                                        "Policy Not Found"));

        if (user.getLob().equals(policy.getLob())
                || "ADMIN".equals(user.getRole())) {

            policyRepository.delete(policy);

            log.warn(
                    "Policy {} deleted by {}",
                    policyNumber,
                    username);

            return "Policy deleted, "
                    + policyNumber;
        }

        log.error(
                "Unauthorized delete attempt by {} on policy {}",
                username,
                policyNumber);

        throw new RuntimeException(
                "Unauthorized Access");
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        log.info(String.valueOf(products)   );
        List<ProductResponseDTO> response = new ArrayList<>();

        for(Product product :products){
            ProductResponseDTO responseDTO1 =mapToDtoProduct(product);
            response.add(responseDTO1);
        }
        return response;
    }

    public PolicyResponseDTO mapToDTO(Policy policy){
        PolicyResponseDTO policyResponseDTO = new PolicyResponseDTO();
        policyResponseDTO.setPolicyName(policy.getProduct().getProductName());
        policyResponseDTO.setPolicyNumber(policy.getPolicyNumber());
        policyResponseDTO.setIssueDate(policy.getIssueDate());
        policyResponseDTO.setLob(policy.getLob());
        policyResponseDTO.setIssuedBy(policy.getUser().getUsername());
        policyResponseDTO.setPremium(policy.getProduct().getPremium());
        policyResponseDTO.setFullName(policy.getFirstName()+" "+policy.getLastName());
        return  policyResponseDTO;
    }

    public ProductResponseDTO mapToDtoProduct(Product product){
        ProductResponseDTO productResponse = new ProductResponseDTO();
        productResponse.setId(product.getId());
        productResponse.setProductCode(product.getProductCode());
        productResponse.setPremium(product.getPremium());
        productResponse.setProductName(product.getProductName());
        log.info(String.valueOf(productResponse));
        return productResponse;
    }

}

