package com.razor0719.common.domain.log;

import com.razor0719.common.task.expiration.ExpiredCleanupService;

/**
 * Api日志服务
 *
 * @author baoyl
 * @created 2019/3/30
 */
public interface ApiLogService extends ExpiredCleanupService{

    /**
     * 保存api日志
     * @param apiLog
     */
    void save(ApiLog apiLog);

}
