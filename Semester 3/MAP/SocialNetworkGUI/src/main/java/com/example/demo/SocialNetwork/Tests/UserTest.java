package com.example.demo.SocialNetwork.Tests;
import com.example.demo.SocialNetwork.Model.User;
import org.junit.jupiter.api.Assertions;

class UserTest {
    User user=new User("Ana","Popa","ana.popa@yahoo.com","anaPopa.99");

    @org.junit.jupiter.api.Test
    void getFirstName() {
        Assertions.assertEquals("Ana",user.getFirstName());
    }

    @org.junit.jupiter.api.Test
    void getLastName() {
        Assertions.assertEquals("Popa",user.getLastName());
    }

    @org.junit.jupiter.api.Test
    void getEmail() {
        Assertions.assertEquals("ana.popa@yahoo.com",user.getEmail());
    }

    @org.junit.jupiter.api.Test
    void getPassword() {
        Assertions.assertEquals("anaPopa.99",user.getPassword());
    }

    @org.junit.jupiter.api.Test
    void setFirstName() {
        user.setFirstName("Anamaria");
        Assertions.assertEquals("Anamaria",user.getFirstName());
    }

    @org.junit.jupiter.api.Test
    void setLastName() {
        user.setLastName("Zavaliche");
        Assertions.assertEquals("Zavaliche",user.getLastName());
    }

    @org.junit.jupiter.api.Test
    void setEmail() {
        user.setEmail("anamaria.zavaliche@yahoo.com");
        Assertions.assertEquals("anamaria.zavaliche@yahoo.com",user.getEmail());
    }

    @org.junit.jupiter.api.Test
    void setPassword() {
        user.setPassword("anamaria.99");
        Assertions.assertEquals("anamaria.99",user.getPassword());
    }
}