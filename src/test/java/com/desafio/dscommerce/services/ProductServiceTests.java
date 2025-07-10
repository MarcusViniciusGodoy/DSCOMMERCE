package com.desafio.dscommerce.services;

import com.desafio.dscommerce.DTO.ProductDTO;
import com.desafio.dscommerce.entities.Product;
import com.desafio.dscommerce.repositories.ProductRepository;
import com.desafio.dscommerce.tests.ProductFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    private long existingProductId, nonExistingProductId;
    private String productName;
    private Product product;

    @BeforeEach
    void setUp() throws Exception{
        existingProductId = 1L;
        nonExistingProductId = 2L;

        productName = "PlayStation 5";
        product = ProductFactory.createProduct(productName);

        Mockito.when(repository.findById(existingProductId)).thenReturn(Optional.of(product));
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists(){
        ProductDTO result = service.findById(existingProductId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingProductId);
        Assertions.assertEquals(result.getName(), product.getName());
    }
}
