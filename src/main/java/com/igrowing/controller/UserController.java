package com.igrowing.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.igrowing.common.BaseResponse;
import com.igrowing.common.ErrorCode;
import com.igrowing.common.ResultUtils;
import com.igrowing.exception.ResponseException;
import com.igrowing.pojo.User;
import com.igrowing.pojo.request.UserLoginRequest;
import com.igrowing.pojo.request.UserRegisterRequest;
import com.igrowing.service.UserService;
import com.igrowing.service.impl.UserServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:    // 类说明
 *
 * @ClassName: UserController    // 类名，会自动填充
 * @Author: 周天赐        // 创建者
 * @Email: m18879004515@163.com
 * @Date: 2023/10/7 19:17   // 时间
 * @Version: 1.0     // 版本
 */
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://8.130.34.141"},allowCredentials = "true")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new ResponseException(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new ResponseException(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {


        if (userLoginRequest==null)
        {
            throw new ResponseException(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount,userPassword))
        {
            throw new ResponseException(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }
        User user = userService.userLogin(userAccount,userPassword , request);
        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request)
    {
        if (request==null)
        {
            throw new ResponseException(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }
        Boolean isLogout = userService.userLogout(request);
        return ResultUtils.success(isLogout);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrent(HttpServletRequest request)
    {
        Object userobj = request.getSession().getAttribute(UserServiceImpl.USER_LOGIN_STATE);
        User currentUser = (User) userobj;
        if (currentUser==null)
        {
            throw new ResponseException(ErrorCode.NOT_LOGIN);
        }

        long id = currentUser.getId();
        User user = userService.getById(id);

        User safeUser = userService.getSafeUser(user);

        return ResultUtils.success(safeUser);

    }

    @PostMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username,HttpServletRequest request)
    {
        if (!isAdmin(request))
        {
            throw new ResponseException(ErrorCode.NO_AUTH,"权限不足");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username))
        {
             queryWrapper.like("username", username);
        }

        List<User> list = userService.list(queryWrapper);
        List<User> userList = list.stream().map(user -> userService.getSafeUser(user)).collect(Collectors.toList());
        return ResultUtils.success(userList);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser( @RequestBody User user, HttpServletRequest request)
    {
        if (!isAdmin(request))
        {
            throw new ResponseException(ErrorCode.NO_AUTH,"权限不足");
        }

        if (user.getId()<=0)
        {
            throw new ResponseException(ErrorCode.NULL_ERROR,"请求数据为空");
        }
        boolean b = userService.removeById(user.getId());
        return ResultUtils.success(b);
    }

    private boolean isAdmin(HttpServletRequest request)
    {
        Object userObj = request.getSession().getAttribute(UserServiceImpl.USER_LOGIN_STATE);
        User user =(User) userObj;
        if (user==null||user.getUserRole()!=1)
        {
            return false;
        }
        return true;
    }

}


    
    
