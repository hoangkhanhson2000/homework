package com.example.demo.service;

import com.example.demo.entity.Product;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class PdfExporter {


    public static ResponseEntity<byte[]> exportToPdf(List<Product> products, String fileName) {
        try {
            // Compile the JRXML file at runtime
            InputStream jrxmlInput = PdfExporter.class.getResourceAsStream("/pdf/products.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlInput);

            // Save the compiled report to a file (optional)
            JRSaver.saveObject(jasperReport, "src/main/resources/pdf/compiled_report.jasper");

            // Set up the data source and parameters
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(products);
            HashMap<String, Object> parameters = new HashMap<>();

            // Fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // Export to PDF
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

            // Set up response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null); // Internal Server Error
        }
    }

}
