package com.razor0719.common.utils.clazz;

import com.razor0719.common.domain.BaseBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author baoyl
 * @created 2018/11/16
 */
@Getter
@Setter
@AllArgsConstructor
public class StackTraceMethodInfo extends BaseBean {
    private static final long serialVersionUID = 5773510579340081173L;

    /**
     * 调用方法信息
     */
    private MethodInfo methodInfo;
    /**
     * 调用下一层方法所在行
     */
    private int line;
}
