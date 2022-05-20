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
   2. 描述信息`单价` 在4个注解中均出现了，比较冗余，不够优雅，其实根据注解含义可以自动生成校验信息
   3. java/hibernate的校验注解过多，而且很多都有类似的含义，比如`@Size`与`@Length`、`@NotEmpty`与`@NotBlank`
   4. 当参数比较多时，实体显得很长很臃肿
---
3. 鉴于如此，就有必要整合api与校验注解合二为一，并且，自动生成`message`信息
---
4. 实现后的代码如下：
```java
    @Data
    public static class ProductRequest {

        @ApiExtBigDecimal(name = "单价", max = "9999999999.9999", min = "0")
        private BigDecimal price;
        
        // 校验不通过自动生成：单价不能为空，大小范围在[0至9999999999.9999之间

    }
```
---

### 作为框架，需要实现的功能
1. 


### 1. swagger、java/hibernate注解合二为一
* 优化了`@ApiModelProperty`注解有许多属性，大多数是用不上的，所以此工具删除了部分属性，只留下最常用的
* 如果api-ext提供的注解无法满足你的要求，你就使用原生的swagger和校验注解，虽然大部分情况下api-ext是够用的，二选一即可，二者都使用不保证优先级
* 建议DTO的基础类型都使用包装类型，因为包装类型的默认值是`null`
* 根据属性自动生成错误信息，无需用户编写
* 预留扩展位，如果已有的注解和校验器不满足，可以自定义，比如枚举类型
* 对于绝大多数类型，比如字符串，数字类型，强制要去输入最大最小值，防止开发者使用原生注解时候忘记长度大小限制的输入

### 1. `@ApiModelProperty`精简属性的结果


### 版本：
* spring boot: 2+
* springfox: 3.0.0+

### 注解/属性的说明
* 如果`required = true`，那么就意味着不能为空，相当于自带了`@NotNull`或者`@NotEmpty`含义
* 定制化枚举类型（枚举类型需要手动书写，这里根据指定枚举类型自动生成展示信息 `// TODO 暂未开发`）