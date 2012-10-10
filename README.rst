======================
JFinal Extensions
======================

Jfinal-ext是对java极速web框架jfinal(https://github.com/jfinal/jfinal)的一个补充。

加强ActionReporter
==================
 
 支持在控制台打印调用Controller方法的具体行数

自动注册route
====================

 扫描classpath下继承了Route的类自动注册

Plugin扩展
====================

自动注册model
-------------
 扫描clsspath和lib中继承了model的类自动注册，可选择不同的命名规则自定映射表名，也可以在每一个model上用注解指定表名

类似ibatis的利用xml管理sql
--------------------------

:: 

    <sqlGroup name="blog" >
	<sql id="findBlog">select * from blog</sql>
	<sql id="findUser">select * from user</sql>
    </sqlGroup>

 SqlManager.sql("blog.findBlog")


jms 消息
----------
 JmsKit.sendQueue("q1", new M(), "a");
 
quartz调度任务
--------------
job.properties中配置任务

#JobA

a.job=test.com.jfinal.plugin.quzrtz.JobA

a.cron=*/5 * * * * ?

a.enable=true

#JobB

b.job=test.com.jfinal.plugin.quartz.JobB

b.cron=*/10 * * * * ?

b.enable=false

cron4j调度任务
--------------
job.properties中配置任务

#JobA

a.job=test.com.jfinal.plugin.cron4j.JobA

a.cron=* * * * *

a.enable=true

#JobB

b.job=test.com.jfinal.plugin.cron4j.JobB

b.cron=* * * * *

b.enable=false

分级配置加载
------------
在团队开发中如果自己有测试配置需要长期存在但是又不需要提交中心库的时候
可以才用分级配置加载的策略。
如中心库中有config.properties这个配置，你可以创建
config-test.properties文件，配置相同的key，ConfigKit中的方法会优先加载
xx-test.properties文件。

视图扩展
=========

dwz支持
--------







