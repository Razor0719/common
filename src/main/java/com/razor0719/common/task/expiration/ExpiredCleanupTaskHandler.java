package com.razor0719.common.task.expiration;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import com.google.common.base.Preconditions;
import com.razor0719.common.domain.date.DateFormat;
import com.razor0719.common.task.TaskHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * 过期清理任务处理
 *
 * @author baoyl
 * @created 2019/3/26
 */
@Getter
@Setter
@Log4j2
public class ExpiredCleanupTaskHandler<E extends Expirable<K>, K extends Serializable> implements TaskHandler, InitializingBean {

    private ExpiredCleanupService<E, K> expiredCleanupService;
    /** 有效期，默认30天 */
    private int validityPeriod = 30;
    /** 周期单位：y:年,M:月,d:天,H:时,m:分钟,s:秒,S:毫秒，默认为秒 */
    private DateFormat periodUnit = DateFormat.DATE;

    @Override
    public void handle(Date schedTime, Date firedTime) {
        List<E> expireds = expiredCleanupService.getExpireds(DateFormat.addTimes(schedTime, validityPeriod, periodUnit));
        expireds.forEach(e -> {
            expiredCleanupService.cleanup(e);
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Preconditions.checkNotNull(expiredCleanupService, "Property expiredCleanupService can't be null!");
    }
}
