package com.example.demo.DTO;

import com.example.demo.entity.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {

    private String cName;
    private String cCode;

    public CategoryDTO(String cName, String cCode) {
        this.cName = cName;
        this.cCode = cCode;
    }


    public static CategoryDTO fromCategory(Category category) {
        return new CategoryDTO(category.getName(), category.getCode());
    }
}
