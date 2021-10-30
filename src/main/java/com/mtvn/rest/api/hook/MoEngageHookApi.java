package com.mtvn.rest.api.hook;

import com.mtvn.rest.api.RestAbstractApi;
import com.mtvn.rest.dto.MoEngageHookDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("moengageHookApi")
public class MoEngageHookApi extends RestAbstractApi<MoEngageHookDto, Object> {

    @Override
    public Object callApi(MoEngageHookDto req) {
        return "SUCCESS";
    }
}
