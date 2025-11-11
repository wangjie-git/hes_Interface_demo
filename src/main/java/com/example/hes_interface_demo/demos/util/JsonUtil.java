package com.example.hes_interface_demo.demos.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public static String toJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("JSON conversion error", e);
        }
    }
}