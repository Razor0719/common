package com.razor0719.common.utils.clazz;

/**
 * @author baoyl
 * @created 2018/11/15
 */
public interface Modifiers {
    /**
     * 获取modifier int值
     * @return
     */
    int intValue();

    /**
     * 是public
     * @return
     */
    boolean isPublic();

    /**
     * 是private
     * @return
     */
    boolean isPrivate();

    /**
     * 是protected
     * @return
     */
    boolean isProtected();

    /**
     * 是package
     * @return
     */
    boolean isPackage();

    /**
     * 是static
     * @return
     */
    boolean isStatic();

    /**
     * 是final
     * @return
     */
    boolean isFinal();

    /**
     * 是synchronization
     * @return
     */
    boolean isSynchronized();

    /**
     * 是volatile
     * @return
     */
    boolean isVolatile();

    /**
     * 是transient
     * @return
     */
    boolean isTransient();

    /**
     * 是native
     * @return
     */
    boolean isNative();

    /**
     * 是interface
     * @return
     */
    boolean isInterface();

    /**
     * 是annotation
     * @return
     */
    boolean isAnnotation();

    /**
     * 是enum
     * @return
     */
    boolean isEnum();

    /**
     * 是abstract
     * @return
     */
    boolean isAbstract();

    /**
     * 是strict
     * @return
     */
    boolean isStrict();

}
