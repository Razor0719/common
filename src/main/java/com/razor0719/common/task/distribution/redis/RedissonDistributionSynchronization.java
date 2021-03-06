package com.razor0719.common.task.distribution.redis;

import java.util.concurrent.locks.Lock;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;

import com.google.common.base.Preconditions;
import com.razor0719.common.task.distribution.AbstractDistributedSynchronization;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * redis分布式执行实现
 *
 * @author baoyl
 * @created 2019/4/2
 */
@Getter
@Setter
@RequiredArgsConstructor
public class RedissonDistributionSynchronization extends AbstractDistributedSynchronization {

    private static final String DISTRIBUTION_LOCK_PREFIX = "redissonDistributionLock:";

    @NonNull
    private Redisson redisson;
    @NonNull
    private String lock;

    @Override
    protected Lock buildDistributedLock() {
        if (StringUtils.isNotBlank(lock)) {
            return redisson.getLock(DISTRIBUTION_LOCK_PREFIX + lock);
        } else {
            return redisson.getLock(DISTRIBUTION_LOCK_PREFIX + getDefaultLock());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Preconditions.checkNotNull(redisson, "Property redission can't be null");
    }
}
