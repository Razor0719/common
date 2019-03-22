package com.razor0719.common.utils.clazz;

import com.razor0719.common.domain.BaseBean;
import lombok.Getter;
import lombok.Setter;

/**
 * 方法堆栈信息
 *
 * @author baoyl
 * @created 2018/11/15
 */
@Getter
@Setter
public class MethodStackTraceInfo extends BaseBean {
    private static final long serialVersionUID = 3885517829899506727L;

    /**
     * 调用的方法信息
     */
    private MethodInfo methodInfo;
    /**
     * 下一层方法所在行号
     */
    private int line;
}
