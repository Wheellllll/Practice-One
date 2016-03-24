###依赖
- sqlite

### 消息范式

#### 注册
##### 客户端
```
reg
message {
 "event":"reg",
 "username":"XXX",
 "password":"XXXXXX"
}
```

##### 服务器
```
reg|success
message {
 "event":"reg",
 "result":"success/fail",
 "reason":"XXXXX"
}
```

####登陆
##### 客户端
```
login
message{
"event":"login",
"username":"XXXXXX",
"password":"XXXXXX"
}
```

##### 服务器
```
login
message{
"event":"login",
"result":"success/fail",
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
message{
"event":"relogin",
"result":"success"
}
message{
"event":"relogin",
"result":"fail",
"reason":"XXXXXX"
}
```

#### 发送消息
##### 客户端
```
send
message{
"event":"send",
"message":"XXXXXXX"
}
```

##### 服务器
```
message{
"event":"send",
"result":"success"
}
message{
"event":"send",
"result":"fail",
"reason":"XXXXXX"
}
message{
"event":"forward",
"from": senderName
"message":"XXXXXX"
}
```
