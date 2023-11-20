package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products/expired")
    public ResponseEntity<List<Product>> getExpiredProducts() {
        List<Product> expiredProducts = productService.getProductsByExpireDate();
        return new ResponseEntity<>(expiredProducts, HttpStatus.OK);
    }

    @PostMapping("/products")
    public ResponseEntity<String> addOrUpdateProduct(@RequestBody Product product) {
        productService.saveOrUpdateProduct(product);
        return new ResponseEntity<>("Product saved successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        productService.deleteCategory(categoryId);
        return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
    }

//    @GetMapping("/products/export")
//    public ResponseEntity<Resource> exportProductsToExcel() {
//        List<Product> products = productService.getAllProducts();
//        ByteArrayResource resource = ExcelExporter.exportToExcel(products);
//
//        return ResponseEntity.ok()
//            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=products.xlsx")
//            .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
//            .contentLength(resource.contentLength())
//            .body(resource);
//    }
}
