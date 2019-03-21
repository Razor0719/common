package com.razor0719.common.domain;

/**
 * 枚举对象值接口
 *
 * @author baoyl
 * @created 2018/11/26
 */
public interface Valueable<V> {
    /**
     * 获取枚举对象值
     * @return
     */
    V getFormat();
}
