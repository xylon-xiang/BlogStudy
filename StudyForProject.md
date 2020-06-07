[toc]

# 功能需求

主页功能：

* 博客汇总，能以列表形式展示文章，并附上作者、发布日期、分类和摘要
* 能以分类形式查看文章
* 能以时间列表方式归档文章
* 通过标签查找相关文章

> 关键字匹配，模糊搜索

* 个人介绍、联系方式
* 更新记录
* 友情链接



后台功能：

* 记录浏览量
* 现有文章管理

> 1. 分页展示文章信息
> 2. 对文章进行再编辑或者删除

* 文章发布管理

> 1. 支持markdown， 支持插入代码、图片等。
> 2. 可选择相关分类标签，可附链接

* 分类管理

> 支持增删改查

* 反馈信息



安装部署要求

* 可用docker方式部署，也可用jar包方式
* 使用spring boot自带方式打包



非功能需求

* 首页响应时间不超过2s
* 文章页不超过3s



# 项目设计

## 总体设计

* 使用的技术和框架

> 1. 项目构建：maven
> 2. web框架：spring boot
> 3. 数据库ORM：Mybatis
> 4. 数据库连接池：Druid
> 5. 分页插件：PageHelper
> 6. 数据库：mySQL
> 7. 缓存：Redis
> 8. 前端模板：Thymeleaf
> 9. 文章展示：Editor.md





## 结构设计 ![image-20200602161442899](/home/legion-xylon/.config/Typora/typora-user-images/image-20200602161442899.png)

* `SpringApplication`	启动类

  ```java
  @SpringBootApplication
  public static void main(String[] args){
      SpringApplication.run(Provjest.class, args);
  }
  ```

  用于启动整个Spring Boot项目





**MVC**

* `model`  实体类

用于处理应用程序的数据逻辑部分

常负责在数据库中存储数据，通常一张表对应一个实体类

个人理解：*对数据库的一个抽象，向数据库中增删改查对象的类型*

* `mapper`  又称DAO层 

存放SQL语句

访问数据库，向数据库发送SQL语句，完成具体的实体类类型的增删改查工作


* `service`  业务逻辑层

完成功能设计

调用mapper/DAO层接口，接受DAO层返回的数据，完成基本功能的设计

* `controller`    Controller层

处理前后端交互部分

从视图读取数据，控制输出

具体是通过调用service层

* `view`  视图

显示部分





* `component`  component层

当不知道应该在DAO、controller、service那一层时使用

他们四个本质上是一样的，都是Bean




* `utils`  工具类

某些常用工具

如xml读取，excel操作，获取spring上下文

*获取时间，得到一个随机数之类的*

* `exception`  统一定义异常

在程序运行过程中，对异常/bug的处理自定义的方法，或遵循或不遵循语言默认的异常处理

* `cofig`  配置类

基本配置

* `intercepter`  拦截器

执行web的拦截，保证网络安全

* `constant` 常量

定义的不会改变的常量

如admin用户名，状态码，域名，ID之类





## 业务设计

### 发布文章流程



```flow
​```flow
st=>start: 开始
edit=>inputoutput: 编辑文章
conditUser=>condition: 检验用户权限
login=>operation: 登录
makearticle=>operation: 生成文章摘要并插入数据库
refresharticle=>operation: 更新缓存文章并页面显示

st->edit->conditUser
conditUser(no)->login
conditUser(yes)->makearticle->refresharticle->

​```
```



### 登录流程

```flow
st=>start: 开始
enternameandpasswd=>inputoutput: 输入用户名和密码
check=>condition: 检验
reenter=>operation: 重新输入
setlifecircle=>operation: 设置session生命周期

st->enternameandpasswd->check
check(no)->reenter->enternameandpasswd
check(yes)->setlifecircle
```





### 用户个人资料修改流程

```flow
st=>start: 开始
judgeexist=>condition:
justpic=>condition: 
setfile=>condition: 
rewrite
```





## 数据设计

### 用户表 user

