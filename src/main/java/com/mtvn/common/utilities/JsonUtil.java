package com.mtvn.common.utilities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mtvn.common.string.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class JsonUtil {

    public static String obj2str(Object o){
        return obj2str(o, false);
    }

    public static <T> T str2obj(String input, Class<T> clazz) {
        if(StringUtils.isEmpty(input)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.readValue(input, clazz);
        } catch (IOException e) {
            log.error("Unable to parse into json: " + StringUtils.trimUpto(input, 64), e);
        }
        return null;
    }

    public static String removeEmptyKeys(String json){
        Type type = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> data = new Gson().fromJson(json, type);

        for (Iterator<Map.Entry<String, Object>> it = data.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, Object> entry = it.next();
            if (entry.getValue() == null) {
                it.remove();
            } else if (entry.getValue() instanceof ArrayList) {
                if (((ArrayList<?>) entry.getValue()).isEmpty()) {
                    it.remove();
                }
            }
        }
        return obj2str(data);
    }

    public static String obj2str(Object o, boolean removeEmptyKeys) {
        String retVal = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            retVal = mapper.setDateFormat(new ISO8601DateFormat()).writeValueAsString(o);
            if(StringUtils.hasText(retVal) && removeEmptyKeys)
                return removeEmptyKeys(retVal);
        } catch (JsonProcessingException e) {
            log.error("Unable to parse into json", e);
        }
        return retVal;
    }

}