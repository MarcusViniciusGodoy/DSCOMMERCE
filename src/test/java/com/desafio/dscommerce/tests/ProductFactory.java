package com.desafio.dscommerce.tests;

import com.desafio.dscommerce.entities.Category;
import com.desafio.dscommerce.entities.Product;

public class ProductFactory {

    public static Product createProduct(){
        Category category = CategoryFactory.createCategory();
        Product product = new Product(1L, "Console PlayStation 5", "Descrição", 3999.0, "url");
        product.getCategories().add(category);
        return product;
    }

    public static Product createProduct(String name){
        Product product = createProduct();
        product.setName(name);
        return product;
    }
}
