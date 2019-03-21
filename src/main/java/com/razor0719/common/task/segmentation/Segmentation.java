package com.razor0719.common.task.segmentation;

import java.util.Date;

import com.razor0719.common.domain.Bean;

/**
 * 分段任务接口
 *
 * @author baoyl
 * @created 2019/3/21
 */
public interface Segmentation extends Bean {

    /**
     * 分段标识时间
     * @return Date
     */
    Date getSegmentedTime();
}
