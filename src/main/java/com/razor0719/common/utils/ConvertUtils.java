package com.razor0719.common.utils;

import java.lang.reflect.Field;

import org.apache.commons.lang3.ClassUtils;

import com.google.common.base.CaseFormat;
import com.google.common.base.Objects;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * 转换工具类
 *
 * @author baoyl
 * @created 2018/11/27
 */
@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConvertUtils {

    private static final String MAP_REGEX = "^\\{.*\\}$";
    private static final String LIST_REGEX = "^\\[.*\\]$";
    private static GsonBuilder gsonBuilder = new GsonBuilder().setFieldNamingStrategy(new FieldNamingStrategy() {
        @Override
        public String translateName(Field field) {
            return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
        }
    })
            .enableComplexMapKeySerialization()
            .serializeNulls();

    public static boolean isJsonString(String text) {
        try {
            new JsonParser().parse(text);
            return true;
        } catch (JsonSyntaxException e) {
            log.warn("bad json : " + text);
            return false;
        }
    }

}
