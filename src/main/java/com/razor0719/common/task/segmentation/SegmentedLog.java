package com.razor0719.common.task.segmentation;

import java.util.Date;

import com.razor0719.common.domain.Bean;

/**
 * 分段任务日志接口
 *
 * @author baoyl
 * @created 2019/3/21
 */
public interface SegmentedLog extends Bean {
    /**
     * 分段日志名称
     * @return String
     */
    String getName();

    /**
     * 分段处理开始时间
     * @return Date
     */
    Date getStartTime();

    /**
     * 分段处理结束时间
     * @return Date
     */
    Date getEndTime();

    /**
     * 分段处理操作时间
     * @return Date
     */
    Date getOperatingTime();
}
