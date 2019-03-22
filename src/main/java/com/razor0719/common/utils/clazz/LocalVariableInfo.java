package com.razor0719.common.utils.clazz;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import com.razor0719.common.domain.BaseBean;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 局部变量信息
 *
 * @author baoyl
 * @created 2018/11/15
 */
@Getter
@Setter
public class LocalVariableInfo extends BaseBean {
    private static final long serialVersionUID = 3713727838960287502L;

    /**
     * 变量名
     */
    private String name;
    /**
     * 变量类型
     */
    private Type type;
    /**
     * 所属方法
     */
    private Method method;
    /**
     * 变量索引
     */
    private int index;
    /**
     * 定义变量所在行
     */
    private int line;
    /**
     * 变量初始化终止行
     */
    private int initLine;
    /**
     * 变量有效使用起始行
     */
    private int startLine;
}
