# 技术点到 Java 基础映射

复习时不要把“框架”和“基础”分开看。真正面试时，面试官经常会从项目切进去，最后追问到 Java 基础。

## 1. 对象模型与 JavaBean

项目落点：

- `sky-pojo` 中大量 `Entity`、`DTO`、`VO`
- `BeanUtils.copyProperties(...)`
- Builder 写法，例如 `Orders.builder()...build()`

你要补牢的基础：

- 什么是 JavaBean，为什么需要无参构造、getter、setter
- `Entity`、`DTO`、`VO` 的职责边界
- 值对象和业务实体的区别
- 对象拷贝的成本、浅拷贝含义、副作用风险

高频追问：

- 为什么不直接把 `Entity` 返回给前端
- `BeanUtils.copyProperties` 的优缺点是什么
- Builder 模式相比 setter 链式调用有什么好处

## 2. 集合、泛型与常用容器

项目落点：

- `List<OrderDetail>`、`List<ShoppingCart>`、`List<DishVO>`
- `Map` 传递动态查询条件
- `Set` 清理 Redis key
- 报表、订单、缓存场景里大量使用集合

你要补牢的基础：

- `ArrayList` 扩容机制、随机访问优势
- `LinkedList` 与 `ArrayList` 的选择
- `HashMap` 底层结构、哈希冲突、扩容
- `Set` 与 `Map` 的关系
- 泛型的作用、类型擦除、通配符基本概念

高频追问：

- 为什么项目里大多数场景用 `List`
- `HashMap` 和 `ConcurrentHashMap` 的区别
- 为什么 `Map` 适合传动态查询条件，但不适合作为复杂业务对象的长期替代

## 3. Stream、Lambda 与函数式风格

项目落点：

- `repetition()` 中把订单明细转成购物车列表
- 报表统计中通过 `stream().map(...).collect(...)` 组装数据
- Top10 销量统计里提取名称列表和销量列表

你要补牢的基础：

- Lambda 的本质
- `map`、`filter`、`collect`、`reduce`
- Stream 和传统 `for` 循环的适用边界
- 可读性和性能之间的取舍

高频追问：

- 什么时候你会选择普通循环而不是 Stream
- `stream().reduce(Integer::sum).get()` 有什么边界风险
- Stream 是否会天然提升性能

## 4. 异常体系

项目落点：

- 自定义业务异常：`OrderBusinessException`、`DeletionNotAllowedException`
- `GlobalExceptionHandler` 统一处理异常
- 业务校验失败时抛出明确异常信息

你要补牢的基础：

- checked exception 和 unchecked exception 的区别
- 为什么业务异常通常继承运行时异常
- 异常传播链路
- 统一异常处理的意义

高频追问：

- 为什么不直接返回错误码而是抛异常
- 事务和异常有什么关系
- 全局异常处理器和局部 `try-catch` 的边界怎么划分

## 5. 注解、反射、AOP

项目落点：

- `@AutoFill`
- `AutoFillAspect`
- `getDeclaredMethod(...)`
- `invoke(...)`

你要补牢的基础：

- 注解是什么，本质能做什么
- 反射的常见 API
- AOP 的核心概念：切点、通知、切面
- Spring AOP 为什么适合做公共逻辑

高频追问：

- 自动填充为什么不放在 service 里做
- 反射的优缺点是什么
- AOP 和拦截器分别适合处理什么问题

## 6. 接口、实现类与面向对象

项目落点：

- `OrderService` / `OrderServiceImpl`
- `DishService` / `DishServiceImpl`

你要补牢的基础：

- 面向接口编程的意义
- 接口与抽象类的区别
- 单一职责和分层思想
- 为什么 controller 不应该直接写复杂业务

高频追问：

- 如果只有一个实现类，为什么还保留接口
- 这种写法对测试和扩展有什么帮助

## 7. 事务、原子性与一致性

项目落点：

- 菜品和口味的新增、修改、删除
- 套餐和套餐菜品关系的新增、修改、删除
- 订单主表、明细表、购物车之间的联动

你要补牢的基础：

- 什么是事务
- ACID 的核心含义
- Spring `@Transactional` 的工作方式
- 什么情况下事务会失效

高频追问：

- 为什么下单要放事务里
- 抛出什么异常会触发回滚
- 一个类内部方法互相调用时事务为什么可能失效

## 8. ThreadLocal 与线程隔离

项目落点：

- `BaseContext`
- JWT 拦截器中写入当前用户 id
- AOP 自动填充读取当前用户 id

你要补牢的基础：

- 线程和请求的关系
- `ThreadLocal` 的基本用法
- 为什么它适合保存请求级上下文
- 为什么用完要 `remove`

高频追问：

- 为什么不把用户 id 层层传参
- `ThreadLocal` 会不会线程不安全
- 在线程池环境下会有什么风险

## 9. 缓存与序列化

项目落点：

- `RedisTemplate`
- 菜品手动缓存与手动失效
- 套餐使用 `@Cacheable` 和 `@CacheEvict`

你要补牢的基础：

- 缓存的价值和典型适用场景
- 缓存命中、失效、更新
- key 设计
- Java 对象写入缓存时的序列化问题

高频追问：

- 先删缓存还是先更新数据库
- 缓存不一致可能怎么产生
- 什么时候适合手动控制缓存，什么时候适合注解缓存

## 10. IO、文件与第三方调用

项目落点：

- POI 导出 Excel
- OSS 上传
- 微信、百度 API 调用
- `HttpClientUtil`

你要补牢的基础：

- 输入输出流
- try-with-resources
- 网络调用的基本异常处理
- 为什么第三方接口调用要考虑超时、重试、幂等

高频追问：

- 导出报表为什么适合使用模板
- 调外部接口失败时系统应该怎么处理

## 11. 定时任务、WebSocket 与并发入门

项目落点：

- `@Scheduled`
- `WebSocketServer`
- 会话集合管理

你要补牢的基础：

- 什么是后台任务
- WebSocket 与 HTTP 轮询的区别
- 共享数据在并发场景中的风险
- 为什么普通 `HashMap` 在并发写入下有风险

高频追问：

- 如果多个商家同时在线，通知怎么发
- WebSocket 会话管理如何保证线程安全

## 12. 一句话总原则

复习时始终把每个知识点回答成三层：

1. 它是什么
2. 它在这个项目里为什么出现
3. 如果继续优化，我会怎么改
