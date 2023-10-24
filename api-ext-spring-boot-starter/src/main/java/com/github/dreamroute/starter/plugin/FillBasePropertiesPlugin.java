package com.github.dreamroute.starter.plugin;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.github.dreamroute.starter.constraints.*;
import com.github.dreamroute.starter.constraints.ApiExtDate.Phase;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import springfox.documentation.schema.Xml;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;

import static springfox.documentation.swagger.common.SwaggerPluginSupport.pluginDoesApply;

/**
 * 描述：插件，填充原本<code>@ApiModelProperty</code>的基础属性
 *
 * @author w.dehi.2022-05-18
 */
@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1) // 需要在ApiModelPropertyPropertyBuilder之后被调用，用于覆盖默认属性
public class FillBasePropertiesPlugin implements ModelPropertyBuilderPlugin {

    public static final String SPECIAL = "_____";

    private static final Map<Class<?>, Map<String, Integer>> ORDER_CACHE = new ConcurrentHashMap<>();
    public static final Set<Class<?>> API_EXT_ANNOS;

    static {
        API_EXT_ANNOS = ClassUtil.scanPackageByAnnotation(ApiExtMarker.class.getPackage().getName(), ApiExtMarker.class);
    }

    @Resource
    private PluginRegistry<EnumPlugin, Class<?>> registry;

    private int getPosition(Class<?> dtoCls, String name) {
        Map<String, Integer> map = ORDER_CACHE.get(dtoCls);
        if (map == null || map.isEmpty()) {
            map = new ConcurrentHashMap<>();
            ORDER_CACHE.put(dtoCls, map);
            Field[] fields = ReflectUtil.getFields(dtoCls);
            for (int i = 0; i < fields.length; i++) {
                map.put(fields[i].getName(), i);
            }
        }
        return map.get(name);
    }

    @Override
    public void apply(ModelPropertyContext context) {

        Class<?> dtoCls = context.getResolver().resolve(context.getOwner().getType()).getErasedType();

        context.getBeanPropertyDefinition().ifPresent(x -> {
            AnnotatedField af = x.getField();
            if (af == null) {
                return;
            }
            Field field = af.getAnnotated();
            ApiExtMarker an = AnnotationUtils.findAnnotation(field, ApiExtMarker.class);
            if (an != null) {
                // 使用xml来代表顺序，最后再将其设置成null
                context.getSpecificationBuilder()
                        .xml(new Xml().name(String.valueOf(getPosition(dtoCls, field.getName()))));

                // 处理插件类型
                Class<?> type;
                if (Collection.class.isAssignableFrom(field.getType())) {
                    ParameterizedType pts = (ParameterizedType) field.getGenericType();
                    type = (Class<?>) pts.getActualTypeArguments()[0];
                } else {
                    type = field.getType();
                }
                registry.getPluginFor(type).ifPresent(c -> {
                    String[] desc = c.desc(type);
                    if (desc.length > 0) {
                        String description = String.join("; ", desc);
                        context.getSpecificationBuilder()
                                .xml(new Xml()
                                        .name(String.valueOf(getPosition(dtoCls, field.getName())))
                                        .namespace(SPECIAL + description + SPECIAL));
                    }
                });
            }

            // 如果是返回参数
            ApiExtResp apiExtResp = AnnotationUtils.findAnnotation(field, ApiExtResp.class);
            if (apiExtResp != null) {
                context.getSpecificationBuilder()
                        .description(apiExtResp.value())
                        .isHidden(apiExtResp.hidden());
            }
            // 如果是请求参数
            else if (an != null && !CollectionUtils.isEmpty(API_EXT_ANNOS)) {
                for (Class<?> apiExtAnno : API_EXT_ANNOS) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> attr = AnnotationUtil.getAnnotationValueMap(field, (Class<Annotation>) apiExtAnno);
                    if (!CollectionUtils.isEmpty(attr)) {

                        // 获取校验信息
                        String validation = createValidation(apiExtAnno, attr);

                        // 将基本属性织入到swagger中
                        context.getSpecificationBuilder()
                                .description(attr.get("name") + validation)
                                .required((Boolean) attr.get("required"))
                                .isHidden((Boolean) attr.get("hidden"));
                        break;
                    }
                }
            }
        });
    }

    private String createValidation(Class<?> anno, Map<String, Object> attr) {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        if (anno == ApiExtStr.class
                || anno == ApiExtInteger.class
                || anno == ApiExtLong.class
                || anno == ApiExtBigDecimal.class
                || anno == ApiExtCollection.class
                || anno == ApiExtArray.class) {

            String minStr = null;
            String maxStr = null;

            // 如果未手动指定int和long的最小值，那么就是该类型的最小值，页面展示负无穷大
            Object min = attr.get("min");
            Object max = attr.get("max");
            boolean minInteger = min instanceof Integer && ((Integer) min).equals(Integer.MIN_VALUE);
            boolean minLong = min instanceof Long && ((Long) min).equals(Long.MIN_VALUE);
            if (minInteger || minLong) {
                minStr = "-∞";
            }

            // 如果未手动指定int和long的最大值，那么就是该类型的最大值，页面展示正无穷大
            boolean maxInteger = max instanceof Integer && ((Integer) max).equals(Integer.MAX_VALUE);
            boolean maxLong = max instanceof Long && ((Long) max).equals(Long.MAX_VALUE);
            if (maxInteger || maxLong) {
                maxStr = "+∞";
            }

            if (StringUtils.isEmpty(minStr)) {
                minStr = attr.get("min").toString();
            }
            if (StringUtils.isEmpty(maxStr)) {
                maxStr = attr.get("max").toString();
            }

            joiner.add(minStr).add(maxStr);
        } else if (anno == ApiExtDate.class) {
            Phase p = (Phase) attr.get("phase");
            switch (p) {
                case All: joiner.add("无限制"); break;
                case Past: joiner.add("[过去]"); break;
                case PastOrPresent: joiner.add("过去或者现在"); break;
                case Future: joiner.add("将来"); break;
                case FutureOrPresent: joiner.add("将来或者现在"); break;
                default: throw new IllegalArgumentException("时间有误");
            }
        } else {
            return "";
        }
        return joiner.toString();
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return pluginDoesApply(delimiter);
    }
}
