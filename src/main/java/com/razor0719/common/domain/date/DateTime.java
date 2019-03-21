package com.razor0719.common.domain.date;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.razor0719.common.domain.BaseBean;
import lombok.Getter;
import lombok.Setter;

/**
 * @author baoyl
 * @created 2018/11/26
 */
@Getter
@Setter
public class DateTime extends BaseBean {
    private static final long serialVersionUID = -4063573497603768798L;

    /**
     * time
     */
    private Date time;
    /**
     * Calendar
     */
    private int field;
    /**
     * time format
     */
    private String format;

    public DateTime() {
        this(new Date());
    }

    public DateTime(Date time) {
        this(time, Calendar.MILLISECOND);
    }

    public DateTime(Date time, int field) {
        if (time != null) {
            this.time = DateUtils.truncate(time, field);
        }
        this.field = field;
        this.format = DateFormat.getByField(field) == null ?
                DateFormat.TIMESTAMP.getFormat() : DateFormat.getByField(field).getFormat();
    }

    @Override
    public String toString() {
        return time == null ? StringUtils.EMPTY : DateFormatUtils.format(time, format);
    }
}
