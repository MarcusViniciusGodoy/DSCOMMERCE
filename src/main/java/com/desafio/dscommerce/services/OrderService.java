package com.desafio.dscommerce.services;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desafio.dscommerce.DTO.OrderDTO;
import com.desafio.dscommerce.DTO.OrderItemDTO;
import com.desafio.dscommerce.entities.Order;
import com.desafio.dscommerce.entities.OrderItem;
import com.desafio.dscommerce.entities.OrderStatus;
import com.desafio.dscommerce.entities.Product;
import com.desafio.dscommerce.entities.User;
import com.desafio.dscommerce.repositories.OrderItemRepository;
import com.desafio.dscommerce.repositories.OrderRepository;
import com.desafio.dscommerce.repositories.ProductRepository;
import com.desafio.dscommerce.services.exceptions.ResourceNotFoundException;

import jakarta.validation.Valid;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id){
        Order order = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado."));
        authService.validateSelfOrAdmin(order.getClient().getId());
        return new OrderDTO(order);
    }

    //Salva pedido carrinho
    @Transactional
    public OrderDTO insert(OrderDTO dto) {
        Order order = new Order();
        order.setMoment(Instant.now());
        order.setStatus(OrderStatus.WAITING_PAYMENT);

        User user = userService.authenticated();
        order.setClient(user);

        for (OrderItemDTO itemDTO : dto.getItems()){
            Product product = productRepository.getReferenceById(itemDTO.getProductId());
            OrderItem item = new OrderItem(order, product, itemDTO.getQuantity(), product.getPrice());
            order.getItems().add(item);
        }
        repository.save(order);
        orderItemRepository.saveAll(order.getItems());
        return new OrderDTO(order);
    }
}
