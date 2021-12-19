package com.mtvn.rest.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DropdownService {
    public Map<String, String> getCompanyList() {
        return new HashMap<String, String>() {{
            put("key1", "value1");
            put("key2", "value2");
        }};
    }
}
