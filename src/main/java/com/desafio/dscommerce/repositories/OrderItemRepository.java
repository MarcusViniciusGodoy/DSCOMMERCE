package com.desafio.dscommerce.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.desafio.dscommerce.entities.OrderItem;
import com.desafio.dscommerce.entities.OrderItemPK;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {

}
