package com.desafio.dscommerce.services;

import com.desafio.dscommerce.entities.User;
import com.desafio.dscommerce.projections.UserDetailsProjection;
import com.desafio.dscommerce.repositories.UserRepository;
import com.desafio.dscommerce.tests.UserDetailsFactory;
import com.desafio.dscommerce.tests.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    private String existingUsername, nonExistingUsername;
    private User user;
    private List<UserDetailsProjection> userDetails;

    @BeforeEach
    void setUp() throws Exception{
        existingUsername = "maria@gmail.com";
        nonExistingUsername = "user@gmail.com";

        user = UserFactory.createCustomClientUser(1L, existingUsername);
        userDetails = UserDetailsFactory.createCustomAdminUser(existingUsername);
    }
}
