package com.razor0719.common.utils.clazz;

import java.lang.reflect.Constructor;

import com.razor0719.common.domain.BaseBean;
import javassist.CtConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 构造方法信息
 *
 * @author baoyl
 * @created 2018/11/15
 */
@Getter
@Setter
@RequiredArgsConstructor
public class ConstructorInfo extends BaseBean {
    private static final long serialVersionUID = 6339082968381677494L;

    /**
     * 构造方法
     */
    @NonNull
    private Constructor<?> constructor;
    /**
     * javassist构造方法
     */
    @NonNull
    private CtConstructor ctConstructor;
    /**
     * 构造方法修饰词
     */
    @NonNull
    private Modifiers modifiers;
    /**
     * 构造方法参数名
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
}
