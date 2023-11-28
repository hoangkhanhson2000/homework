package com.example.demo.modal;

import com.example.demo.entity.Category;
import lombok.Data;

import java.time.LocalDate;
@Data
public class ProductRequest {


    private String code;

    private String name;

    private double price = 0;

    private LocalDate expireDate;

    private Category category;
}

