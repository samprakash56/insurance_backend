package com.insurance.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class PolicyResponseDTO {

    private String policyNumber;

    private String fullName;

    private String policyName;

    private Double premium;

    private LocalDate issueDate;

    private String lob;

    private String issuedBy;

}