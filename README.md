

# 用户中心项目



## 后端技术选型

* java
* spring（依赖注入框架，帮助你管理java对象，集成一些其他内容）
* springmvc（web层框架，提供接口访问，restful风格接口等）
* mybatis（java对数据库进行操作的持久层框架，对jdbc的封装）
* mybatis-plus（对mybatis增强，不用写sql也能实现crud，可以直接在service层实现）
* springboot (帮助我们快速启动一个项目，不用自己管理spring配置，不用自己整合各种框架)
* junit单元测试
* mysql数据库



## 后端项目流程



### 快速初始化一个项目

1. GitHub 搜现成的代码
2. SpringBoot 官方的模板生成器（https://start.spring.io/）
3. 直接在 IDEA 开发工具中生成  ✔

如果要引入 java 的包，可以去 maven 中心仓库寻找（http://mvnrepository.com/）



### 数据库设计

```mysql
create table user
(
    id           bigint auto_increment comment '主键'
        primary key,
    username     varchar(256)                       null comment '姓名',
    userAccount  varchar(256)                       null comment '登陆账号',
    avatarUrl    varchar(1024)                      null comment '账号头像',
    gender       tinyint                            null comment '性别',
    userPassword varchar(256)                       null comment '用户登录 密码',
    phone        varchar(256)                       null comment '手机号码',
    email        varchar(256)                       null comment '邮箱',
    userStatus   int      default 0                 not null comment '用户账号状态',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null comment '修改时间',
    isDelete     tinyint  default 0                 null comment '是否删除',
    userRole     int      default 0                 not null comment '0代表普通用户，1代表管理员'
)
    comment '用户表';
```



### MyBatisX 逆向工程自动生成代码

MyBatisX 插件，自动根据数据库生成：

- domain：实体对象
- mapper：操作数据库的对象
- mapper.xml：定义了 mapper 对象和数据库的关联，可以在里面自己写 SQL
- service：包含常用的增删改查
- serviceImpl：具体实现 service

从而提高开发效率！

![image-20231015151602792](../AppData/Roaming/Typora/typora-user-images/image-20231015151602792.png)



### 注册逻辑设计

1. 用户在前端输入账户和密码、以及校验码（todo）
2. 校验用户的账户、密码、校验密码，是否符合要求
   1. 非空
   2. 账户长度 **不小于** 4 位
   3. 密码就 **不小于** 8 位吧
   4. 账户不能重复
   5. 账户不包含特殊字符
   6. 密码和校验密码相同
3. 对密码进行加密（密码千万不要直接以明文存储到数据库中）
4. 向数据库插入用户数据



### 登录接口

接受参数：用户账户、密码

请求类型：POST 

请求体：JSON 格式的数据

> 请求参数很长时不建议用 get

返回值：用户信息（ **脱敏** ）



```java
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
```



### 登录逻辑

1. 校验用户账户和密码是否合法

   1. 非空
   2. 账户长度不小于 4 位
   3. 密码就不小于 8 位
   4. 
   5. 账户不包含特殊字符

2. 校验密码是否输入正确，要和数据库中的密文密码去对比

3. 用户信息脱敏，隐藏敏感信息，防止数据库中的字段泄露

4. 我们要记录用户的登录态（session），将其存到服务器上（用后端 SpringBoot 框架封装的服务器 tomcat 去记录）

   cookie

5. 返回脱敏后的用户信息



### 删除和查找逻辑

**首先，重中之重的就是对这两个接口的调用者鉴权，只有管理员才有权调用这两个接口**



### 后端优化

#### 设计通用返回对象

目的：给对象补充一些信息，告诉前端这个请求在业务层面是成功还是失败

#### 实现分布式session登录（或者单点登录-redis）

目的：使得Nginx在方向代理时无论是代理到哪台服务器上都能通过session获取用户信息

### 封装全局异常处理类

1. 定义业务异常类

   1. 相对于 java 的异常类，支持更多字段
   2. 自定义构造函数，更灵活 / 快捷的设置字段
2. 编写全局异常处理器（利用 Spring AOP，在调用方法前后进行额外的处理）

### 作用

1. 捕获代码中所有的异常，内部消化，让前端得到更详细的业务报错 / 信息
2. 同时屏蔽掉项目框架本身的异常（不暴露服务器内部状态）
3. 集中处理，比如记录日志





## 项目上线

### 多环境

参考文章：https://blog.csdn.net/weixin_41701290/article/details/120173283

