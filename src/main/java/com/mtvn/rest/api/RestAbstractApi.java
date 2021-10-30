package com.mtvn.rest.api;

import com.mtvn.api.spec.AbstractApi;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class RestAbstractApi<I, O> extends AbstractApi<I, O> {

    /*@Autowired
    protected ApiSecurityService apiSecurityService;
    @Autowired private ApplicationDao applicationDao;

    protected Customer getAuthCustomer() {
        return apiSecurityService.getAuthCustomer();
    }

    @Override
    public void processApiException(I in, Exception e) {
        if(e == null)
            return;
        Customer c = getAuthCustomer();
        if(c == null || !StringUtils.hasText(c.getCurrentApplicationId()))
            return;
        Application app = applicationDao.findById(c.getCurrentApplicationId());
        if(app == null)
            return;

        String errMsg = DateUtil.format(new Date(), DateUtil.DATE_TIME_ISO_8601);
        app.setErrorMessage(errMsg + ":" + e.getClass().getSimpleName() + "-" + e.getMessage());
        applicationDao.saveOrUpdate(app);
    }*/

}