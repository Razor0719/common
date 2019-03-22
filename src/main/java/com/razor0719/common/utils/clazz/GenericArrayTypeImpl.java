package com.razor0719.common.utils.clazz;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

import com.razor0719.common.domain.BaseBean;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 泛型数组类型反射实现类
 *
 * @author baoyl
 * @created 2018/11/15
 */
@Getter
@RequiredArgsConstructor
public class GenericArrayTypeImpl extends BaseBean implements GenericArrayType {
    private static final long serialVersionUID = -2928935176411642327L;

    /**
     * 泛型元素类型
     */
    @NonNull
    private Type genericComponentType;

    @Override
    public String toString() {
        return ClsUtils.formatType(genericComponentType);
    }
}
