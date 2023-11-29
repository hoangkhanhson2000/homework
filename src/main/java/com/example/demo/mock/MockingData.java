package com.example.demo.mock;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.Roles;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

@Component
public class MockingData {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @PostConstruct
    @Transactional
    public void initData() {
        createRoleIfNotFound("ADMIN");
        createRoleIfNotFound("USER");

        createSampleProductAndCategory();
    }

    private void createRoleIfNotFound(String roleName) {
        roleRepository.findByRoleName(roleName)
                .orElseGet(() -> {
                    Roles role = new Roles();
                    role.setRoleName(roleName);
                    return roleRepository.save(role);
                });
    }

    private void createSampleProductAndCategory() {
        if (productRepository.count() == 0 && categoryRepository.count() == 0) {
            Category category1 = new Category();
            category1.setCode("C001");
            category1.setName("Electronics");
            categoryRepository.save(category1);

            Category category2 = new Category();
            category2.setCode("C002");
            category2.setName("Clothing");
            categoryRepository.save(category2);

            Product product1 = new Product();
            product1.setCode("P001");
            product1.setName("Laptop");
            product1.setPrice(1200.0);
            product1.setExpireDate(LocalDate.now().plusMonths(6));
            product1.setCategory(category1);
            productRepository.save(product1);

            Product product2 = new Product();
            product2.setCode("P002");
            product2.setName("T-Shirt");
            product2.setPrice(25.0);
            product2.setExpireDate(LocalDate.now().plusMonths(3));
            product2.setCategory(category2);
            productRepository.save(product2);
        }
    }
}
