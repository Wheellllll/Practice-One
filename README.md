##组员
| 姓名      | 学号      | Github账号          |
| :-------: | :-------: | :-----------------: |
| 罗毅      | 1352388   | [blackbbc][1]       |
| 冯夏令    | 135xxxx   | [WishSummer][2]     |
| 廖山河    | 135xxxx   | [mirrordust][3]     |
| 彭程      | 135xxxx   | [KrisCheng][4]      |
| 范亮      | 135xxxx   | [fanliangandrew][5] |

###依赖
- sqlite

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
message{
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
