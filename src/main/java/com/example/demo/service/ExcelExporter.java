package com.example.demo.service;

import com.example.demo.entity.Product;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelExporter {
    public static byte[] exportToExcel(List<Product> products, String outputFile) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Products");
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Product Name");
            headerRow.createCell(1).setCellValue("Product Code");
            headerRow.createCell(2).setCellValue("Category Name");
            headerRow.createCell(3).setCellValue("Price");
            headerRow.createCell(4).setCellValue("Expire Date");
            int rowNum = 1;
            for (Product product : products) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(product.getName());
                row.createCell(1).setCellValue(product.getCode());
                row.createCell(2).setCellValue(product.getCategory().getName());
                row.createCell(3).setCellValue(product.getPrice());
                row.createCell(4).setCellValue(product.getExpireDate().toString());
            }

            // Lưu workbook vào file
            try (FileOutputStream fileOut = new FileOutputStream(outputFile)) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
