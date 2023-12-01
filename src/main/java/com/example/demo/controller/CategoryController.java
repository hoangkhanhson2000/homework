package com.example.demo.controller;

import com.example.demo.base.CreatedResponse;
import com.example.demo.base.PageResponse;
import com.example.demo.base.ResponseBase;
import com.example.demo.modal.CategoryRequest;
import com.example.demo.modal.CategoryResponse;
import com.example.demo.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category")
@RequiredArgsConstructor

public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ResponseBase<PageResponse<List<CategoryResponse>>>> getAllCategories(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return categoryService.getAllCategories(pageable);
    }


    @PostMapping
    public ResponseEntity<ResponseBase<CreatedResponse>> addCategory(@RequestBody CategoryRequest request) {
        return categoryService.saveCategory(request);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<ResponseBase<Object>> updateCategory(
            @PathVariable Long categoryId,
            @RequestBody CategoryRequest request) {
        return categoryService.updateCategory(categoryId, request);
    }


    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ResponseBase<Object>> deleteCategory(@PathVariable Long categoryId) {
        return categoryService.deleteCategory(categoryId);
    }
}


