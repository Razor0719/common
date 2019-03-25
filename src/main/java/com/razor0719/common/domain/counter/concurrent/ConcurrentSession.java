package com.razor0719.common.domain.counter.concurrent;

import java.util.Date;

/**
 * 并发会话接口
 *
 * @author baoyl
 * @created 2019/3/25
 */
public interface ConcurrentSession {

    /**
     * 开始时间
     * @return Date
     */
    Date getStartTime();

    /**
     * 结束时间
     * @return Date
     */
    Date getEndTime();

    /**
     * 持续时间
     * @return Date
     */
    int getDuration();

    /**
     * 有效性检验
     * @return boolean
     */
    boolean isAvalible();
}
