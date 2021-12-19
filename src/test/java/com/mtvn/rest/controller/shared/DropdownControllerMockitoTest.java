package com.mtvn.rest.controller.shared;

import com.mtvn.rest.service.DropdownService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DropdownControllerMockitoTest {
    @Mock
    private DropdownService dropdownService;

    @InjectMocks
    private DropdownController dropdownController;

    @BeforeEach
    void setMockOutput() {
        when(dropdownService.getCompanyList()).thenReturn(new HashMap<String, String>() {{
            put("key1", "value1");
            put("key2", "value2");
            put("key3", "value3");
        }});
    }

    @Test
    public void shouldReturnDefaultData() {
        ResponseEntity<Map<String,String>> response = dropdownController.getCompanyList();
        Map<String,String> result = response.getBody();
        System.out.println(result);
        assert result != null;
        assertEquals(3, result.size());
    }
}
