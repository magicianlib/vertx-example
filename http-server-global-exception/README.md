**Normal Request:**

```shell
$ curl -i --location --request GET 'localhost:8080/index'
HTTP/1.1 200 OK
content-type: application/json
content-length: 25

{"code":0,"message":"ok"}
```

**Server Internal Error Request:**

```shell
$ curl -i --location --request GET 'localhost:8080/exception'
HTTP/1.1 200 OK
content-type: application/json
content-length: 60

{"code":200,"message":"服务器异常: 模拟业务异常"}
```

**Not Found Request:**

```shell
$ curl -i --location --request GET 'localhost:8080/notfount' 
HTTP/1.1 200 OK
content-type: application/json
content-length: 46

{"code":100,"message":"请求地址不存在"}
```