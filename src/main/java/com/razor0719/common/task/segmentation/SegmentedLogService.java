package com.razor0719.common.task.segmentation;

import java.util.Date;

/**
 * 分段任务日志服务
 *
 * @author baoyl
 * @created 2019/3/21
 */
public interface SegmentedLogService {
    /**
     * 构建分段任务日志
     * @param name
     * @param startTime
     * @param endTime
     * @return SegmentedLog
     */
    SegmentedLog build(String name, Date startTime, Date endTime);

    /**
     * 保存分段任务日志
     * @param log
     */
    void save(SegmentedLog log);

    /**
     * 获取最近的分段任务日志
     * @param name
     * @return SegmentedLog
     */
    SegmentedLog getLatest(String name);
}
