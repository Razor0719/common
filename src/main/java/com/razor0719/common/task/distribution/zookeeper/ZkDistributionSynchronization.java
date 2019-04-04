package com.razor0719.common.task.distribution.zookeeper;

import java.util.concurrent.locks.Lock;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;

import com.google.common.base.Preconditions;
import com.razor0719.common.task.distribution.AbstractDistributedSynchronization;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * zookeeper分布式执行实现
 *
 * @author baoyl
 * @created 2019/4/3
 */
@Getter
@Setter
@RequiredArgsConstructor
public class ZkDistributionSynchronization extends AbstractDistributedSynchronization {

    private static final String DISTRIBUTION_LOCK_PREFIX = "redissonDistributionLock:";

    @NonNull
    private CuratorFramework curatorFramework;
    @NonNull
    private String lock;

    @Override
    public Lock buildDistributedLock() {
        if (StringUtils.isNotBlank(lock)) {
            return new ZkDistributionLock(curatorFramework, DISTRIBUTION_LOCK_PREFIX + lock);
        } else {
            return new ZkDistributionLock(curatorFramework, DISTRIBUTION_LOCK_PREFIX + getDefaultLock());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Preconditions.checkNotNull(curatorFramework, "Property curatorFramework can't be null");
    }
}
