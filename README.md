# Swagger注解、Java/Hibernate校验注解增强框架

### get start.
```xml
  <dependency>
      <groupId>com.github.dreamroute</groupId>
      <artifactId>api-ext-spring-boot-starter</artifactId>
      <version>${latest.version}</version>
  </dependency>
```

> 强烈建议DTO的基础类型都使用包装类型，因为包装类型的默认值是`null`

### 起步配置：注意（十分重要）
如果你手动配置了Swagger的`Docket`，那么`DocumentationType`的值一定要配置成`OAS_30`，比如
```
 @Bean
 public Docket createRestApi() {
     // 这里一定需要设置成OAS_30，而不能是SWAGGER_2或者SWAGGER_12
     return new Docket(DocumentationType.OAS_30)
             .apiInfo(apiInfo())
             .select()
             .apis(RequestHandlerSelectors.basePackage("com.xxx.xxx.controller"))
             .paths(PathSelectors.regex("(?!/error.*).*"))
             .build();
 }
```
> 对上述说明：SortProperties拦截的是`springfox.documentation.oas.web.WebMvcBasePathAndHostnameTransformationFilter`下的transform方法，而如果上面配置不是OAS_30，swagger返回值过滤器走的是
> springfox.documentation.swagger2.web.WebMvcBasePathAndHostnameTransformationFilter这个过滤器，内部的数据结构完全不一样，会报错

### 0. 动机
1. 我们使用Swagger作为API工具、使用java/Hibernate validator作为请求参数校验时，实体类似这样：
```java
@Data
public class ProductReq {

    @ApiModelProperty(value = "单价", required = true)
    @NotNull(message = "单价不能为空")
    @DecimalMin(value = "0", message = "单价最小0")
    @DecimalMax(value = "9999999999.9999", message = "单价不能超过100亿")
    private BigDecimal price;
    
}
```
---
2. 上述方式的缺点：
   1. 一个单价字段头顶上的注解过多，顶了4个（1个api + 3个校验）
   2. 描述信息`单价` 在4个注解中均出现了，比较冗余，不够优雅，其实根据注解语意可以自动推断校验信息
   3. java/hibernate的校验注解过多，而且很多都有类似的含义，比如`@Size`与`@Length`、`@NotEmpty`与`@NotBlank`
   4. 请求参数必填多余非必填，而swagger的`required`属性默认是`false`，就需要开发人员大多数情况下需要手动指定`required = true`，解决：这里调换一下，默认是`true`
   5. 对于校验属性，基本上都是非必填的，开发人员容易忘记，比如字符串的最大最小长度，数字类型的最大最小值，解决：为了让开发人员不忘记，类似字符串长度、数字类型的最大最小值属性都改为必填
   6. 当参数比较多时，实体显得很长很臃肿
---
3. 鉴于如此，就有必要整合api与校验注解合二为一，并且，自动生成`message`信息
---
4. 实现后的代码如下：
```java
    @Data
    public static class ProductRequest {

        @ApiExtBigDecimal(name = "单价", max = "9999999999.9999", min = "0")
        private BigDecimal price;
        
        // 校验不通过自动生成：单价不能为空，大小范围在[0至9999999999.9999]之间

    }
```
---

### 此框架，需要实现的功能
1. 支持所有数据类型的校验，其中包括基础类型、数组、集合等
2. 自动生成校验不通过时的错误信息，无需用户编写，减少重复轮子
3. 针对不同的数据类型型定义一组校验注解，开发者根据数据类型使用对应的注解
4. 【必填属性】比如字段中文名字`name`，如果是字符串/数字类型，强制填写最大值，防止用户忘记填写
5. 属性排序，由于Swagger的排序字段是选填的，3.0之后手动指定`pisition`也无效，接口排序也是按照字母顺序自然排序的， 解决方案： 
   1. ~~接口顺序按照定义顺序排序；~~
   2. 属性顺序按照定义顺序排序；
