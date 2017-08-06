# SpringBootTest  学习spring boot

# 配置与打包
application.properties是各环境共用的配置文件
application-dev.properties : 开发环境 
application-test.properties : 测试环境 

通过java -jar 命令行运行时，连续的两个减号--就是对application.properties中的属性值进行赋值的标识。
如: java -jar xxx.jar --server.port=8888命令，等价于我们在application.properties中添加属性server.port=8888

application.properties中默认配置为开发环境:spring.profiles.active=dev
打不同环境包时使用 java -jar xxx.jar --spring.profiles.active=test


#接口文档
通过http://127.0.0.1:8080/sb/swagger-ui.html访问swagger文档

#访问控制 用户/密码 : admin/admin

