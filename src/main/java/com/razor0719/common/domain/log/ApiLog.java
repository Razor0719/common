package com.razor0719.common.domain.log;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.razor0719.common.auth.AuthUser;
import com.razor0719.common.domain.Bean;
import com.razor0719.common.task.expiration.Expirable;

/**
 * Api日志
 *
 * @author baoyl
 * @created 2019/3/26
 */
public interface ApiLog extends Expirable {
    /**
     * 当前用户
     *
     * @return AuthUser
     */
    AuthUser getUser();

    /**
     * api资源
     *
     * @return String
     */
    String getResource();

    /**
     * 请求方式
     *
     * @return String
     */
    String getMethod();

    /**
     * URL中的变量
     * @return List<Object></Object>
     */
    List<Object> getPathVariables();

    /**
     * 请求参数
     * @return Map<String, Object>
     */
    Map<String, Object> getRequestParams();

    /**
     * 请求时间
     * @return Date
     */
    Date getRequestTime();

    /**
     * 响应时间
     * @return Date
     */
    Date getResponseTime();

    /**
     * 响应数据
     * @return Object
     */
    Object getResponseData();

    /**
     * 状态码
     * @return Integer
     */
    Integer getStatusCode();

    /**
     * 异常信息
     * @return String
     */
    String getMessage();
}
