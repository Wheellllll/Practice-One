##组员

| 姓名      | 学号      | Github账号          |
| :-------: | :-------: | :-----------------: |
| 罗毅      | 1352388   | [blackbbc][1]       |
| 冯夏令    | 1352920   | [WishSummer][2]     |
| 廖山河    | 1352921   | [mirrordust][3]     |
| 彭程      | 1352905   | [KrisCheng][4]      |
| 范亮      | 1352899   | [fanliangandrew][5] |

###依赖
- 配置(Config)管理模块 https://github.com/Wheellllll/ConfigManager
- 性能(Performance)管理模块 https://github.com/Wheellllll/PerformanceManager
- 通行证(License)管理模块 https://github.com/Wheellllll/LicenseManager
- 事件(Event)模块 https://github.com/Wheellllll/EventManager
- 数据库(Database)模块 https://github.com/Wheellllll/Database
- 工具类(Util)模块 https://github.com/Wheellllll/Util

### 启动方式
#### Windows
1. 在目录下打开`CMD`，输入以下命令：
 ```
 java -jar Server.jar
 ```
 server成功启动后， 控制台中输出：`Server is listening at localhost/127.0.0.1:9001`

2. 启动Client: 双击`Client.jar`，图形化界面启动。Client可多次启动，每次启动代表建立一个新的客户端

3. 相关可配置参数请在`server.conf`和`client.conf`中进行修改

#### Linux/Mac OS
1. 启动终端，输入以下命令：
 ```
 java -jar Server.jar
 ```
 server成功启动后， 控制台中输出：`Server is listening at localhost/127.0.0.1:9001`

2. 启动终端，输入以下命令：
 ```
 java -jar Client.jar
 ```
 client成功启动后就会弹出GUI界面，按照指示操作即可

3. 相关可配置参数请在`server.conf`和`client.conf`中进行修改

### 使用方法
1. 用户可以点击界面上的设置按钮来设置服务器的地址和端口号。

2. 用户输入用户名和密码后，可点击登录/注册按钮进行登录或注册，随后进入聊天室界面，登录进入聊天室界面后会自动进入最后一次聊天所在的组，注册进入聊天室界面则会自动进入默认组，组号为1。

3. 在聊天室界面，用户通过在输入框内输入特殊格式的消息`/group {groupid}`来切换到某一个组，在聊天室界面的顶部会显示用户当前所在的组号。

4. 在聊天室界面中，用户在输入框内输入需要发送的消息，点击发送或使用快捷键`Ctrl+Enter`即可发送消息，用户发送的消息只会被转发给同组的用户。


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
 "result":"success",
 "groupid":"{groupid}"
}
{
 "event":"reg",
 "result":"fail",
 "reason":"XXXXXX"
}
```

#### 登陆
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
 "groupid":"{groupid}"
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
"from": senderName,
"message":"XXXXXX"
}
```

#### 改变组号
##### 客户端
##### 服务器
```
{
"event":"group",
"type":operationType,
"groupid":"{groupid}"
}
```


[1]: https://github.com/blackbbc
[2]: https://github.com/WishSummer
[3]: https://github.com/mirrordust
[4]: https://github.com/KrisCheng
[5]: https://github.com/fanliangandrew
