# 微服务框架
## 简介
该框架将业务抽象为业务线(BizLine),每个业务线包含三个阶段

初始化阶段（多个）->验证阶段（多个）->处理阶段(一个)

+ 初始化阶段处理器可以做非空等基础校验，并准备数据，不包含业务逻辑
+ 验证阶段处理器可以根据初始化的数据执行业务处理，包含业务逻辑
+ 处理阶段处理器则是通过验证后执行的业务逻辑，包含业务逻辑

在每个阶段都可以嵌套更多的业务线。

不同业务线的处理器可复用，尤其是在业务比较贴近的时候
## 如何使用
1.根据需求编写各阶段处理器<br>
2.创建业务线(作为类的属性),注册已经编写的各阶段处理器,并启用

    BizLine bizLine = BizLine.create()
    bizLine.register(new MyIniter());
    bizLine.register(new MyValidator());
    bizLine.register(new MyValidator2());
    bizLine.register(new MyHandler));
    bizLine.start();

3.组装数据，调用业务线的处理方法完成业务处理

    Data data = Data.create();
    data.put("something", "something");
    bizLine.process(data);

4.获取返回结果（同步时）

    data.get("someResult")

## 特性
1.使用Disruptor实现无锁的高性能的多线程处理<br>
2.初始化阶段和验证阶段采用并行执行，上阶段执行完毕后执行下一阶段<br>
3.处理阶段单线程执行，验证阶段执行完成后才会执行<br>
4.任意阶段处理器可选<br>
5.支持方便的读写分离<br>
6.支持对资源的顺序访问<br>
7.支持五种业务策略
    
     单写同步(初始化器和验证器总数不能超过3个)
     单写异步(初始化器和验证器总数不能超过3个)
     多写同步(默认，初始化器和验证器总数不能超过3个)
     多写异步(初始化器和验证器总数不能超过3个)
     当前线程

## 需要添加的特性
1.添加对spring boot的集成，以增加热加载，可执行包，系统配置，监控，一键部署等功能<br>
2.添加对dubbo的集成，增加分布式服务支持<br>