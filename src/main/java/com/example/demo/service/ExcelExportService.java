package com.example.demo.service;

import com.example.demo.entity.Product;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelExportService {

    public byte[] exportProductsToExcel(List<Product> products) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Products");
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Code");
            headerRow.createCell(2).setCellValue("Name");
            headerRow.createCell(3).setCellValue("Price");
            headerRow.createCell(4).setCellValue("Expire Date");
            headerRow.createCell(5).setCellValue("Category");
            int rowNum = 1;
            for (Product product : products) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(product.getId());
                row.createCell(1).setCellValue(product.getCode());
                row.createCell(2).setCellValue(product.getName());
                row.createCell(3).setCellValue(product.getPrice());
                row.createCell(4).setCellValue(product.getExpireDate().toString());
                row.createCell(5).setCellValue(product.getCategory().getName());
            }
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        }
    }
}

