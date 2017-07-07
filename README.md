# Falsework

中科软项目脚手架主要集成目前流行的各种技术框架,结合在各种项目场景中的最佳实践,提供开箱即用的代码桩,主要包含:

## boot-mybatis-beetl
### 技术栈
+ spring-boot
+ mybatis
+ 通用mapper
+ pagehelper
+ beetl
### 运行项目
+ 创建数据库
+ 执行sql目录下的初始化语句
+ 修改application.yml中的数据库连接信息
+ 执行BootMybatisBeetlApplication类即可
### 打包编译
+ mvn clean package -Dmaven.test.skip=true
### 相关文档
http://mybatis.tk/

## boot-mybatis-plus-beetl
### 技术栈
+ spring-boot
+ mybatis
+ mybatis-plus
+ beetl
### 运行项目
+ 创建数据库
+ 执行sql目录下的初始化语句
+ 修改application.yml中的数据库连接信息
+ 执行BootMybatisPlusBeetlApplication类即可
### 打包编译
+ mvn clean package -Dmaven.test.skip=true
### 相关文档
http://mp.baomidou.com/#/?id=%e7%ae%80%e4%bb%8b
## boot-nutz-beetl
### 技术栈
+ spring-boot
+ nutz
+ beetl
### 运行项目
+ 创建数据库
+ 修改application.yml中的数据库连接信息
+ 执行BootNutzBeetlApplication类即可,启动过程中会自动初始化相关数据
### 打包编译
+ mvn clean package -Dmaven.test.skip=true -U
### 相关文档
+ http://www.nutzam.com
+ https://nutz.cn
## boot-nutz-vue
### 技术栈
+ spring-boot
+ nutz
+ vue
### 运行项目
+ 创建数据库
+ 修改application.yml中的数据库连接信息
+ 执行BootNutzVueApplication类即可,启动过程中会自动初始化相关数据
### 打包编译
+ mvn clean package -Dmaven.test.skip=true -U -Punix/windows
### 相关文档
+ http://www.nutzam.com
+ https://nutz.cn