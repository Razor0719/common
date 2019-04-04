package com.razor0719.common.task.distribution;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import org.springframework.beans.factory.InitializingBean;

import lombok.Getter;
import lombok.Setter;

/**
 * 分布式同步执行
 *
 * @author baoyl
 * @created 2019/4/2
 */
public abstract class AbstractDistributedSynchronization implements InitializingBean {

    protected static final String DISTRIBUTION_LOCK_PREFIX = "distributionLock:";
    protected static final int DEFAULT_TIMEOUT = 30;
    @Getter
    @Setter
    protected int defaultTimeout = DEFAULT_TIMEOUT;

    /**
     * 分布式执行
     *
     * @param executor
     * @param <D>
     * @return D
     * @throws DistributedException
     */
    protected <D> D execute(DistributedExecutor<D> executor) throws DistributedException {
        return execute(getDefaultLock(), executor);
    }

    /**
     * 分布式同步执行
     *
     * @param executor
     * @param <D>
     * @return D
     * @throws DistributedException
     */
    protected <D> D syncExecute(DistributedExecutor<D> executor) throws DistributedException {
        return syncExecute(getDefaultLock(), executor);
    }

    ;

    /**
     * 分布式同步执行
     *
     * @param executor
     * @param <D>
     * @param timeout
     * @return D
     * @throws DistributedException
     */
    protected <D> D syncExecute(DistributedExecutor<D> executor, int timeout) throws DistributedException {
        return syncExecute(getDefaultLock(), executor, timeout);
    }

    /**
     * 分布式执行
     *
     * @param lock
     * @param executor
     * @param <D>
     * @return D
     * @throws DistributedException
     */
    protected <D> D execute(String lock, DistributedExecutor<D> executor) throws DistributedException {
        Lock distributedLock = buildDistributedLock();
        if (!distributedLock.tryLock()) {
            throw new DistributedException("Other processes are executing!");
        }
        try {
            return executor.execute();
        } catch (Exception e) {
            throw new DistributedException(e);
        } finally {
            distributedLock.unlock();
        }
    }

    /**
     * 分布式同步执行
     *
     * @param lock
     * @param executor
     * @param <D>
     * @return D
     * @throws DistributedException
     */
    protected <D> D syncExecute(String lock, DistributedExecutor<D> executor) throws DistributedException {
        return syncExecute(lock, executor, getDefaultTimeout());
    }

    /**
     * 分布式同步执行
     *
     * @param lock
     * @param executor
     * @param <D>
     * @param timeOut
     * @return D
     * @throws DistributedException
     */
    protected <D> D syncExecute(String lock, DistributedExecutor<D> executor, int timeOut) throws DistributedException {
        Lock distributedLock = buildDistributedLock();
        try {
            if (!distributedLock.tryLock((long) timeOut, TimeUnit.SECONDS)) {
                throw new DistributedException("Synchronous execution wait timeout!");
            }
        } catch (Exception e) {
            throw new DistributedException(e);
        }
        try {
            return executor.execute();
        } catch (Exception e) {
            throw new DistributedException(e);
        } finally {
            distributedLock.unlock();
        }
    }

    /**
     * 默认锁名称
     *
     * @return String
     */
    protected String getDefaultLock() {
        return Thread.currentThread().getStackTrace()[3].toString();
    }

    /**
     * 构造分布式锁
     *
     * @return Lock
     */
    protected abstract Lock buildDistributedLock();
}
