package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.service.ExcelExporter;
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
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/expired")
    public ResponseEntity<List<Product>> getExpiredProducts() {
        List<Product> expiredProducts = productService.getProductsByExpireDate();
        return new ResponseEntity<>(expiredProducts, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addOrUpdateProduct(@RequestBody Product product) {
        productService.saveOrUpdateProduct(product);
        return new ResponseEntity<>("Product saved successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
    }
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportProductsToExcel() {
        List<Product> products = productService.getAllProducts();

        String fileName = "products.xlsx";

        byte[] excelBytes = ExcelExporter.exportToExcel(products, fileName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelBytes);
    }

}
