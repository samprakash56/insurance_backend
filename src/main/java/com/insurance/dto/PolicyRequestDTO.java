package com.insurance.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.insurance.entity.Product;
import com.insurance.entity.UsersEntity;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class PolicyRequestDTO {

    @NotBlank(message = "First Name Required")
    private String firstName;

    @NotBlank(message = "Last Name Required")
    private String lastName;

    @NotNull(message = "Date of Birth Required")
    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate dateOfBirth;

    @NotNull(message = "Product Id Required")
    private Long productId;

    private String lob;

}