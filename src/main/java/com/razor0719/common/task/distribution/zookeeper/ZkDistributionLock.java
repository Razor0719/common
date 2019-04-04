package com.razor0719.common.task.distribution.zookeeper;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

/**
 * zookeeper分布式锁
 *
 * @author baoyl
 * @created 2019/4/3
 */
public class ZkDistributionLock implements Lock {

    private static final String DISTRIBUTION_LOCK_PREFIX = "zkDistributionLock_";
    private static final int EXPIRE = 60;
    private InterProcessMutex interProcessMutex;

    public ZkDistributionLock(CuratorFramework curatorFramework, String lock) {
        this.interProcessMutex = new InterProcessMutex(curatorFramework, DISTRIBUTION_LOCK_PREFIX + lock);
    }

    @Override
    public void lock() {
        try {
            lockInterruptibly();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        if (!tryLock(EXPIRE, TimeUnit.SECONDS)) {
            throw new InterruptedException("try to lock timeout!");
        }
    }

    @Override
    public boolean tryLock() {
        try {
            interProcessMutex.acquire();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        try {
            interProcessMutex.acquire(time, unit);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void unlock() {
        try {
            interProcessMutex.release();
        } catch (Exception e) {
            throw new RuntimeException("zklock release failed!");
        }
    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }
}
