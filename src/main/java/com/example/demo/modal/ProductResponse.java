package com.example.demo.modal;

import lombok.Data;

import java.time.LocalDate;

@Data

public class ProductResponse {
    private Long id;
    private String pName;
    private String pCode;
    private String cName;
    private double price;
    private LocalDate expDate;
}
