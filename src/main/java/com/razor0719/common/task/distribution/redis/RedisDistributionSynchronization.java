package com.razor0719.common.task.distribution.redis;

import java.util.concurrent.TimeUnit;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.InitializingBean;

import com.google.common.base.Preconditions;
import com.razor0719.common.task.distribution.DistributedException;
import com.razor0719.common.task.distribution.DistributedExecutor;
import com.razor0719.common.task.distribution.DistributedSynchronization;
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
@NoArgsConstructor
@RequiredArgsConstructor
public class RedisDistributionSynchronization implements DistributedSynchronization, InitializingBean {

    private static final String DISTRIBUTION_LOCK_PREFIX = "redisDistributionLock:";
    private static final int DEFAULT_TIMEOUT = 30;

    @NonNull
    private Redisson redisson;
    private int defaultTimeout = DEFAULT_TIMEOUT;

    @Override
    public <D> D execute(DistributedExecutor<D> executor) throws DistributedException {
        return execute(getDefaultLock(), executor);
    }

    @Override
    public <D> D syncExecute(DistributedExecutor<D> executor) throws DistributedException {
        return execute(getDefaultLock(), executor);
    }

    @Override
    public <D> D syncExecute(DistributedExecutor<D> executor, int timeout) throws DistributedException {
        return syncExecute(getDefaultLock(), executor, timeout);
    }

    @Override
    public <D> D execute(String lock, DistributedExecutor<D> executor) throws DistributedException {
        RLock rLock = redisson.getLock(DISTRIBUTION_LOCK_PREFIX + lock);
        if (!rLock.tryLock()) {
            throw new DistributedException("其他进程正在执行！");
        }
        try {
            return executor.execute();
        } catch (Exception e) {
            throw new DistributedException(e);
        } finally {
            rLock.unlock();
        }
    }

    @Override
    public <D> D syncExecute(String lock, DistributedExecutor<D> executor) throws DistributedException {
        return syncExecute(lock, executor, getDefaultTimeout());
    }

    @Override
    public <D> D syncExecute(String lock, DistributedExecutor<D> executor, int timeOut) throws DistributedException {
        RLock rLock = redisson.getLock(DISTRIBUTION_LOCK_PREFIX + lock);
        try {
            if (!rLock.tryLock((long) timeOut, TimeUnit.SECONDS)) {
                throw new DistributedException("同步执行等待超时！");
            }
        } catch (Exception e) {
            throw new DistributedException(e);
        }
        try {
            return executor.execute();
        } catch (Exception e) {
            throw new DistributedException(e);
        } finally {
            rLock.unlock();
        }
    }

    @Override
    public int getDefaultTimeout() {
        return defaultTimeout;
    }

    @Override
    public String getDefaultLock() {
        return Thread.currentThread().getStackTrace()[3].toString();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Preconditions.checkNotNull(redisson, "Property redisson can't be null");
    }
}
