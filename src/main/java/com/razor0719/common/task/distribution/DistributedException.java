package com.razor0719.common.task.distribution;

/**
 * 分布式异常
 *
 * @author baoyl
 * @created 2019/4/2
 */
public class DistributedException extends Exception {
    private static final long serialVersionUID = -4518543355762369617L;

    public DistributedException() {
    }

    public DistributedException(String message) {
        super(message);
    }

    public DistributedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DistributedException(Throwable cause) {
        super(cause);
    }

    public DistributedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
