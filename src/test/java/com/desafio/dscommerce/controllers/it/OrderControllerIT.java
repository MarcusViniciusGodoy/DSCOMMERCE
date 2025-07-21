package com.desafio.dscommerce.controllers.it;

import com.desafio.dscommerce.DTO.OrderDTO;
import com.desafio.dscommerce.DTO.ProductDTO;
import com.desafio.dscommerce.entities.*;
import com.desafio.dscommerce.tests.ProductFactory;
import com.desafio.dscommerce.tests.TokenUtil;
import com.desafio.dscommerce.tests.UserFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenUtil tokenUtil;

    private String clientUsername, clientPassword, adminUsername, adminPassword, adminOnlyUsername, adminOnlyPassword;
    private String clientToken, adminToken, adminOnlyToken, invalidToken;
    private Long existingOrderId, nonExistingOrderId;

    private Order order;
    private OrderDTO orderDTO;
    private User user;

    @BeforeEach
    void setUp() throws Exception {

        clientUsername = "maria@gmail.com";
        clientPassword = "123456";
        adminUsername = "alex@gmail.com";
        adminPassword = "123456";
        adminOnlyUsername = "ana@gmail.com";
        adminOnlyPassword = "123456";

        existingOrderId = 1L;
        nonExistingOrderId = 1000L;

        clientToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);
        adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
        adminOnlyToken = tokenUtil.obtainAccessToken(mockMvc, adminOnlyUsername, adminOnlyPassword);
        invalidToken = adminToken + "xpto"; // Simulates a wrong token

        user = UserFactory.createClientUser();
        order = new Order(null, Instant.now(), OrderStatus.WAITING_PAYMENT, user, null);

        Product product = ProductFactory.createProduct();
        OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
        order.getItems().add(orderItem);

        orderDTO = new OrderDTO(order);
    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsAndAdminLogged() throws Exception {

        ResultActions result =
                mockMvc.perform(get("/orders/{id}", existingOrderId)
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(1L));
        result.andExpect(jsonPath("$.moment").value(Instant.now()));
        result.andExpect(jsonPath("$.status").value("PAID"));
        result.andExpect(jsonPath("$.client").exists());
        result.andExpect(jsonPath("$.payment").exists());
        result.andExpect(jsonPath("$.items").exists());
        result.andExpect(jsonPath("$.total").exists());
    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsAndClientLogged() throws Exception {

        ResultActions result =
                mockMvc.perform(get("/orders/{id}", existingOrderId)
                        .header("Authorization", "Bearer " + clientToken)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(1L));
        result.andExpect(jsonPath("$.moment").value(Instant.now()));
        result.andExpect(jsonPath("$.status").value("PAID"));
        result.andExpect(jsonPath("$.client").exists());
        result.andExpect(jsonPath("$.payment").exists());
        result.andExpect(jsonPath("$.items").exists());
        result.andExpect(jsonPath("$.total").exists());
    }

    @Test
    public void findByIdShouldReturnForbiddenWhenIdExistsAndClientLogged() throws Exception {

        Long otherOrderId = 2L;

        ResultActions result =
                mockMvc.perform(get("/orders/{id}", otherOrderId)
                        .header("Authorization", "Bearer " + clientToken)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isForbidden());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExistAndAdminLogged() throws Exception {

        ResultActions result =
                mockMvc.perform(get("/orders/{id}", nonExistingOrderId)
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExistAndClientLogged() throws Exception {

        ResultActions result =
                mockMvc.perform(get("/orders/{id}", nonExistingOrderId)
                        .header("Authorization", "Bearer " + clientToken)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void findByIdShouldReturnUnauthorizedWhenIdExistsAndInvalidToken() throws Exception {

        ResultActions result =
                mockMvc.perform(get("/orders/{id}", existingOrderId)
                        .header("Authorization", "Bearer " + invalidToken)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void insertShouldReturnOrderDTOCreatedWhenClientLogged() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(orderDTO);

        ResultActions result =
                mockMvc.perform(post("/orders")
                                .header("Authorization", "Bearer " + clientToken)
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").value(4L));
        result.andExpect(jsonPath("$.moment").exists());
        result.andExpect(jsonPath("$.status").value("WAITING_PAYMENT"));
        result.andExpect(jsonPath("$.client").exists());
        result.andExpect(jsonPath("$.items").exists());
        result.andExpect(jsonPath("$.total").exists());
    }

    @Test
    public void insertShouldReturnUnprocessableEntityWhenClientLoggedAndOrderHasNoItem() throws Exception {

        orderDTO.getItems().clear();

        String jsonBody = objectMapper.writeValueAsString(orderDTO);

        ResultActions result =
                mockMvc.perform(post("/orders")
                                .header("Authorization", "Bearer " + clientToken)
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void insertShouldReturnForbiddenWhenAdminLogged() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(orderDTO);

        ResultActions result =
                mockMvc.perform(post("/orders")
                        .header("Authorization", "Bearer " + adminOnlyToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isForbidden());
    }

    @Test
    public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(orderDTO);

        ResultActions result =
                mockMvc.perform(post("/orders")
                        .header("Authorization", "Bearer " + invalidToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnauthorized());
    }
}