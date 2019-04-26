package com.razor0719.common.domain.log;

import java.math.BigInteger;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Api日志构造器
 *
 * @author baoyl
 * @created 2019/3/30
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiLogBuilder {

    private static final String PATH_VARIABLES_REGEX = "/([+\\-]?\\d+|[\\da-f]{24,32})(/|$)";
    private static final String UNKNOWN = "unKnown";
    private static final String ROOT_PATH = "/";
    /** 路径变量索引占位符(正则表达式) */
    private static final String PATH_VARIABLE_INDEX_PLACEHOLDER = "\\?";
    /** 路径变量名表达式(实际的变量名需替换占位符) */
    private static final String PATH_VARIABLE_NAME_EXPRESSION = "{pathvar?}";

    public static String parseClient(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotBlank(ip) && !UNKNOWN.equalsIgnoreCase(ip.trim())){
            String[] ips = ip.replaceAll("^[\\s,;]+", "").split("[\\s,;]+");
            for (String s : ips) {
                if(StringUtils.isNotBlank(s)) {
                    return s;
                }
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(StringUtils.isNotBlank(ip) && !UNKNOWN.equalsIgnoreCase(ip.trim())){
            return ip.trim();
        }
        return request.getRemoteAddr();
    }

    public static String parseResource(HttpServletRequest request) {
        String resource = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (StringUtils.isNotBlank(contextPath) && !Objects.equal(contextPath, ROOT_PATH)) {
            resource = resource.replaceAll("^" + contextPath, "");
        }
        return resource;
    }

    public static String handleResourcePathVariable(String resource, List<Object> pathVariables) {
        if (StringUtils.isBlank(resource)) {
            return resource;
        }
        Matcher matcher = Pattern.compile(PATH_VARIABLES_REGEX).matcher(resource.replaceAll("/+", "//"));
        StringBuffer sb = new StringBuffer();
        int index = 0;
        while(matcher.find()) {
            String pathVariable = matcher.group(1);
            if(pathVariable.length() >= 24) {
                pathVariables.add(pathVariable);
            } else {
                pathVariables.add(new BigInteger(pathVariable));
            }
            matcher.appendReplacement(sb , "/" + PATH_VARIABLE_NAME_EXPRESSION.replaceAll(PATH_VARIABLE_INDEX_PLACEHOLDER, String.valueOf(++index)));
        }
        if(index == 0) {
            return resource;
        }
        matcher.appendTail(sb);
        return sb.toString().replaceAll("//", "/");
    }

}
