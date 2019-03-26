package com.razor0719.common.task.expiredcleanup;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author baoyl
 * @created 2019/3/26
 */
public interface ExpiredCleanupService<E extends Expirable<K>, K extends Serializable> {

    /**
     * 获取指定达到过期日期的记录
     * @param expiredLimitTime
     * @return List<E>
     */
    List<E> getExpireds(Date expiredLimitTime);

    /**
     * 清理过期记录
     * @param expired
     */
    void cleanup(E expired);
}
