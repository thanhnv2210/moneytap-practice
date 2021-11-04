package com.mtvn.rest.controller.testing;

import com.mtvn.common.http.RestService;
import com.mtvn.common.utilities.DateUtil;
import com.mtvn.enums.error.ErrorCode;
import com.mtvn.i18n.MessageService;
import com.mtvn.rest.api.hook.MoEngageHookApi;
import com.mtvn.rest.dto.MoEngageHookDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Date;

//@RestController
//@RequestMapping(value = "/v1/hook")
//@Component
@Slf4j
public class UtilityTestingController {

//    @Autowired
//    MessageService messageService;
//    @Autowired
//    private RestService restService;


    @PostConstruct
    private void init(){
        log.info("value for INVALID_JSON code is:<{}>", ErrorCode.INVALID_JSON.getMessage());

//        Object postTesting = restService.post("http://localhost:8081/v1/hook/moengage", null, HttpMethod.POST, MoEngageHookDto.builder().email("thanh@moneytap.vn").build(), Object.class);
//        log.info("postTesting Response:<{}>", postTesting);
        log.info("DateUtil -> currentDate:{}",DateUtil.format(new Date(), "yyyy-MM-dd"));
    }
}