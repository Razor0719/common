package com.razor0719.common.task.distribution;

/**
 * 分布式执行回调接口
 *
 * @author baoyl
 * @created 2019/4/2
 */
public interface DistributedExecutor<T> {

    /**
     * 分布式执行
     * @return T
     * @throws Exception
     */
    T execute() throws Exception;

}
