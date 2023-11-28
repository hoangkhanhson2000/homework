package com.example.demo.service;

import com.example.demo.base.CreatedResponse;
import com.example.demo.base.PageResponse;
import com.example.demo.base.ResponseBase;
import com.example.demo.entity.Product;
import com.example.demo.modal.ProductRequest;
import com.example.demo.modal.ProductResponse;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;


@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity<ResponseBase<PageResponse<List<ProductResponse>>>> getExpiredProducts(Pageable pageable) {
        LocalDate currentDate = LocalDate.now();
        Page<Product> productPage = productRepository.findByExpireDateAfter(currentDate, pageable);
        List<ProductResponse> productResponses = productPage.getContent()
                .stream()
                .map(product -> {
                    ProductResponse response = new ProductResponse();
                    response.setId(product.getId());
                    response.setPName(product.getName());
                    response.setPCode(product.getCode());
                    response.setCName(product.getCategory().getName());
                    response.setPrice(product.getPrice());
                    response.setExpDate(product.getExpireDate());
                    return response;
                })
                .toList();
        PageResponse<List<ProductResponse>> pageResponse = new PageResponse<>();
        pageResponse.setPage(productPage.getNumber());
        pageResponse.setSize(productPage.getSize());
        pageResponse.setTotalElements((int) productPage.getTotalElements());
        pageResponse.setTotalPages(productPage.getTotalPages());
        pageResponse.setData(Collections.singletonList(productResponses));
        return ResponseEntity.ok(new ResponseBase<>(pageResponse));
    }


    public ResponseEntity<ResponseBase<CreatedResponse>> saveProduct(ProductRequest request) {
        Product product = convertToProduct(request);
        productRepository.save(product);
        CreatedResponse createdResponse = new CreatedResponse(product.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseBase<>(createdResponse));
    }


    private void updateProductFields(Product product, ProductRequest request) {
        product.setCode(request.getCode());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setExpireDate(request.getExpireDate());
        product.setCategory(request.getCategory());
    }

    public ResponseEntity<ResponseBase<Object>> updateProduct(Long productId, ProductRequest request) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        updateProductFields(existingProduct, request);
        productRepository.save(existingProduct);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<ResponseBase<Object>> deleteProduct(Long productId) {
        productRepository.deleteById(productId);
        return ResponseEntity.noContent().build();
    }


    public ResponseEntity<ResponseBase<PageResponse<List<ProductResponse>>>> getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductResponse> productResponses = productPage.getContent()
                .stream()
                .map(product -> {
                    ProductResponse response = new ProductResponse();
                    response.setId(product.getId());
                    response.setPName(product.getName());
                    response.setPCode(product.getCode());
                    response.setCName(product.getCategory().getName());
                    response.setPrice(product.getPrice());
                    response.setExpDate(product.getExpireDate());
                    return response;
                })
                .toList();
        PageResponse<List<ProductResponse>> pageResponse = new PageResponse<>();
        pageResponse.setPage(productPage.getNumber());
        pageResponse.setSize(productPage.getSize());
        pageResponse.setTotalElements((int) productPage.getTotalElements());
        pageResponse.setTotalPages(productPage.getTotalPages());
        pageResponse.setData(Collections.singletonList(productResponses));
        return ResponseEntity.ok(new ResponseBase<>(pageResponse));
    }


    private Product convertToProduct(ProductRequest request) {
        Product product = new Product();
        product.setCode(request.getCode());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setExpireDate(request.getExpireDate());
        product.setCategory(request.getCategory());
        return product;
    }
}