* 本地环境(localhost)：在自己电脑上配置数据库等等自己开发测试运行
* 开发环境(dev，远程开发)：大家连接同一台开发机，联合开发
* 测试环境(test)：开发，测试，产品同学一起完成项目，开发完后测试找Bug，并返回开发完善，最后由产品经理体验后决定是否上线预发布环境
* 预发布环境(体验服)：将项目发布到线上，与十分接近生产环境，俗称“体验服”，和真正的生产环境连接同一个数据库，只不过前端访问的域名不同罢了，为了真实模拟上线后的情况，在上线前查出更多问题
* 生产环境(prod)：正式给用户体验的产品环境，这个环境的代码尽量不要动，防止给用户造成不好的体验

### 后端多环境实现

通常，我们可以通过实现不同的application.yml文件来指定后端项目代码运行的不同环境如：

* application-prod.yml
* application-test.yml
* application-dev.yml

## 项目部署

参考文章：https://www.bilibili.com/read/cv16179200

（本项目用的是第一种原生部署）

### 原生部署

* 准备服务器。首先，你需要一台远程服务器，可以去阿里云，腾讯云，华为云等等云服务器厂商购买，建议安装Centos8.0+系统，不推荐window，因为window系统本身就要占据不小的运行内存

* 准备数据库。在拥有一台服务器之后，你可以在你的服务器本地搭建MySQL数据库，也可以使用云数据库，再可以使用微信云托管的MySQL服务(随关随停，不过，嗯哼，你懂的💸!!!)

* 准备Web服务器。在有了服务器和数据库之后，我们需要有一台Web服务器来将前端的页面展现给客户，常见的服务器有**nginx**、apache、tomcat，这里建议首选nginx，可以自己到官网安装，参考命令：

  ```bash
  curl -o nginx-1.21.6.tar.gz http://nginx.org/download/nginx-1.21.6.tar.gz
  
  tar -zxvf nginx-1.21.6.tar.gz
  
  cd nginx-1.21.6
  
     37  2022-04-17 23:30:09 yum install pcre pcre-devel -y
     39  2022-04-17 23:30:59 yum install openssl openssl-devel -y
     41  2022-04-17 23:31:57 ./configure --with-http_ssl_module --with-http_v2_module --with-stream
     42  2022-04-17 23:32:13 make
     43  2022-04-17 23:32:54 make install
     48  2022-04-17 23:33:40 ls /usr/local/nginx/sbin/nginx
     vim /etc/profile
    在最后一行添加：export PATH=$PATH:/usr/local/nginx/sbin	
    
    nginx
    
    netstat -ntlp 查看启动情况
  ```

* 更改nginx服务器usr/local/nginx.conf配置文件，将其根路径改为前端页面所在的文件路径，这样我们的前端至此就已经准备完毕

* 准备后端环境。下载java、maven。下载java是为了运行之后的jar包文件，而maven是为了将后端项目代码打包成jar包，这里其实也可以不下载maven，直接在IDEA中执行**clean、package**命令，将打包好的jar包上传至服务器即可

  ```bash
  yum install -y java-17-openjdk* // 建议下载JDK11及以上的，有时可能运行jar包文件时会报错版本太低
  ```

* 部署后端。将后端项目打成jar包后，开启一个后台进程，项目即部署完毕

```bash
nohup java -jar ./user-center-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### 宝塔Linux部署

Linux 运维面板

官方安装教程：https://www.bt.cn/new/download.html

方便管理服务器、方便安装软件





### 前端托管

前端腾讯云 web 应用托管（比容器化更傻瓜式，不需要自己写构建应用的命令，就能启动前端项目）

> https://console.cloud.tencent.com/webify/new

- 小缺点：需要将代码放到代码托管平台上
- 优势：不用写命令、代码更新时自动构建





### Docker部署

docker 是容器，可以将项目的环境（比如 java、nginx）和项目的代码一起打包成镜像，所有同学都能下载镜像，更容易分发和移植。

再启动项目时，不需要敲一大堆命令，而是直接下载镜像、启动镜像就可以了。

docker 可以理解为软件安装包。

Docker 安装：https://www.docker.com/get-started/ 或者宝塔安装



Dockerfile 用于指定构建 Docker 镜像的方法

Dockerfile 一般情况下不需要完全从 0 自己写，建议去 github、gitee 等托管平台参考同类项目（比如 springboot）

Dockerfile 编写：

- FROM 依赖的基础镜像
- WORKDIR 工作目录
- COPY 从本机复制文件
- RUN 执行命令
- CMD / ENTRYPOINT（附加额外参数）指定运行容器时默认执行的命令

根据 Dockerfile 构建镜像：

```bash
# 后端
docker build -t user-center-backend:v0.0.1 .

# 前端
docker build -t user-center-front:v0.0.1 .
```

Docker 构建优化：减少尺寸、减少构建时间（比如多阶段构建，可以丢弃之前阶段不需要的内容）

docker run 启动：

```bash
# 前端
docker run -p 80:80 -d user-center-frontend:v0.0.1