6. 【非必填属性】比如数组、集合的最大长度、最小长度，如果填了就进行校验，否则就不进行校验
7. 增强Swagger的枚举显示，显示枚举值和描述，例如`1-有效, 2-无效`，并且将数据类型改成int，让前端人员更清晰
8. 返回对象属性统一使用`ApiExtResp`注解标记，次注解只有`value`和`hidden`2个属性，避免开发人员滥用各种属性
9. 简化`Swagger`和`Validator`校验的注解，去其糟粕取其精华，丢弃不常用注解属性，重新设置默认属性，比如`required = true`
10. 在使用新的框架的同时，要兼容之前的注解方式，可以搭配使用，不能二选一，让用户为难
11. 移除传统的非空注解，比如`@NotNull`、`@NotEmpty`、`@NotEmpty`等，与Swagger的`required`进行合并，当`required = true`时，就代表非空，所有注解都满足这一条件
12. 预留扩展位，当用户需要自定义个性化校验规则时，用户可以很轻松自定义校验注解（使用Spring SPI Provider技术）
13. 不做过多无意义扩展，将改变花在刀刃上面，对于添加在Controller和方法头顶上的`@Api`和`@ApiOperation`使用原生的即可

### 版本：
* spring boot: 2+
* springfox: 3.0.0+

### 支持的数据类型校验
* 字符串（非空校验、长度校验）
* 数字类型（包括`int`、`long`、`BigDecimal`等的非空、最大最小值）
* 数组（非空校验、最大长度、最小长度校验）
* 集合（非空校验、最大长度、最小长度校验）
* 日期（非空校验、判断是否是过去、判断是否是未来）
* 非基础类型的对象（非空校验）
* ...

### 扩展自定义描述信息
1. 每个系统的枚举类型风格都不太一样，往往希望在swagger文档中显示【1-有效；2-无效】这种样式
2. 传统方式是手动在注解上面添加这样的文本，这样子做的坏处比较明显：
   1. 需要手动编写，如果枚举比较多，那么会编写很长一串字符串
   2. 当枚举有变动的时候，容易忘记更新这一串字符串
3. 所以对于枚举类型，通过系统自动生成，就能解决上面2个问题
4. 实现`EnumPlugin`接口，实现`desc`方法，返回一个描述数组即可，可以参考`EnumMarkerPlugin`的实现

### 说明
> 1. 字符串校验：max必填，当required = false时，min不校验，required = true时，min、max都必填
> 2. 数组/集合校验：required = false时，min不校验，只校验max，required = true时，min、max都校验

### 框架实现原理
1. 要实现一个注解既包括swagger又包括validator，那么就需要将这个注解整合进这两个框架
2. 整合swagger
   1. 利用swagger的`AutoConfiguration`优先注入自定义相关配置类`@AutoConfigureBefore(ValidationAutoConfiguration.class)`
   2. 使用自定义`message`串改器（`MessageInterpolator`）修改错误信息返回值， 不使用默认值，而是使用自动化生成机制
3. 整合校验
   1. 自定义大量数据类型校验注解和校验器
   2. 支持自定义注解和校验器扩展

### 举例
```java
    @Data
    @ApiModel("新增对象")
    public static class InsertReq {

        @ApiExtStr(name = "姓名", max = 10, min = 2)
        private String name;

        @ApiExtStr(name = "备注", max = 10, min = 2)
        private String remark;

        @ApiExtInteger(name = "年龄", max = 200, min = 1)
        private Integer age;

        @ApiExtLong(name = "头发根数", max = 100000L, min = 0L)
        private Long num;

        @ApiExtBigDecimal(name = "单价", max = "10.5", min = "0.2", required = false)
        private BigDecimal price;

        @ApiExtObject(name = "状态", required = false)
        private Status status;

        @ApiExtDate(name = "出生日期", phase = Phase.PastOrPresent)
        private Date birthday;

        @Valid
        @ApiExtArray(name = "角色列表")
        private Role[] roles;

        @Valid
        @ApiExtCollection(name = "角色信息")
        private List<Role> roleList;

        @ApiExtArray(name = "KPI列表")
        private Boolean[] kpis;

        @ApiExtCollection(name = "地址")
        private List<String> addrs;

        @ApiExtCollection(name = "手机号码", min = 2, max = 5)
        private Set<String> phones;
    }
```