package com.razor0719.common.utils.clazz;

import java.lang.reflect.Field;

import com.razor0719.common.domain.BaseBean;
import javassist.CtField;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 成员属性信息
 *
 * @author baoyl
 * @created 2018/11/15
 */
@Getter
@RequiredArgsConstructor
public class FieldInfo extends BaseBean {
    private static final long serialVersionUID = -924749185394597551L;

    /**
     * 属性
     */
    @NonNull
    private Field field;
    /**
     * javassist属性
     */
    @NonNull
    private CtField ctField;
    /**
     * 属性修饰词
     */
    @NonNull
    private Modifiers modifiers;
}
