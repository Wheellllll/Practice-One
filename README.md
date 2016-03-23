###依赖
- sqlite

### 消息范式

#### 
##### 客户端
- reg
message {
 "event":"reg",
 "username":"XXX",
 "password":"XXXXXX"
}

##### 服务器
- reg|success
message {
 "event":"reg",
 "result":"success/fail",
 "message":"XXXXX"
}

####登陆
##### 客户端
- login
 message{
"event":"login",
"username":"XXX",
"password":"XXXXXX"
}

##### 服务器
- login
message{
"event":"login",
"result":"success/fail",
"message":"login failed due to XXX"
}

#### 发送消息

##### 客户端
- send
message{
"event":"send",
"message":"XXXXXXX"
}

##### 服务器
- send|success (给自己)
message{
"event":"send",
"from":"你",
"message":"XXXXX"
}
- forward (从其他客户端来的)
message{
"event":"forward",
"from": senderName
"message":"XXXXXX"
}
- send|{reason}              (发送失败，理由)
message{
"event":"send",
"reason":"XXXXXX"
}
