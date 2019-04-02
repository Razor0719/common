package com.razor0719.common.task.distribution;

/**
 * 分布式同步执行
 *
 * @author baoyl
 * @created 2019/4/2
 */
public interface DistributedSynchronization {

    /**
     * 分布式执行
     * @param executor
     * @param <D>
     * @return D
     * @throws DistributedException
     */
    <D> D execute(DistributedExecutor<D> executor) throws DistributedException;

    /**
     * 分布式同步执行
     * @param executor
     * @param <D>
     * @return D
     * @throws DistributedException
     */
    <D> D syncExecute(DistributedExecutor<D> executor) throws DistributedException;

    /**
     * 分布式同步执行
     * @param executor
     * @param <D>
     * @param timeout
     * @return D
     * @throws DistributedException
     */
    <D> D syncExecute(DistributedExecutor<D> executor, int timeout) throws DistributedException;

    /**
     * 分布式执行
     * @param lock
     * @param executor
     * @param <D>
     * @return D
     * @throws DistributedException
     */
    <D> D execute(String lock, DistributedExecutor<D> executor) throws DistributedException;

    /**
     * 分布式同步执行
     * @param lock
     * @param executor
     * @param <D>
     * @return D
     * @throws DistributedException
     */
    <D> D syncExecute(String lock, DistributedExecutor<D> executor) throws DistributedException;

    /**
     * 分布式同步执行
     * @param lock
     * @param executor
     * @param <D>
     * @param timeOut
     * @return D
     * @throws DistributedException
     */
    <D> D syncExecute(String lock, DistributedExecutor<D> executor, int timeOut) throws DistributedException;

    /**
     * 默认超时时间
     * @return
     */
    int getDefaultTimeout();

    /**
     * 默认锁名称
     * @return
     */
    String getDefaultLock();
}
