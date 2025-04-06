package com.desafio.dscommerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.desafio.dscommerce.DTO.CategoryDTO;
import com.desafio.dscommerce.DTO.ProductDTO;
import com.desafio.dscommerce.DTO.ProductMinDTO;
import com.desafio.dscommerce.entities.Category;
import com.desafio.dscommerce.entities.Product;
import com.desafio.dscommerce.repositories.CategoryRepository;
import com.desafio.dscommerce.repositories.ProductRepository;
import com.desafio.dscommerce.services.exceptions.DatabaseException;
import com.desafio.dscommerce.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> result = repository.findAll();
        return result.stream().map(x -> new CategoryDTO(x)).toList();
    }
}
