package com.igrowing.service;

import com.igrowing.pojo.User;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void test()
    {
        User user = new User();
        user.setUsername("admin");
        user.setUserAccount("manager");
        user.setAvatarUrl("null");
        user.setGender(0);
        user.setUserRole(1);
        user.setUserPassword("98765412");
        user.setPhone("123456");
        user.setEmail("123456");
        user.setUserStatus(0);
        boolean result = userService.save(user);
        System.out.println(user.getId());
        System.out.println(result);
    }

    @Test
    void userRegister()
    {
        String userAccount="testuser";
        String userPassword="";
        String checkPassword="123456";

        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        userAccount="tt";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        userAccount="testuser";
        userPassword="1234";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        userAccount="test user";
        userPassword="12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        userAccount="testuser";
        checkPassword="123456789";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        userAccount="test";
        checkPassword="12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        userAccount="testuser";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertTrue(result>0);


    }

}