package com.benjsoft.unittestingdemo.repository;

import com.benjsoft.unittestingdemo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
