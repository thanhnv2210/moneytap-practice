package com.mtvn.rest.controller.hook;

import com.mtvn.rest.api.hook.MoEngageHookApi;
import com.mtvn.rest.dto.MoEngageHookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/hook")
public class MoEngageHookController {
    @Autowired
    MoEngageHookApi moEngageHookApi;

    @PostMapping(value = "/moengage", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> hookSalvageCustomer(@RequestBody MoEngageHookDto data){
        return new ResponseEntity<>(moEngageHookApi.executeApi(data), HttpStatus.OK);
    }
}