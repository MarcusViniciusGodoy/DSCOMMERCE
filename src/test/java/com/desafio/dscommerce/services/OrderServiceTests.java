package com.desafio.dscommerce.services;

import com.desafio.dscommerce.DTO.OrderDTO;
import com.desafio.dscommerce.entities.Order;
import com.desafio.dscommerce.entities.User;
import com.desafio.dscommerce.repositories.OrderRepository;
import com.desafio.dscommerce.tests.OrderFactory;
import com.desafio.dscommerce.tests.UserFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class OrderServiceTests {

    @InjectMocks
    private OrderService service;

    @Mock
    private OrderRepository repository;

    @Mock
    private AuthService authService;

    private Long existingOrderId, nonExistingOrderId;
    private Order order;
    private OrderDTO orderDTO;
    private User admin, client;

    void setUp() throws Exception{
        existingOrderId = 1L;
        nonExistingOrderId = 2L;

        admin = UserFactory.createCustomAdminUser(1L, "Jef");
        client = UserFactory.createCustomClientUser(2L, "Bob");

        order = OrderFactory.createOrder(client);

        orderDTO = new OrderDTO(order);

        Mockito.when(repository.findById(existingOrderId)).thenReturn(Optional.of(order));
        Mockito.when(repository.findById(nonExistingOrderId)).thenReturn(Optional.empty());
    }

}
