package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.repo.CategoryRepository;
import com.example.demo.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Product> getProductsByExpireDate() {
        LocalDate currentDate = LocalDate.now();
        return productRepository.findByExpireDateAfter(currentDate);
    }

    public void saveOrUpdateProduct(Product product) {
        productRepository.save(product);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void saveOrUpdateCategory(Category category) {
        // Trước khi lưu hoặc cập nhật Category, cập nhật Product trong Category này
        if (category.getId() != null) {
            List<Product> products = productRepository.findByCategoryId(category.getId());
            for (Product product : products) {
                product.setCategory(null);
                productRepository.save(product);
            }
        }

        categoryRepository.save(category);
    }

    public void deleteCategory(Long categoryId) {
        // Trước khi xóa Category, cập nhật Product trong Category này
        List<Product> products = productRepository.findByCategoryId(categoryId);
        for (Product product : products) {
            product.setCategory(null);
            productRepository.save(product);
        }

        categoryRepository.deleteById(categoryId);
    }
}


