package com.razor0719.common.domain.converter;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import com.razor0719.common.domain.date.DateTime;
import lombok.extern.log4j.Log4j2;

/**
 * DateTime转换器
 *
 * @author baoyl
 * @created 2018/11/27
 */
@Log4j2
public class DateTimeConverter implements Converter<String, DateTime> {

    private static final DateConverter DATE_CONVERTER = new DateConverter();
    private static final String SECOND_REGEX = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";
    private static final String MINUTE_REGEX = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$";
    private static final String DATE_REGEX = "^\\d{4}-\\d{2}-\\d{2}$";
    private static final String MONTH_REGEX = "^\\d{4}-\\d{2}$";

    @Override
    public DateTime convert(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        try {
            Date date = DATE_CONVERTER.convert(source);
            int field = Calendar.MILLISECOND;
            if(source.matches(SECOND_REGEX)) {
                field = Calendar.SECOND;
            } else if(source.matches(MINUTE_REGEX)) {
                field = Calendar.MINUTE;
            } else if(source.matches(DATE_REGEX)) {
                field = Calendar.DATE;
            } else if(source.matches(MONTH_REGEX)) {
                field = Calendar.MONTH;
            }
            return new DateTime(date, field);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }
}
