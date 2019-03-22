package com.razor0719.common.utils.clazz;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import com.google.common.collect.Maps;
import com.razor0719.common.domain.BaseBean;
import javassist.CtClass;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 类信息
 *
 * @author baoyl
 * @created 2018/11/15
 */
@Getter
@Setter
@RequiredArgsConstructor
public class ClsInfo extends BaseBean {
    private static final long serialVersionUID = 8472323345427637579L;

    /**
     * 类型
     */
    @NonNull
    private Class<?> clazz;
    /**
     * javassist类型
     */
    @NonNull
    private CtClass ctClass;
    /**
     * 类修饰词
     */
    @NonNull
    private Modifiers modifiers;
    /**
     * 泛型与上界映射
     */
    private Map<String, Class<?>> genericMap = Maps.newHashMap();
    /**
     * 属性信息映射
     */
    private Map<Field, FieldInfo> fieldInfoMap = Maps.newHashMap();
    /**
     * 构造方法信息映射
     */
    private Map<Constructor<?>, ConstructorInfo> constructorInfoMap = Maps.newHashMap();
    /**
     * 方法映射
     */
    private Map<Method, MethodInfo> methodInfoMap = Maps.newHashMap();
}