|      name      |  type   | length | key  | !null | description |
| :------------: | :-----: | :----: | ---- | ----- | ----------- |
|       id       |   int   |   11   | true | true  | 主键，自增  |
|     phone      | varchar |  255   |      |       |             |
|    netname     | varchar |  255   |      |       |             |
|    password    | varchar |  255   |      |       |             |
|     gender     |  char   |   50   |      |       |             |
|      Name      | varchar |  255   |      |       |             |
|    birthday    |  char   |   50   |      |       |             |
|     email      | varchar |  255   |      |       |             |
| personalBrief  | varchar |  255   |      |       |             |
|  avatarImgUrl  | varchar |  255   |      |       |             |
| recentlyLanded | varchar |  255   |      |       |             |



### 文章表 article

| name              | type    | length | key  | !null | description |
| ----------------- | ------- | ------ | ---- | ----- | ----------- |
| id                | long    |        |      |       | 主键，自增  |
| articleId         | varchar |        |      |       |             |
| author            |         |        |      |       |             |
| originalAuthor    |         |        |      |       |             |
| articleTitle      |         |        |      |       |             |
| articleContent    |         |        |      |       |             |
| atrticleTags      |         |        |      |       |             |
| articleType       |         |        |      |       |             |
| articleCategories |         |        |      |       |             |
| publishData       |         |        |      |       |             |
| updateData        |         |        |      |       |             |
| articleUrl        |         |        |      |       |             |
| articleTabloid    |         |        |      |       |             |
| likes             |         |        |      |       |             |
| lastArticleId     |         |        |      |       |             |
| nextArticleId     |         |        |      |       |             |



### 评论记录表 comment_record









# 开发流程

## 数据库CRUD 

* controller层中编写前端接口，接受前端参数
* service层中编写所需业务接口，供controller曾调用
* 实现service层中的接口，并注入mapper层中的SQL接口
* 采用Mybatis的JavaConfig方式编写SQL语句。
> 没有使用Mybatis的逆向功能，手写所有SQL语句

* 事务的实现，在启动类中开启事务，并在service层需要实现业务的接口上使用`@Transactional`注解
* 复杂之处在于业务逻辑



## 页面与展示

* 前后端的交互主要在controller包中，用Thymeleaf渲染页面
* 自定义异常处理，通过重写`WebMvcConfigurerAdapter`实现自动跳转404、403页面

























# 具体代码分析

## model

```java
@Data
/*归档*/
public class Archive{
    private int id;
    
    
    /*归档日期*/
    private String archiveName;
}
```

`@Data`注解是帮助其后的类自动配置get()， set()方法

```java
@NoArgsConstructor
public class ArticleLikesRecord{
    
    
    public ArticleLikesRecord(...){
        this.* = *;
    }
}
```

`@NoArgsConstructor`注解是无参构造函数。即在new创造一个实例的时候无需在类的括号中加入变量。

> 无参： `new ArticleLikesRecord()`
>
> 有参： `new ArticleLikesRecord(id, name, ....)`

此处`@NoArgsConstructor`是代替无参构造函数的代码。因为写了含参构造函数，故编译器不会自动加入无参构造函数。需要手写。`@NoArgsConstructor`的作用就是让编译器加上无参的构造函数。



***在整个model包中，认清model对应是的数据库中的一张表。在设计阶段要把握好需要记录哪些数据，并分好类，使之能与数据库对应。考虑好相关类在new时是否需要加参。***





-----------------------

## mapper

全部是`interface`类型

```java
@Mapper
@Repository
public interface ArchiveMapper{
    
    @Select("select achiveName from archives order bt id desc")
    List<String> findArchives();
    
    @Insert("insert into archives(archivesName) values(#{archiveName})")
    void save(@Param("archiveName") String archiveName);
    
    @Select("select IFNULL(max(id), 0) from archives where archiveName=#{archiveName}")
    int findArchiveNameByArchiveName(@Param("archiveName") String archiveName);
    
}
```

`@Repository`是Spring的注解，用于声明一个Bean。一般的，是DAO层的Bean。手写JDBC（一套与数据库连接的API）

`@Mapper`是Mybatis的注解，与Spring无关。

使用Mybatis有XML文件和`@Mapper`两种方式。`@Mapper`是对xml配置的一种简化。

> Mybatis可以通过配置xml或注解来配置和映射原生信息，将接口和Java的POJOs(Plain Ordinary Java Object 普通Java对象)映射成数据库中的记录。
>
> > 个人理解：Mybatis通过xml或注释使得将POJOs与数据库进行关联。通过映射关系使得对POJOs的操作变成对数据库的操作。

