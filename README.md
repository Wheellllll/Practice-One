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
