### 一、Nginx简介

Nginx是一个开源的且高性能、可靠的http中间件、代理服务。

优势：

1. IO多路复用。
2. 轻量级。功能模块化、代码模块化。
3. CPU亲和
4. sendfile

### 二、安装

1. 安装目录讲解

   ```shell
   rpm -ql nginx
   ```

   | 路径                   | 类型       | 作用                                       |
   | ---------------------- | ---------- | ------------------------------------------ |
   | /etc/logrotate.d/nginx | 配置文件   | Nginx日志轮转，用于logrotate服务的日志切割 |
   | **conf01**             | 目录、配置 | Nginx的主配置文件                          |
   | **conf02**             | 配置文件   | cgi、fastcgi配置文件                       |
   | **conf03**             | 配置文件   | 编码转换映射转化文件                       |
   | /etc/nginx/mime.types  | 配置文件   | 设置http协议的Content-type与扩展名对应关系 |
   | **conf04**             | 配置文件   | 用于配置出系统守护进程管理器管理方式       |
   | **conf05**             | 目录       | 模块目录                                   |
   | **conf06**             | 命令       | Nginx的启动管理                            |
   | **conf07**             | 文件、目录 | Nginx的手册和帮助文件                      |
   | /var/cache/nginx       | 目录       | Nginx的缓存目录                            |
   | /var/log/nginx         | 目录       | Nginx的日志目录                            |

   - **conf01**:/etc/nginx、 /etc/nginx/nginx.conf、/etc/nginx/conf.d、 /etc/nginx/conf.d/ default.conf

   - **conf02**:/etc/nginx/fastgi_params、/etc/nginx/uwsgi_params、/etc/nginx/scgi_params

   - **conf03**:/etc/nginx/koi-utf、/etc/nginx/koi-win、/etc/nginx/win-utf

   - **conf04**:/usr/lib/systemd/system/nginx-debug.service、

     ​              /usr/lib/systemd/system/nginx.service、

     ​             /etc/sysconfig/nginx、/etc/sysconfig/nginx-debug 

   - **conf05**：/usr/lib64/nginx/modules、/etc/nginx//mudules

   - **conf06**:/usr/sbin/nginx、/usr/sbin/nginx-debug

   - **conf07**:/usr/share/doc/nginx-1.xx.0、/usr/share/doc/nginx-1.xx.0/COPYRIGHT

     ​	     /usr/share/man/man8/nginx.8.gz

   - 

2. 安装编译参数

   | 编译选项                            | 作用                               |      |
   | ----------------------------------- | ---------------------------------- | ---- |
   | --prefix=/etc/nginx                 | 安装目录或路径                     |      |
   | --http-client-body-temp-path=....等 | Nginx保留的临时性文件              |      |
   | --user=nginx  --group=nginx         | 设定Nginx进程启动的用户和组用户    |      |
   | --with-cc-opt=parameters            | 设置额外的参数将被添加到CFLASS变量 |      |
   | --with-ld-opt=parameters            | 设置附加的参数，链接系统库         |      |
   |                                     |                                    |      |
   |                                     |                                    |      |
   |                                     |                                    |      |

3. 

### 三、启动、关闭、重启

**格式：nginx -s signal**

```shell
#启动
nginx
#关闭
nginx -s stop
nginx -s quit
#重启日志文件
nginx -s reopen
#重启配置文件
nginx -s reload
#杀死进程
ps -aux | grep nginx
kill -s QUIT 进程号
kill 9 进程号  --强制杀死进程
```

注意事项：需要再/etc/profile文件中配置环境变量，否则需要用/usr/local/nginx -s  stop 来操作nginx命令。

三、模块

1）官方模块

  1）http_refer防盗链

 **valid_referers** 

​        **syntax:** *valid_referers [none|blocked|server_names]  ...* 

​	**default:** *none*  

​	**context:** *server, location* 

------

```html
目的：防止资源被盗用
设计思路：区别哪些请求是非正常的用户请求。
基于http_refer防盗链配置模块。
#nginx.conf
valid_referers nono blocked 116.62.103.228;
if (invilid_referer){
	return 403;
}
```



  2) http proxy代理服务

  **语法：proxy_pass** URL;

​	     **default**:

​	     **context**:location、if in location、limit_except

  分为**正向代理**、 **反向代理**。

区别：**代理的对象不一样**。正向代理代理的是对象的客户端。反向代理代理的是服务端。

3）secure_link_module模块

该模块用于计算和检测URL请求中必须的安全标识。

该模块没有默认编译。需要携带编译参数。

语法1：**syntax:** `secure_link expression;` 

​	   **default:**`none `

​	   **context:**`http`, `server`, `location`

语法2：**syntax:** `secure_link_md5 expression`

​	   **default:** `none `

​	   **context:**`http`, `server`, `location`

 语法3：**syntax:** `secure_link_secret word;`

​	   **default:** `none `

​	   **context:**`location`

4) geoip_module模块

**基于IP地址匹配MaxMind GeoIP二进制文件，读取IP所在地域信息。**

作用：1.区别国内外作http访问规则

​	    2.区别国内城市地域做http访问规则

5）HTTPS

```
1.为什么需要https?
答：http不安全。
   传输数据被中间人盗用、信息泄露、数据内容被劫持、篡改、
2.https协议的实现
对传输内容进行加密以及身份验证。
3.没有ca证书，无法验证中间人的手段。
中间人：中间人可以劫持客户端的请求，直接冒充服务端返回客户端公钥，然后客户端继续访问，中间人又劫持请求，把数据直接响应。
4.生成个人的ca证书。
 #生成key
 openssl genrsa -idea -out csq.key 1024
 #生成csr的请求文件
 openssl req -new -key csq.key -out csq.csr
 #csr的有效期(一年)
 openssl  -req -days 365 -x509 -in csq.csr -signkey csq.key -out csq.crt
5.优化
 #激活keepalive长连接
 #设置ssl session缓存的过期时间
 
```

6）LUA

  定义：**是一个简介、轻量、可扩展的脚本语言。**

 优势：充分的结合Nginx的并发处理epoll优势和Lua的轻量实现简单的功能且高并发的场景。

```html
1.安装
	yum install lua
2.运行
    lua
    print("hello word!")
    或者
lua ./test.lua
3.注释
    行：--
    块：--[[块注释--]]
4.变量
    a='adf"'
    a='sdf\123'
    布尔类型只有nil和false是false,数字0，空字符串都是true。
    lua中如果没有特殊说明，全是全局变量。
	lua中无+=和++。
	lua中~=为不等于。
    lua中字符串拼接为“..”
```

**Nginx+lua环境：**

```html
1.luaJIT
2.ngx_devle_kit 和lua-nginx-module
3.重新编译Nginx
```



四、功能简介