在Spring程序中，MyBatis需要找到对应的`@Mapper`，在编译的时候动态生成代理类，实现数据库的查询功能。

> 个人理解：`@Mapper`是Mybatis的特征量。通过标注`@Mapper`注解，将下面的类与Mybatis的DAO相关联。



通过注解将其后的方法与SQL语句相关联，代表执行真实的SQL语句。

```java
@Insert()
@Update()
@Delete() 
@Select()
（）中的为SQL语句
```

* 增

```mysql
INSERT INTO tableName (column 1, column 2) VALUE (*,*), (*,*)
####//////####
INSERT INTO tableName (column 1, column 2) SELECT column1, column2 FROM tableName WHERE column3=1

```

* 删

```mysql
DELETE FROM tableName WHERE column1=1
```



* 改

```mysql
UPDATE tableName SET column1="s" WHERE column2="y"
```



* 查

```mysql
SELECT columnName FROM tableName

SELECT columnName FROM tableName WHERE column1=1

SELECT columnName FROM tableName ORDER BY column1 asc/desc

asc:	升序
desc	降序
```

-------------





## Service

***Service层为具体的业务层。需要将每个函数的功能厘清***

本项目使用的为service+serviceImpl实现模式。

> service+serviceImpl的模式最初是为了适应不同的数据库，解决移植性问题。将service类拆分为service接口+serviceImpl类实现。

故具体分析Impl内代码即可

```java
@Service
public class ArchiveServiceImpl implements ArchiveService{
    
    @Autowired
    ArchiveMapper archiveMapper;
    @Autowired
    ArticleService articleService;
    
    @Override
    public DataMap findArchiveNameAndArticleNum(){
        //specific codes
    }
    
    
    @Override
    public void addArchiveName(String archiveName){
        //specific codes
    }
}
```

`@Service`注解用于表示以下类为一个service。作用是完成Bean自动配置。

`@Autowired`用于装配Bean。可写在字段上或setter方法上。根据类型，自动的找到对应的Bean进行注入。

> 例如：
>
> ```java
> public class Person{
>     @Autowired
>     private Animal animal = null;
> }
> 
> @Component
> public class Dog implements Animal{
>     public void use{
>         // specific codes
>     }
> }
> ```
>
> 此中Dog被配置为一个Bean。在Person中， `@Autowired`使得Dog实例被自动注入到animal中。
>
> 无需再像原来Java的类间需要通过`new`创建实例。实现了解耦。
>
> 上述代码等价于：
>
> ```java
> public class Dog implements Animal{
>     public void use{
>         //specific codes
>     }
> }
> 
> public class Person{
>     private Animal animal = new Dog();
> }
> ```



```java
@Override
public DataMap findArchiveNameAndArticleNum(){
    List<String> archives = archiveMapper.findArchives();
        JSONArray archivesJsonArray = new JSONArray();
        
        JSONObject archiveJson;
        
        TimeUtil timeUtil = new TimeUtil();
        
        for(String archiveName : archives){
            archiveJson = new JSONObjest();
            archiveJson.put("archiveName", archiveName);
            
            archiveName = timeUtil.timeYearToWhippletree(archiveName);
            
            archiveJson.put("archiveArticleNum", articleService.coutArticleArchiveByArchive(archiveName));
            
            archivesJsonArray.add(archiveJson);
        }
        
        JSONObject returnJson = new JSONObject();
        returnJson.put("result", archivesJsonArray);
        
        return DataMap.success().setData(returnJson);
}
```

```flow
end=>end: sss

archives=>operation: archives(List<String>)




archives->end
```

























## Controller

```java
@RestController
@Slf4j
public class ArchivesControl{
    
    @Autowired
    ArchiveService archiveService;
    @Autowired
    AechiveService archiveService;
    
    @GetMapping(value = "/findArchiveNameAndArticleNum", produces = MediaType.APPLICATION_JSON_UTF*_VALUE)
    public String findArchiveNameAndArticleNum(){
        try{
            DataMap data = archiveService.findArchiveNameAndAriticleNum();
            return JsonResult.build(data).toJSON();
        }catch(Exception e){
            log.error("Find archive name and article num exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }
}
```

