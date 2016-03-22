###依赖
- sqlite

### 消息范式

#### 注册
##### 客户端
- reg|{username}|{password}

##### 服务器
- reg|success
- reg|{reason}

####登陆
##### 客户端
- login|{username}|{password}

##### 服务器
- login|success
- login|{reason}

#### 发送消息
##### 客户端
- send|{message}

##### 服务器
- send|success               (给自己)
- forward|{from}|{message}   (从其他客户端来的)
- send|{reason}              (发送失败，理由)
