package com.desafio.dscommerce.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.desafio.dscommerce.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
