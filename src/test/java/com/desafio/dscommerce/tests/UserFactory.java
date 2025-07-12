package com.desafio.dscommerce.tests;

import com.desafio.dscommerce.entities.Role;
import com.desafio.dscommerce.entities.User;

import java.time.LocalDate;

public class UserFactory {

    public static User createClientUser(){
        User user = new User(1L, "Maria", "maria@gmail.com", "9898989898", LocalDate.parse("2001-07-25"), "$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO");
        user.addRole(new Role(1L, "ROLE_CLIENT"));
        return user;
    }

    public static User createAdminUser(){
        User user = new User(2L, "Alex", "alex@gmail.com", "9898989898", LocalDate.parse("2001-07-25"), "$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO");
        user.addRole(new Role(2L, "ROLE_ADMIN"));
        return user;
    }

    public static User createCustomClientUser(Long id, String username){
        User user = new User(id, "Maria", username, "9898989898", LocalDate.parse("2001-07-25"), "$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO");
        user.addRole(new Role(1L, "ROLE_CLIENT"));
        return user;
    }

    public static User createCustomAdminUser(Long id, String username){
        User user = new User(id, "Alex", username, "9898989898", LocalDate.parse("2001-07-25"), "$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO");
        user.addRole(new Role(2L, "ROLE_ADMIN"));
        return user;
    }
}
