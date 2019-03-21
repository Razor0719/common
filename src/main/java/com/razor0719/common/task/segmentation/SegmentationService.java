package com.razor0719.common.task.segmentation;

import java.util.Date;
import java.util.List;

/**
 * @author baoyl
 * @created 2019/3/21
 */
public interface SegmentationService<S extends Segmentation> {

    /**
     * 分段任务名称
     * @return String
     */
    String getName();

    /**
     * 获取最早的待处理记录
     * @return S
     */
    S getEarliest();

    /**
     * 获取最近的待处理记录
     * @return S
     */
    S getLatest();

    /**
     * 获取时间段内的待处理记录
     * @param startTime
     * @param endTime
     * @return
     */
    List<S> gets(Date startTime, Date endTime);

    /**
     * 处理待处理记录
     * @param segmentations
     */
    void execute(List<S> segmentations);
}
