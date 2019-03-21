package com.razor0719.common.task;

import java.util.Date;

/**
 * @author baoyl
 * @created 2019/3/21
 */
public interface TaskHandler {

    /**
     * 任务处理回调
     * @param schedTime
     * @param firedTime
     */
    void handle(Date schedTime, Date firedTime);
}
