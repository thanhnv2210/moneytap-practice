package com.mtvn.rest.controller.shared;

import com.mtvn.auth.server.enums.InternalAuthenticationType;
import com.mtvn.enums.AuthenticationType;
import com.mtvn.rest.service.DropdownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/shared")
public class DropdownController {

    @Autowired
    DropdownService dropdownService;

    @GetMapping("/companies")
    public ResponseEntity<Map<String, String>> getCompanyList(){
        return ResponseEntity.ok(dropdownService.getCompanyList());
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
