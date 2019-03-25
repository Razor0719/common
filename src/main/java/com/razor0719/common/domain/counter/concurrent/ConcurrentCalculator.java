package com.razor0719.common.domain.counter.concurrent;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 并发计算器
 *
 * @author baoyl
 * @created 2019/3/25
 */
@Getter
public class ConcurrentCalculator<S extends ConcurrentSession> {

    @NonNull
    private List<S> sessions = Lists.newArrayList();
    private List<Concurrent> concurrents = Lists.newArrayList();
    private int maxConcurrent;
    private int totalDuration;
    private int maxDuration;
    private int minDuration;

    public ConcurrentCalculator(List<S> sessions) {
        List<Concurrent> concurrents = Lists.newArrayList();
        int totalDuration = 0;
        int maxDuration = Integer.MIN_VALUE;
        int minDuration = Integer.MAX_VALUE;
        if (sessions != null) {
            for (S s : sessions) {
                if (s != null && s.isAvalible()) {
                    this.sessions.add(s);
                    concurrents.add(Concurrent.of(s.getStartTime(), 1));
                    concurrents.add(Concurrent.of(s.getEndTime(), -1));
                    totalDuration += s.getDuration();
                    maxDuration = s.getDuration() > maxDuration ? s.getDuration() : maxDuration;
                    minDuration = s.getDuration() < minDuration ? s.getDuration() : minDuration;
                }
            }
        }
        this.totalDuration = totalDuration;
        this.maxDuration = maxDuration;
        this.minDuration = minDuration;
        Collections.sort(concurrents);
        int maxConcurrent = 0;
        for (Concurrent concurrent : concurrents) {
            int i = concurrents.indexOf(concurrent);
            concurrent.setCount(i == 0 ? 0 : concurrents.get(i - 1).getCount() + concurrent.getValue());
            maxConcurrent = concurrent.getCount() > maxConcurrent ? concurrent.getCount() : maxConcurrent;
        }
        this.maxConcurrent = maxConcurrent;
    }

    /**
     * 获取某个时间点（最接近）的并发量
     *
     * @param time
     * @return
     */
    public int concurrent(Date time) {
        int left = 1;
        int right = this.concurrents.size() - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            switch (time.compareTo(this.concurrents.get(mid).getTime())) {
                case -1:
                    right = mid - 1;
                    break;
                case 1:
                    left = mid + 1;
                    break;
                case 0:
                    return this.concurrents.get(mid).getCount();
                default:
                    break;
            }
        }
        if (left <= 0) {
            return this.concurrents.get(0).getCount();
        }
        if (right >= this.concurrents.size()) {
            return this.concurrents.get(this.concurrents.size() - 1).getCount();
        }
        long dleft = time.getTime() - this.concurrents.get(left - 1).getTime().getTime();
        long dright = this.concurrents.get(left).getTime().getTime() - time.getTime();
        return dleft < dright ? this.concurrents.get(left - 1).getCount() : this.concurrents.get(left).getCount();
    }

    /**
     * 获取某个时间段的最大并发
     *
     * @param timeRange
     * @return
     */
    public int maxConcurrent(Range<Date> timeRange) {
        int maxConcurrent = 0;
        for (Concurrent concurrent : this.concurrents) {
            if (timeRange.contains(concurrent.getTime())) {
                maxConcurrent = concurrent.getCount() > maxConcurrent ? concurrent.getCount() : maxConcurrent;
            }
        }
        return maxConcurrent;
    }


    @Getter
    @RequiredArgsConstructor(staticName = "of")
    class Concurrent implements Comparable<Concurrent> {
        @NonNull
        private final Date time;
        @NonNull
        private final int value;
        @Setter
        private int count;

        @Override
        public int compareTo(@NonNull Concurrent o) {
            return time.compareTo(o.time);
        }
    }
}
