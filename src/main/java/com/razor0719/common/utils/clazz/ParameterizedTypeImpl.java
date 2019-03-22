package com.razor0719.common.utils.clazz;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import com.razor0719.common.domain.BaseBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 泛型类型反射实现类
 *
 * @author baoyl
 * @created 2018/11/15
 */
@AllArgsConstructor
public class ParameterizedTypeImpl extends BaseBean implements ParameterizedType {
    private static final long serialVersionUID = 4093815558137604019L;

    /**
     * 原始类型
     */
    @Getter
    private Class<?> rawType;
    /**
     * 泛型参数类型集合
     */
    private List<Type> actualTypeArguments = Lists.newArrayList();

    ParameterizedTypeImpl(Class<?> rawType, Type... actualTypeArguments) {
        this.rawType = rawType;
        this.actualTypeArguments = Arrays.asList(actualTypeArguments);
    }
    @Override
    public Type[] getActualTypeArguments() {
        return actualTypeArguments.toArray(new Type[0]);
    }

    @Override
    public Type getOwnerType() {
        return null;
    }

    public void addActualTypeArguments(Type... actualTypeArguments) {
        this.actualTypeArguments.addAll(Arrays.asList(actualTypeArguments));
    }

    public void addActualTypeArguments(List<Type> actualTypeArguments) {
        this.actualTypeArguments.addAll(actualTypeArguments);
    }

    @Override
    public String toString() {
        return this.rawType.getName() + '<' + ClsUtils.COMMA_JOINER.join(ClsUtils.formatType(actualTypeArguments)) + '>';
    }
}
