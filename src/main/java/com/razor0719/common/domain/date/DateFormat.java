package com.razor0719.common.domain.date;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.razor0719.common.domain.Valueable;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * 时间格式常量
 *
 * @author baoyl
 * @created 2018/11/26
 */
@Getter
@RequiredArgsConstructor
public enum DateFormat implements Valueable<String> {
    /**
     * 年份格式
     */
    YEAR("yyyy", "y", 1),
    /**
     * 月份格式
     */
    MONTH("yyyy-MM", "M", 2),
    /**
     * 日期格式
     */
    DATE("yyyy-MM-dd", "d", 5),
    /**
     * 小时格式
     */
    HOUR("yyyy-MM-dd HH:00", "H", 10),
    /**
     * 分钟格式
     */
    MINUTE("yyyy-MM-dd HH:mm", "m", 12),
    /**
     * 秒格式
     */
    SECOND("yyyy-MM-dd HH:mm:ss", "s", 13),
    /**
     * 毫秒格式
     */
    TIMESTAMP("yyyy-MM-dd HH:mm:ss SSS", "S", 14);

    @NonNull
    private String format;
    @NonNull
    private String unit;
    /**
     * @see java.util.Calendar
     */
    @NonNull
    private Integer field;
    private static final Map<Integer, DateFormat> FILED_MAP;

    static {
        Map<Integer, DateFormat> map = Maps.newHashMap();
        for (DateFormat format : DateFormat.values()) {
            map.put(format.getField(), format);
        }
        ImmutableMap.Builder<Integer, DateFormat> formatMapBuilder = ImmutableMap.builder();
        FILED_MAP = formatMapBuilder.putAll(map).build();
    }

    public static DateFormat getByField(int field) {
        return FILED_MAP.get(field);
    }

}
