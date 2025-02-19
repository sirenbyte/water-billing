package org.example.waterbilling.service.script;


import org.example.waterbilling.model.annotation.FiledTitle;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationScript {
    public static List<Map<String, String>> getFieldsFromClass(Class<?> clazz) {
        List<Map<String, String>> fieldList = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            FiledTitle annotation = field.getAnnotation(FiledTitle.class);
            if (annotation != null) {
                Map<String, String> fieldMap = new HashMap<>();
                fieldMap.put("accessor", field.getName());
                fieldMap.put("title", annotation.value());
                fieldList.add(fieldMap);
            }
        }
        return fieldList;
    }
}
