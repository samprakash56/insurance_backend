package com.insurance.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class ProductResponseDTO {

    private Long id;

    private String productCode;

    private String productName;

    private Double premium;

}
