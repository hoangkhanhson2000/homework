package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JasperReportService {

    @Autowired
    private ProductRepository productRepository;

    public byte[] exportProductsToPdf() throws IOException, JRException {
        InputStream inputStream = new ClassPathResource("product_report.jrxml").getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

        List<Map<String, Object>> dataSource = new ArrayList<>();
        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", product.getId());
            data.put("code", product.getCode());
            data.put("name", product.getName());
            data.put("price", product.getPrice());
            data.put("expireDate", product.getExpireDate());
            data.put("categoryCode", product.getCategory().getCode());
            data.put("categoryName", product.getCategory().getName());

            dataSource.add(data);
        }

        Map<String, Object> parameters = null;

        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(dataSource);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanCollectionDataSource);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

        return outputStream.toByteArray();
    }
}

