# 苍穹外卖后端 Java 夯实资料

这套资料基于当前仓库实现整理，目标不是重复教程，而是把你已经做出来的功能转化为可复述、可解释、可深挖的 Java 后端能力。

## 使用顺序

1. 先看 [01-项目技术点总览.md](./01-%E9%A1%B9%E7%9B%AE%E6%8A%80%E6%9C%AF%E7%82%B9%E6%80%BB%E8%A7%88.md)，建立项目全景图。
2. 再看 [02-技术点到Java基础映射.md](./02-%E6%8A%80%E6%9C%AF%E7%82%B9%E5%88%B0Java%E5%9F%BA%E7%A1%80%E6%98%A0%E5%B0%84.md)，把框架能力翻译成 Java 基础。
3. 按 [03-四周学习计划.md](./03-%E5%9B%9B%E5%91%A8%E5%AD%A6%E4%B9%A0%E8%AE%A1%E5%88%92.md) 执行 4 周复习。
4. 用 [04-针对性练习题.md](./04-%E9%92%88%E5%AF%B9%E6%80%A7%E7%BB%83%E4%B9%A0%E9%A2%98.md) 做每日练习和周测。
5. 最后用 [05-复盘清单与模拟面试.md](./05-%E5%A4%8D%E7%9B%98%E6%B8%85%E5%8D%95%E4%B8%8E%E6%A8%A1%E6%8B%9F%E9%9D%A2%E8%AF%95.md) 做复盘和口述训练。

## 这套资料覆盖什么

- 项目架构与核心业务链路
- Java 语法、集合、异常、泛型、反射、注解、Stream
- Spring Boot、IOC、AOP、拦截器、事务、MyBatis、Redis
- 并发入门、ThreadLocal、WebSocket、定时任务、JVM 入门
- 面试表达、项目深挖、薄弱点复盘

## 建议节奏

- 工作日每天 60 到 90 分钟
- 每天固定四步：讲解 -> 追问 -> 小练习 -> 复盘
- 每周至少做 1 次项目口述，主题优先选“下单链路”和“认证到数据落库链路”

## 当前仓库中最值得反复看的锚点

- `sky-server/src/main/java/com/sky/service/impl/OrderServiceImpl.java`
- `sky-server/src/main/java/com/sky/aspect/AutoFillAspect.java`
- `sky-server/src/main/java/com/sky/interceptor/JwtTokenAdminInterceptor.java`
- `sky-server/src/main/java/com/sky/interceptor/JwtTokenUserInterceptor.java`
- `sky-server/src/main/java/com/sky/service/impl/DishServiceImpl.java`
- `sky-server/src/main/java/com/sky/service/impl/SetmealServiceImpl.java`
- `sky-server/src/main/java/com/sky/service/impl/ReportServiceImpl.java`
- `sky-server/src/main/java/com/sky/task/OrderTask.java`
- `sky-server/src/main/java/com/sky/websocket/WebSocketServer.java`

## 你要达到的状态

- 能讲清“我做了什么”
- 能解释“为什么这么做”
- 能回答“这样做有什么问题和可改进点”
- 能把项目经验翻译成通用 Java 后端能力
