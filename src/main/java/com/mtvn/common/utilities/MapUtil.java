package com.mtvn.common.utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class MapUtil {

    public static Map<String, String> getObjectAsMap(Object obj){
        try {
            Map<String, Object> rawMap = new ObjectMapper().convertValue(obj, new TypeReference<Map<String, Object>>() {});
            rawMap.values().removeIf(Objects::isNull);
            return convertMap(rawMap);
        }
        catch (Exception e){
            log.error("Unable to convert to Desired Map");
        }
        return new HashMap<>();
    }

    public static Map<String, String> convertMap(Map<String, Object> rawMap){
        Map<String, String> dataMap = new HashMap<>();
        try {
            for (Map.Entry<String, Object> entry : rawMap.entrySet()) {
                if((entry.getValue() instanceof String)){
                    dataMap.put(entry.getKey(), (String) entry.getValue());
                }
                else if(entry.getValue() != null){
                    dataMap.put(entry.getKey(), new Gson().toJson(entry.getValue()));
                }
            }
        }
        catch (Exception e){
            log.error("Unable to convert to Desired Map");
        }
        return dataMap;
    }

}
