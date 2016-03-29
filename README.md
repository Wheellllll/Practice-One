##组员
| 姓名      | 学号      | Github账号          |
| :-------: | :-------: | :-----------------: |
| 罗毅      | 1352388   | [blackbbc][1]       |
| 冯夏令    | 1352920   | [WishSummer][2]     |
| 廖山河    | 1352921   | [mirrordust][3]     |
| 彭程      | 1352905   | [KrisCheng][4]      |
| 范亮      | 1352899   | [fanliangandrew][5] |

###依赖
- sqlite

### 使用说明
1. 启动Server: 运行`Server.jar`， server成功启动后， 控制台中输出：`Server is listening at localhost/127.0.0.1:9001`

2. 启动Client: 运行`Client.jar`，图形化界面启动。Client可多次启动，每次启动代表建立一个新的客户端。

3. 用户可以点击界面上的设置按钮来设置服务器的地址和端口号。

4. 用户输入用户名和密码后，可点击登录/注册按钮进行登录或注册，随后进入聊天室界面。

5. 在聊天室界面中，用户在输入框内输入需要发送的消息，点击发送后即可发送消息。

 

### 消息范式

#### 注册
##### 客户端
```
{
 "event":"reg",
 "username":"XXX",
 "password":"XXXXXX"
}
```

##### 服务器
```
{
 "event":"reg",
 "result":"success"
}
{
 "event":"reg",
 "result":"fail",
 "reason":"XXXXXX"
}
```

####登陆
##### 客户端
```
{
 "event":"login",
 "username":"XXXXXX",
 "password":"XXXXXX"
}
```

##### 服务器
```
{
 "event":"login",
 "result":"success",
}
{
 "event":"login",
 "result":"fail",
 "reason":"XXXXXX"
}
```

#### 重新登陆
##### 客户端
```
{
"event":"relogin",
"username":"XXXXXX",
"password":"XXXXXX"
}
```

##### 服务器
```
{
"event":"relogin",
"result":"success"
}
{
"event":"relogin",
"result":"fail",
"reason":"XXXXXX"
}
```

#### 发送消息
##### 客户端
```
{
"event":"send",
"message":"XXXXXXX"
}
```

##### 服务器
```
{
"event":"send",
"result":"success"
}
{
"event":"send",
"result":"fail",
"reason":"XXXXXX"
}
{
"event":"forward",
"from": senderName
"message":"XXXXXX"
}
```

[1]: https://github.com/blackbbc
[2]: https://github.com/WishSummer
[3]: https://github.com/mirrordust
[4]: https://github.com/KrisCheng
[5]: https://github.com/fanliangandrew
