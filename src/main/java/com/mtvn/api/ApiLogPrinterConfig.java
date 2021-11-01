package com.mtvn.api;

import com.google.gson.*;
import com.mtvn.Exclude;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ApiLogPrinterConfig {

    @Bean(name="apiLogPrinter")
    public Gson apiLogPinter() {
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new AnnotationExclusionStrategy())
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
                .create();

        return gson;
    }

    class LocalDateAdapter implements JsonSerializer<LocalDate> {
        public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
    }

    class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime> {
        public JsonElement serialize(LocalDateTime dateTime, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
    }

    class ZonedDateTimeAdapter implements JsonSerializer<ZonedDateTime> {
        public JsonElement serialize(ZonedDateTime zoneDateTime, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(zoneDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        }
    }

    class AnnotationExclusionStrategy implements ExclusionStrategy {

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getAnnotation(Exclude.class) != null;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}
