package com.razor0719.common.task.segmentation;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Preconditions;
import com.razor0719.common.domain.date.DateFormat;
import com.razor0719.common.task.TaskHandler;
import lombok.Getter;
import lombok.Setter;

/**
 * 分段任务处理
 *
 * @author baoyl
 * @created 2019/3/21
 */
@Getter
@Setter
public class SegmentationTaskHandler<S extends Segmentation> implements TaskHandler, InitializingBean {

    private SegmentationService<S> segmentationService;
    private SegmentedLogService segmentedLogService;
    /** 分段周期 */
    private int period;
    /** 周期单位：y:年,M:月,d:天,H:时,m:分钟,s:秒,S:毫秒，默认为秒 */
    private DateFormat periodUnit = DateFormat.SECOND;
    /** 分段延迟，默认为一个分段周期 */
    private int delay = -1;

    @Override
    public void handle(Date schedTime, Date firedTime) {
        int period = (int)((firedTime.getTime() - schedTime.getTime())/1000);
        DateFormat unit = DateFormat.SECOND;
        if(this.period > 0) {
            period = this.period;
            unit = periodUnit;
        }
        SegmentedLog segmentedLog = getSegmentedLog(period, unit);
        if (segmentedLog == null) {
            return;
        }
        S latestSegmentation = segmentationService.getLatest();
        while (segmentedLog.getEndTime().before(
                delay < 0 ? addTimes(new Date(), -period, unit) : (delay == 0 ? new Date() : addTimes(new Date(), -delay, unit))
        ) && (latestSegmentation == null || latestSegmentation.getSegmentedTime() == null
                || !segmentedLog.getEndTime().after(latestSegmentation.getSegmentedTime()))) {
            List<S> segmentations = segmentationService.gets(segmentedLog.getStartTime(), segmentedLog.getEndTime());
            if (CollectionUtils.isEmpty(segmentations)) {
                segmentationService.execute(segmentations);
            }
            segmentedLogService.save(segmentedLog);
            //下个周期
            segmentedLog = buildSegmentedLog(segmentedLog.getEndTime(), period, unit);
        }
    }

    private SegmentedLog getSegmentedLog(int period, DateFormat unit) {
        //最近的操作日志
        SegmentedLog segmentedLog = segmentedLogService.getLatest(segmentationService.getName());
        if (segmentedLog == null) {
            //最早的记录
            S earliestSegmentation = segmentationService.getEarliest();
            if (earliestSegmentation == null || earliestSegmentation.getSegmentedTime() == null) {
                return null;
            }
            //构造新的操作日志
            segmentedLog = buildSegmentedLog(DateUtils.truncate(earliestSegmentation.getSegmentedTime(), unit.getField()), period, unit);
        } else {
            //构造下个周期操作日志
            segmentedLog = buildSegmentedLog(segmentedLog.getEndTime(), period, unit);
        }
        return segmentedLog;
    }

    private SegmentedLog buildSegmentedLog(Date startTime, int period, DateFormat unit) {
        return segmentedLogService.build(segmentationService.getName(), startTime, addTimes(startTime, period, unit));
    }

    private Date addTimes(Date date, int amount, DateFormat unit) {
        if (unit == null) {
            unit = DateFormat.SECOND;
        }
        switch (unit) {
            case YEAR:
                return DateUtils.addYears(date, amount);
            case MONTH:
                return DateUtils.addMonths(date, amount);
            case DATE:
                return DateUtils.addDays(date, amount);
            case HOUR:
                return DateUtils.addHours(date, amount);
            case MINUTE:
                return DateUtils.addMinutes(date, amount);
            case SECOND:
                return DateUtils.addSeconds(date, amount);
            default:
                return DateUtils.addMilliseconds(date, amount);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Preconditions.checkNotNull(segmentationService, "segmentationService can not be null");
        Preconditions.checkNotNull(segmentedLogService, "segmentedLogService can not be null");
    }

}
