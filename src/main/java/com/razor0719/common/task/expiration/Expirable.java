package com.razor0719.common.task.expiration;

import java.io.Serializable;
import java.util.Date;

import com.razor0719.common.domain.Bean;

/**
 * 可过期接口
 *
 * @author baoyl
 * @created 2019/3/26
 */
public interface Expirable<K extends Serializable> extends Bean {

    /**
     * 过期记录标识
     * @return K
     */
    K getId();

    /**
     * 过期时间
     * @return Date
     */
    Date getExpiredTime();
}
