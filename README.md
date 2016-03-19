### 消息范式

#### 注册
##### 客户端
- reg|{username}|{password}

##### 服务器
- success
- failed|{reason}

####登陆
##### 客户端
- login|{username}|{password}

##### 服务器
- success
- failed|{reason}

#### 发送消息
##### 客户端
- send|{message}

##### 服务器
- success
- failed{reason}
