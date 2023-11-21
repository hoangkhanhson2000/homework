package com.example.demo.DTO;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProductDTO {

    private String pName;
    private String pCode;
    private String cName;
    private double price;
    private LocalDate expDate;

    public ProductDTO(String pName, String pCode, String cName, double price, LocalDate expDate) {
        this.pName = pName;
        this.pCode = pCode;
        this.cName = cName;
        this.price = price;
        this.expDate = expDate;
    }


    public static ProductDTO fromProduct(Product product) {
        Category category = product.getCategory();
        return new ProductDTO(
                product.getName(),
                product.getCode(),
                category != null ? category.getName() : null,
                product.getPrice(),
                product.getExpireDate()
        );
    }
}
