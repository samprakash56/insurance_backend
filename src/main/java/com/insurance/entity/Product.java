package com.insurance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "TBL_PRODUCT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_code",
            nullable = false,
            unique = true)
    private String productCode;

    @Column(name = "product_name",
            nullable = false)
    private String productName;

    @Column(nullable = false)
    private Double premium;

}