# 后端
docker run -p 8080:8080 user-center-backend:v0.0.1
```

虚拟化

1. 端口映射：把本机的资源（实际访问地址）和容器内部的资源（应用启动端口）进行关联
2. 目录映射：把本机的端口和容器应用的端口进行关联



进入容器：

```bash
docker exec -i -t  fee2bbb7c9ee /bin/bash
```



查看进程：

```bash
docker ps 
```



查看日志：

```bash
docker logs -f [container-id]
```



杀死容器：

```bash
docker kill
```



强制删除镜像：

```bash
docker rmi -f
```





### Docker平台部署

1. 云服务商的容器平台（腾讯云、阿里云）
2. 面向某个领域的容器平台（前端 / 后端微信云托管）**要花钱！**



容器平台的好处：

1. 不用输命令来操作，更方便省事
2. 不用在控制台操作，更傻瓜式、更简单
3. 大厂运维，比自己运维更省心
4. 额外的能力，比如监控、告警、其他（存储、负载均衡、自动扩缩容、流水线）



## 跨越问题

这里再来讲讲一个前后端分离项目老生常谈的问题，**跨域！！！**

先来讲讲什么是跨域：

跨域（Cross-origin）指的是在浏览器端进行Web页面的交互时，当一个域（或者协议、端口）的网页请求访问另一个域的资源时，会发生跨域行为。简单来说，跨域是指在浏览器上从一个域名的网页获取另一个域名的资源。

浏览器出于安全的考虑，使用了同源策略（Same-Origin Policy）来防止跨域行为。同源策略要求元素（如JavaScript、CSS、Ajax等）的域名、协议和端口必须相同，否则就会发生跨域。

发生跨域之后浏览器将不会给你返回请求地址的返回结果，因为浏览器认为这是不安全的交互，例如本项目前端部署在8000端口，后端部署在8080端口，当前端给后端发送请求时就会发生跨域，从而导致请求被拦截。

### 解决方法

* 最简单粗暴，直接将前后端都部署到一个台主机的同一个端口上，使用相同的传输协议
* 让服务器告诉浏览器：允许跨域（返回一个响应头，具体叫啥忘了，🤡）
* 网关支持(Nginx)，修改nginx配置文件，新增如下

```nginx
# 跨域配置
location ^~ /api/ {
    proxy_pass http://127.0.0.1:8080/api/;
    add_header 'Access-Control-Allow-Origin' $http_origin;
    add_header 'Access-Control-Allow-Credentials' 'true';
    add_header Access-Control-Allow-Methods 'GET, POST, OPTIONS';
    add_header Access-Control-Allow-Headers '*';
    if ($request_method = 'OPTIONS') {
        add_header 'Access-Control-Allow-Credentials' 'true';
        add_header 'Access-Control-Allow-Origin' $http_origin;
        add_header 'Access-Control-Allow-Methods' 'GET, POST, OPTIONS';
        add_header 'Access-Control-Allow-Headers' 'DNT,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Range';
        add_header 'Access-Control-Max-Age' 1728000;
        add_header 'Content-Type' 'text/plain; charset=utf-8';
        add_header 'Content-Length' 0;
        return 204;
    }
}
```

* 修改后端服务，在Controller层使用@CrossOrigin 注解，具体写法@CrossOrigin(origins = {"你的服务器地址"},allowCredentials = "true")

* 修改后端服务，配置全局请求拦截器。

  ```java
  @Configuration
  public class WebMvcConfg implements WebMvcConfigurer {
   
      @Override
      public void addCorsMappings(CorsRegistry registry) {
          //设置允许跨域的路径
          registry.addMapping("/**")
                  //设置允许跨域请求的域名
                  //当**Credentials为true时，**Origin不能为星号，需为具体的ip地址【如果接口不带cookie,ip无需设成具体ip】
                  .allowedOrigins("http://localhost:9527", "http://127.0.0.1:9527", "http://127.0.0.1:8082", "http://127.0.0.1:8083")
                  //是否允许证书 不再默认开启
                  .allowCredentials(true)
                  //设置允许的方法
                  .allowedMethods("*")
                  //跨域允许时间
                  .maxAge(3600);
      }
  }
  ```

  

# 项目收获

* 了解了Java后端业务分层架构模式
* 对spring，springmvc，mybatis-plus等框架有了更加丰富的实战经验
* 学会了封装全局异常处理类，以及将自定义的业务和系统异常返回给前端用户，优化用户体验
* 了解了一些常见的处理跨域的方法
* 学会了如何使用原生部署，Linux宝塔部署，Docker平台部署等方式上线项目