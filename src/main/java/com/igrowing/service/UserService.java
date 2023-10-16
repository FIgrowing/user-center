package com.igrowing.service;

import com.igrowing.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

/**
* @author Lenovo
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2023-10-07 12:36:22
*/

public interface UserService extends IService<User> {

    long userRegister(String userAccount , String userPassword , String checkPassword);

    User userLogin(String userAccount , String userPassword , HttpServletRequest request);

    User getSafeUser(User user);

    Boolean userLogout (HttpServletRequest request);
}
