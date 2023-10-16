package com.igrowing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.igrowing.common.ErrorCode;
import com.igrowing.exception.ResponseException;
import com.igrowing.pojo.User;
import com.igrowing.service.UserService;
import com.igrowing.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author Lenovo
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2023-10-07 12:36:22
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

    public static final String SALT="igrowing";

    //用户登录态键
    public static final String USER_LOGIN_STATE="userloginstate";

    /**
     *
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //校验
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword))
        {
            throw new ResponseException(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }

        if (userAccount.length()<4)
        {
            throw new ResponseException(ErrorCode.PARAMS_ERROR,"用户名小于过短");
        }

        if (userPassword.length()<8||checkPassword.length()<8)
        {
            throw new ResponseException(ErrorCode.PARAMS_ERROR,"密码过短");
        }



        //账户不能包含特殊字符
        String vaildPattern="[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher= Pattern.compile(vaildPattern).matcher(userAccount);
        if (matcher.find())
        {
            throw new ResponseException(ErrorCode.PARAMS_ERROR,"用户名包含特殊字符");
        }

        //校验账户密码前后是否一致
        if (!userPassword.equals(checkPassword))
        {
            throw new ResponseException(ErrorCode.PARAMS_ERROR,"密码前后不一致");
        }


        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count>0)
        {
            throw new ResponseException(ErrorCode.PARAMS_ERROR,"账户名已存在");
        }

        //对账户密码进行加密
        String handledPassword= DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
        System.out.println(handledPassword);

        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(handledPassword);
        boolean save = this.save(user);
        if (!save)
        {
            throw new ResponseException(ErrorCode.PARAMS_ERROR,"注册失败");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {

        if (StringUtils.isAnyBlank(userAccount,userPassword))
        {
            throw new ResponseException(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }

        if (userAccount.length()<4)
        {
            throw new ResponseException(ErrorCode.PARAMS_ERROR,"用户名小于过短");
        }

        if (userPassword.length()<8)
        {
            throw new ResponseException(ErrorCode.PARAMS_ERROR,"密码过短");
        }


        String vaildPattern="[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher= Pattern.compile(vaildPattern).matcher(userAccount);
        if (matcher.find())
        {
            throw new ResponseException(ErrorCode.PARAMS_ERROR,"用户名包含特殊字符");
        }

        String handledPassword=DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", handledPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user==null)
        {
            log.info("login failed , account cannot match password");
            throw new ResponseException(ErrorCode.NULL_ERROR,"账户名与密码不匹配");
        }


        User safeUser = getSafeUser(user);


        //TODO:这里可以提升为分布式session登录，(假如后端有多台服务器，nginx代理服务器将请求分发给后台服务器后假如第一次给A
        // 服务器，那下次将请求分发给B服务器，这样在B服务器里面实际上并没有保存该session会话)
        request.getSession().setAttribute(USER_LOGIN_STATE, safeUser);

        return safeUser;

    }

    @Override
    public User getSafeUser(User user)
    {
        if (user==null)
        {
            throw new ResponseException(ErrorCode.NULL_ERROR);
        }

        User safeUser = new User();
        safeUser.setId(user.getId());
        safeUser.setUsername(user.getUsername());
        safeUser.setUserAccount(user.getUserAccount());
        safeUser.setAvatarUrl(user.getAvatarUrl());
        safeUser.setGender(user.getGender());
        safeUser.setUserRole(user.getUserRole());
        safeUser.setPhone(user.getPhone());
        safeUser.setEmail(user.getEmail());
        safeUser.setUserStatus(user.getUserStatus());
        safeUser.setCreateTime(user.getCreateTime());
        return safeUser;
    }

    @Override
    public Boolean userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(UserServiceImpl.USER_LOGIN_STATE);
        return true;
    }


}




