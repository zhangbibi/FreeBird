# FreeBird  



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


#spring-boot-starter-actuator 实现boot监控
spring-boot-starter-actuator模块中已经实现的一些原生端点。根据端点的作用,以原生端点可分为三大类：
应用配置类：获取应用程序中加载的应用配置、环境变量、自动化配置报告等与Spring Boot应用密切相关的配置类信息。
度量指标类：获取应用程序运行过程中用于监控的度量指标，比如：内存信息、线程池信息、HTTP请求统计等。
操作控制类：提供了对应用的关闭等操作类功能。

应用配置类:
/autoconfig：该端点用来获取应用的自动化配置报告，其中包括所有自动化配置的候选项。
同时还列出了每个候选项自动化配置的各个先决条件是否满足
positiveMatches中返回的是条件匹配成功的自动化配置
negativeMatches中返回的是条件匹配不成功的自动化配置

/beans：该端点用来获取应用上下文中创建的所有Bean

/configprops：该端点用来获取应用中配置的属性信息报告

/env：该端点与/configprops不同，它用来获取应用所有可用的环境属性报告

/mappings：该端点用来返回所有Spring MVC的控制器映射关系报告

/info：该端点用来返回一些应用自定义的信息。默认情况下，该端点只会返回一个空的json内容。
我们可以在application.properties配置文件中通过info前缀来设置一些属性，比如下面这样：
info.app.name=spring-boot-hello
info.app.version=v1.0.0


度量指标类:
/metrics：该端点用来返回当前应用的各类重要度量指标，比如：内存信息、线程信息、垃圾回收信息等。

/health：该端点在一开始的示例中我们已经使用过了，它用来获取应用的各类健康指标信息

/dump：该端点用来暴露程序运行中的线程信息

/trace：该端点用来返回基本的HTTP跟踪信息。


操作控制类:
只提供了一个用来关闭应用的端点：/shutdown。我们可以通过如下配置开启它：
endpoints.shutdown.enabled=true
在配置了上述属性之后，只需要访问该应用的/shutdown端点就能实现关闭该应用的远程操作

