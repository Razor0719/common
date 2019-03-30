package com.razor0719.common.domain.converter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.core.convert.converter.Converter;

import com.google.gson.Gson;
import com.razor0719.common.utils.ConvertUtils;
import lombok.extern.log4j.Log4j2;

/**
 * Date转换器
 *
 * @author baoyl
 * @created 2018/11/27
 */
@Log4j2
public class DateConverter implements Converter<String, Date> {

    private static final String LONG_REGEX = "^\\d+$";
    private static final String NORMAL_DATE_REGEX = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3,}$";
    private static final String TIMEZONE_DATE_REGEX = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d{3,})?Z?$";
    private static final String MILLSECOND_DATE_REGEX = "^.+\\.\\d{3,}Z?$";
    private static final String ZONE = "Z";

    @Override
    public Date convert(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        try {
            if (source.matches(LONG_REGEX)) {
                return new Date(Long.parseLong(source));
            }
            if (ConvertUtils.isJsonString(source)) {
                return new Gson().fromJson(source, Date.class);
            }
            if (source.matches(TIMEZONE_DATE_REGEX)) {
                String format;
                String value = source.replaceAll("\\D", StringUtils.EMPTY);
                if (value.matches(MILLSECOND_DATE_REGEX)) {
                    format = "yyyyMMddHHmmssSSS";
                    value = value.substring(0, 17);
                } else {
                    format = "yyyyMMddHHmmss";
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new SimpleDateFormat(format).parse(value));
                if (source.endsWith(ZONE)) {
                    calendar.add(Calendar.MILLISECOND, Calendar.getInstance().getTimeZone().getRawOffset());
                }
                return calendar.getTime();
            }
            if (source.matches(NORMAL_DATE_REGEX)) {
                return DateUtils.parseDate(source.substring(0, 23), "yyyy-MM-dd HH:mm:ss.SSS");
            }
            return DateUtils.parseDate(source, Locale.US, "yyyy-MM", "yyyy-MM-dd", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss",
                    "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE, dd MMM yyyy HH:mm:ss zzz", "EEE MMM dd HH:mm:ss zzz yyyy", "EEE MMM dd HH:mm:ss yyyy");
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }
}
