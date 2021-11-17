package com.mtvn.auth.server.controller;

import com.mtvn.auth.server.model.AuthenticationRequestModel;
import com.mtvn.auth.server.model.AuthenticationResponseModel;
import com.mtvn.auth.server.model.UserDetailModel;
import com.mtvn.auth.server.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserAuthService userAuthService;

    @PostMapping("/token")
    public ResponseEntity<?> getToken(@RequestBody AuthenticationRequestModel model){
        Optional<AuthenticationResponseModel> token = userAuthService.getToken(model);
        return token.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity("Invalid Credentials", HttpStatus.FORBIDDEN));
    }

    @GetMapping("/token/verify")
    public ResponseEntity<?> getToken(@RequestParam("token") String token){
        Optional<UserDetailModel> model = userAuthService.verifyToken(token);
        return ResponseEntity.ok(new HashMap<String, Object>() {{
            put("result", model.isPresent());
            put("userDetail", model.orElse(null));
        }});
    }

    @GetMapping("/certificates")
    public ResponseEntity<?> getCertificates(){
        return ResponseEntity.ok(Collections.singletonMap("keys",userAuthService.getCertificates()));
    }

}
