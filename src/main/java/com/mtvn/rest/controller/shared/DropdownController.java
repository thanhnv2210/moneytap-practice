package com.mtvn.rest.controller.shared;

import com.mtvn.auth.server.enums.InternalAuthenticationType;
import com.mtvn.enums.AuthenticationType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/shared")
public class DropdownController {

    @GetMapping("/companies")
    private ResponseEntity<Map<String, String>> getCompanyList(){
        return ResponseEntity.ok(
                new HashMap<String, String>() {{
                    put("key1", "value1");
                    put("key2", "value2");
                }}
        );
    }

    @GetMapping("/auth/internal-type")
    private ResponseEntity<?> getInternalAuthTypeList(){
        return ResponseEntity.ok(
                InternalAuthenticationType.values()
        );
    }

    @GetMapping("/auth/type")
    private ResponseEntity<?> getAuthTypeList(){
        return ResponseEntity.ok(
                AuthenticationType.values()
        );
    }

}
