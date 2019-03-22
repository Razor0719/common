package com.razor0719.common.utils.clazz;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.razor0719.common.domain.BaseBean;
import javassist.CtMethod;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 成员方法信息
 *
 * @author baoyl
 * @created 2018/11/15
 */
@Getter
@Setter
@RequiredArgsConstructor
public class MethodInfo extends BaseBean {
    private static final long serialVersionUID = -464432747968932339L;

    /**
     * 方法
     */
    @NonNull
    private final Method method;
    /**
     * javassist方法
     */
    @NonNull
    private final CtMethod ctMethod;
    /**
     * 方法修饰词
     */
    @NonNull
    private final Modifiers modifiers;
    /**
     * 泛型变量及其上界映射
     */
    private Map<String, Class<?>> genericMap = Maps.newHashMap();
    /**
     * 方法参数名
     */
    private String[] parameters;
    /**
     * 方法体起始行
     */
    private int startLine;
    /**
     * 方法体结束行
     */
    private int endLine;
    /**
     * 变量名与局部变量映射
     */
    private Map<String, List<LocalVariableInfo>> localVariableInfoMap;
    /**
     * 行号与局部变量映射
     */
    private Map<Integer, List<LocalVariableInfo>> localVariableInfoLineMap;
}
