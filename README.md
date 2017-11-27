# easyweb
项目代码正在逐步完善中……
# 项目介绍
 基于 Spring boot构建的一套快速开发框架； Spring+SpringMVC+Mybatis+通用mapper+shiro敏捷开发系统架构，由于天然支持spring cloud，后续根据项目规模可以快速支持分布式;项目陆续支持配置中心、日志分析、任务和通知等，支持服务治理、监控和追踪，努力为型项目打造一套通用快捷的开发框架
``` lua
easyweb
├── egojit-common -- SSM框架公共模块，一些基础类,包括通用utils和通用spring boot config
├── egojit-web -- 后台统一管理模块，单节点部署的统一入口，其中不包含任何实际代码，实际代码被写在各个模块中（端口：9494）
├── egojit-ui --  thymeleaf模板(端口:9999)  通过ngix代理远程模板提高性能，做到动静的完全分离；前后端开发完全分离
├── egojit-upms -- 用户权限管理系统（user power management System）和 登录管理
|    ├── egojit-upms-dao -- 数据访问层，通过通用mapper单表操作基本不用写代码
|    ├── egojit-upms-model -- 数据库模型
|    ├── egojit-upms-service -- 服务和业务逻辑层
|    ├── egojit-upms-web -- web层，可以单独跑起来进行分布式部署（端口：9393）
|    ├── egojit-upms-sso -- 基于shiro提供认证、授权、统一会话管理
```