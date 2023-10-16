package com.igrowing.pojo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:    // 类说明
 *
 * @ClassName: request    // 类名，会自动填充
 * @Author: 周天赐        // 创建者
 * @Email: m18879004515@163.com
 * @Date: 2023/10/7 19:26   // 时间
 * @Version: 1.0     // 版本
 */
@Data
public class UserRegisterRequest implements Serializable {

    private String userAccount;
    private String userPassword;
    private String checkPassword;
}

    
    
