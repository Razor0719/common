package com.razor0719.common.domain;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.razor0719.common.utils.CloneUtils;

/**
 * @author baoyl
 * @created 2018/5/3
 */
public abstract class BaseBean implements Bean{
    private static final long serialVersionUID = 1908686227693303507L;

    @Override
    public int hashCode() {
        return Objects.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equal(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseBean> T shallowClone() {
        try {
            return (T)clone();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseBean> T deepClone() {
        try {
            //TODO
//            return (T) CloneUtils.recursiveClone(this);
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
