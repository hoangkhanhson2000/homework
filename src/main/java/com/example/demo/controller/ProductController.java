package com.example.demo.controller;

import com.example.demo.base.CreatedResponse;
import com.example.demo.base.PageResponse;
import com.example.demo.base.ResponseBase;
import com.example.demo.entity.Product;
import com.example.demo.modal.ProductRequest;
import com.example.demo.modal.ProductResponse;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ExcelExportService;
import com.example.demo.service.JasperReportService;
import com.example.demo.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ExcelExportService excelExportService;

    @Autowired
    private JasperReportService jasperReportService;


    @GetMapping("/products/expired")
    public ResponseEntity<ResponseBase<PageResponse<List<ProductResponse>>>> getExpiredProducts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return productService.getExpiredProducts(pageable);
    }


    @PostMapping
    public ResponseEntity<ResponseBase<CreatedResponse>> saveProduct(@RequestBody ProductRequest productRequest) {
        return productService.saveProduct(productRequest);
    }

    @GetMapping
    public ResponseEntity<ResponseBase<PageResponse<List<ProductResponse>>>> getAllProducts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.getAllProducts(pageable);
    }


    @PutMapping("/{productId}")
    public ResponseEntity<ResponseBase<Object>> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductRequest productRequest) {
        return productService.updateProduct(productId, productRequest);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ResponseBase<Object>> deleteProduct(@PathVariable Long productId) {
        return productService.deleteProduct(productId);
    }


    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportProductsToExcel() throws IOException {
        List<Product> products = productRepository.findAll();
        byte[] excelContent = excelExportService.exportProductsToExcel(products);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "products.xlsx");

        return new ResponseEntity<>(excelContent, headers, org.springframework.http.HttpStatus.OK);
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportProductsToPdf() throws IOException, JRException {
        byte[] pdfContent = jasperReportService.exportProductsToPdf();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "products.pdf");

        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
    }


}
