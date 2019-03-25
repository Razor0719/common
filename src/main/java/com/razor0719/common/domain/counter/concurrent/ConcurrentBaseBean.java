package com.razor0719.common.domain.counter.concurrent;

import java.util.Date;

import com.razor0719.common.domain.BaseBean;
import lombok.Getter;
import lombok.Setter;

/**
 * 并发基础bean
 *
 * @author baoyl
 * @created 2019/3/25
 */
@Getter
@Setter
public class ConcurrentBaseBean extends BaseBean implements ConcurrentSession {
    private Date startTime;
    private Date endTime;
    private int duration;

    @Override
    public boolean isAvalible() {
        return startTime != null && endTime != null && startTime.before(endTime);
    }
}
