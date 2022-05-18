### 1. swagger、java/hibernate注解合二为一
* 优化了`@ApiModelProperty`注解有许多属性，大多数是用不上的，所以此工具删除了部分属性，只留下最常用的
* 如果api-ext提供的注解无法满足你的要求，你就使用原生的swagger和校验注解，虽然大部分情况下api-ext是够用的，二选一即可，二者都使用不保证优先级
* 建议DTO的基础类型都使用包装类型，因为包装类型的默认值是`null`
* 根据属性自动生成错误信息，无需用户编写

### 1. `@ApiModelProperty`精简属性的结果


### 版本：
* spring boot: 2+
* springfox: 3.0.0+

### 注解/属性的说明
* 如果`required = true`，那么就意味着不能为空，相当于自带了`@NotNull`或者`@NotEmpty`含义
* 定制化枚举类型（枚举类型需要手动书写，这里根据指定枚举类型自动生成展示信息 `// TODO 暂未开发`）