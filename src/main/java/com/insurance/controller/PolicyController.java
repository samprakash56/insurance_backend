package com.insurance.controller;

import com.insurance.dto.*;
import com.insurance.serviceimpl.AuthServiceImpl;
import com.insurance.serviceimpl.PolicyServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Tag(
        name = "Policy APIs",
        description = "Policy Management")
@RestController
@RequestMapping("/policy")
public class PolicyController {

    private final PolicyServiceImpl policyService;

    public PolicyController(PolicyServiceImpl policyService){
        this.policyService=policyService;
    }

    private static final Logger log =
            LoggerFactory.getLogger(
                    PolicyController.class);

    @Operation(
            summary = "Create Policy")
    @PostMapping("/create")
    public ResponseEntity<String> createPolicy(
            @RequestBody PolicyRequestDTO dto,
            Principal principal) {

        log.info(
                "Create policy request received from user {}",
                principal.getName());

        String response =
                policyService.createPolicy(
                        dto,
                        principal.getName());

        log.info(
                "Policy created successfully by user {}",
                principal.getName());

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get All Policies",description = "Only User LOB policy")
    @GetMapping("/getAll")
    public ResponseEntity<Page<PolicyResponseDTO>>
    getAllPolicies(
            @RequestParam int page,
            @RequestParam int size,
            Principal principal){

        log.info(
                "Fetching policies for user {}",
                principal.getName());

        return ResponseEntity.ok(
                policyService.getAllPolicies(
                        page,
                        size,
                        principal.getName()));
    }


    @Operation(
            summary = "Get Policy By Number")
    @GetMapping("/get/{policyNumber}")
    public ResponseEntity<PolicyResponseDTO>
    getPolicy(
            @PathVariable String policyNumber){

        log.info(
                "Fetching policy {}",
                policyNumber);

        return ResponseEntity.ok(
                policyService.getPolicy(
                        policyNumber));
    }

    @Operation(
            summary = "Delete Policy By Number")
    @DeleteMapping("/delete/{policyNumber}")
    public ResponseEntity<String>
    deleteByPolicyNumber(
            @PathVariable String policyNumber,
            Principal principal){

        log.warn(
                "Delete request received for policy {} by user {}",
                policyNumber,
                principal.getName());

        return ResponseEntity.ok(
                policyService.deletePolicy(
                        policyNumber,
                        principal.getName()));
    }

    @Operation(
            summary = "Update Policy Details")
    @PutMapping("/update/{policyNumber}")
    public ResponseEntity<String>
    updatePolicy(
            @PathVariable String policyNumber,
            Principal principal,
            @RequestBody PolicyRequestDTO dto){

        log.info(
                "Update request received for policy {} by user {}",
                policyNumber,
                principal.getName());

        return ResponseEntity.ok(
                policyService.updatePolicy(
                        dto,
                        principal.getName(),
                        policyNumber));
    }
}

