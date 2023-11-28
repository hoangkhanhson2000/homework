package com.example.demo.service;

import com.example.demo.base.CreatedResponse;
import com.example.demo.base.PageResponse;
import com.example.demo.base.ResponseBase;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.modal.CategoryRequest;
import com.example.demo.modal.CategoryResponse;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public ResponseEntity<ResponseBase<PageResponse<List<CategoryResponse>>>> getAllCategories(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        List<CategoryResponse> categoryResponses = categoryPage.getContent()
                .stream()
                .map(category -> {
                    CategoryResponse response = new CategoryResponse();
                    response.setId(category.getId());
                    response.setCName(category.getName());
                    response.setCCode(category.getCode());
                    return response;
                })
                .toList();

        PageResponse<List<CategoryResponse>> pageResponse = new PageResponse<>();
        pageResponse.setPage(categoryPage.getNumber());
        pageResponse.setSize(categoryPage.getSize());
        pageResponse.setTotalElements((int) categoryPage.getTotalElements());
        pageResponse.setTotalPages(categoryPage.getTotalPages());
        pageResponse.setData(Collections.singletonList(categoryResponses));

        return ResponseEntity.ok(new ResponseBase<>(pageResponse));
    }

    public ResponseEntity<ResponseBase<CreatedResponse>> saveCategory(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setCode(request.getCode());

        Category savedCategory = categoryRepository.save(category);
        return ResponseEntity.ok(new ResponseBase<>(new CreatedResponse(savedCategory.getId())));
    }

    public ResponseEntity<ResponseBase<Object>> updateCategory(Long categoryId, CategoryRequest request) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        existingCategory.setName(request.getName());
        existingCategory.setCode(request.getCode());

        updateProductsForCategory(categoryId);

        categoryRepository.save(existingCategory);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<ResponseBase<Object>> deleteCategory(Long categoryId) {
        updateProductsForCategory(categoryId);
        categoryRepository.deleteById(categoryId);
        return ResponseEntity.noContent().build();
    }


    private void updateProductsForCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        for (Product product : products) {
            product.setCategory(null);
            productRepository.save(product);
        }
    }
}
