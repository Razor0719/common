package com.razor0719.common.utils.clazz;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

import com.razor0719.common.domain.BaseBean;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author baoyl
 * @created 2018/11/22
 */
@NoArgsConstructor
public class WildcardTypeImpl extends BaseBean implements WildcardType {
    private static final long serialVersionUID = -8135245432556509164L;
    private static final Type[] EMPTY_LOWER_BOUNDS = new Type[0];

    private Type upperBound;
    private Type lowerBound;

    public WildcardTypeImpl(Type upperBound) {
        this(upperBound, null);
    }

    public WildcardTypeImpl(Type upperBound, Type lowerBound) {
        if (upperBound != null) {
            this.upperBound = upperBound;
        }
        this.lowerBound = lowerBound;
    }

    @Override
    public Type[] getUpperBounds() {
        return new Type[]{upperBound};
    }

    @Override
    public Type[] getLowerBounds() {
        return lowerBound == null ? EMPTY_LOWER_BOUNDS : new Type[]{lowerBound};
    }

    @Override
    public String toString() {
        if(upperBound == Object.class) {
            return lowerBound == null ? "?" : "? super " + ClsUtils.formatType(lowerBound);
        }
        return "? extends " + ClsUtils.formatType(upperBound);
    }
}
