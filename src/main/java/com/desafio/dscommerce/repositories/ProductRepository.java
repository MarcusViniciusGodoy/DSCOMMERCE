package com.desafio.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desafio.dscommerce.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
