package com.desafio.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desafio.dscommerce.DTO.OrderDTO;
import com.desafio.dscommerce.entities.Order;
import com.desafio.dscommerce.repositories.OrderRepository;
import com.desafio.dscommerce.services.exceptions.ResourceNotFoundException;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id){
            Order order = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado."));
            return new OrderDTO(order);
    }
}
