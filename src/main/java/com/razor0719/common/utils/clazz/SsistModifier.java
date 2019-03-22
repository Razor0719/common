package com.razor0719.common.utils.clazz;

import javassist.Modifier;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.razor0719.common.domain.BaseBean;

/**
 * Modifier的ssist实现
 *
 * @author baoyl
 * @created 2018/11/15
 */
@RequiredArgsConstructor()
public class SsistModifier extends BaseBean implements Modifiers {
    private static final long serialVersionUID = 669245960356081533L;

    @NonNull
    private int flag;

    @Override
    public int intValue() {
        return flag;
    }

    @Override
    public boolean isPublic() {
        return Modifier.isPublic(flag);
    }

    @Override
    public boolean isPrivate() {
        return Modifier.isPrivate(flag);
    }

    @Override
    public boolean isProtected() {
        return Modifier.isProtected(flag);
    }

    @Override
    public boolean isPackage() {
        return Modifier.isPackage(flag);
    }

    @Override
    public boolean isStatic() {
        return Modifier.isStatic(flag);
    }

    @Override
    public boolean isFinal() {
        return Modifier.isFinal(flag);
    }

    @Override
    public boolean isSynchronized() {
        return Modifier.isSynchronized(flag);
    }

    @Override
    public boolean isVolatile() {
        return Modifier.isVolatile(flag);
    }

    @Override
    public boolean isTransient() {
        return Modifier.isTransient(flag);
    }

    @Override
    public boolean isNative() {
        return Modifier.isNative(flag);
    }

    @Override
    public boolean isInterface() {
        return Modifier.isInterface(flag);
    }

    @Override
    public boolean isAnnotation() {
        return Modifier.isAnnotation(flag);
    }

    @Override
    public boolean isEnum() {
        return Modifier.isAnnotation(flag);
    }

    @Override
    public boolean isAbstract() {
        return Modifier.isAbstract(flag);
    }

    @Override
    public boolean isStrict() {
        return Modifier.isStrict(flag);
    }
}
