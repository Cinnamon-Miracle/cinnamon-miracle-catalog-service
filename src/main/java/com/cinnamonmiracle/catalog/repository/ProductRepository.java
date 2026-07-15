package com.cinnamonmiracle.catalog.repository;

import com.cinnamonmiracle.catalog.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
