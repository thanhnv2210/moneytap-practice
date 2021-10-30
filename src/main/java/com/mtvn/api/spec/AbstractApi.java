package com.mtvn.api.spec;

import com.google.gson.Gson;
import com.mtvn.common.http.HttpUtil;
import com.mtvn.common.string.StringUtils;
import com.mtvn.common.utilities.JsonUtil;
import com.mtvn.rest.exception.DisplayMessageException;
import com.netflix.config.DynamicPropertyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
public abstract class AbstractApi<I, O> {
    private static final String suppressedJson = "{\"log\":\"suppressed\"}";
    @Autowired
    protected Gson apiLogPrinter;
    @Autowired protected Environment environment;

    public abstract O callApi(I in);

    protected boolean logInput(){
        return true;
    }
    protected boolean logOutput(){
        return true;
    }

    public O executeApi(I in){
        long startTime = new Date().getTime();
        O out = null;
        try {
            callPreApi(in);
            out = callApi(in);
            callPostApi(in, out, startTime);
            return out;
        } catch (Exception e) {
            if(e instanceof DisplayMessageException) {
                log.error("Api DisplayMessageException: {}", e.getMessage());
            }else {
                String msg = "Api Exception: " + e.getMessage();
                if(e != null)
                    e.printStackTrace();
                log.error(msg, e);
            }
            processApiException(in, e);
            throw e;
        } finally {
            long totalTime = new Date().getTime() - startTime;
            String apiName = getApiName();
            String input = logInput() ? obj2str(in) : suppressedJson;
            String output = out == null ? "null" : logCompleteOutput() ? JsonUtil.obj2str(out) : logOutput() ? obj2str(out) : suppressedJson;
            log.info(String.format("ApiFinished=%s IP=%s Input=%s Output=%s Time=%sms.", apiName, HttpUtil.remoteAddr(), input, output, totalTime));
        }
    }

    private void callPreApi(I in) {
        log.info("ApiStarted={} CId={} MTAppPC={} AppPlat={} AppVer={} AdvId={} MTReqId={}",
                getApiName(), getCustomerId(), HttpUtil.getMtPartnerCode(), getAppPlatform(), getAppVer(), getAdvId(), HttpUtil.getMtRequestId());
    }

    private void callPostApi(I in, O out, long startTime) {

    }

    public void processApiException(I in, Exception e) {

    }

    private boolean logCompleteOutput() {
        return DynamicPropertyFactory.getInstance().getBooleanProperty("logging.api.output.complete", false).getValue();
    }

    private String getApiName() {
        Service apiService = AnnotationUtils.findAnnotation(this.getClass(), Service.class);
        if(apiService == null){
            return "";
        }
        return StringUtils.hasText(apiService.value()) ? apiService.value() : this.getClass().getName();
    }

    /**
     * Using a different library from Jackson as we only want to prevent some things from getting logged in the logs,
     * but they should still go out in the response, which uses Jackson.
     * Fields that not be serialized need to be marked with @com.moneytap.Exclude
     * @param object
     * @return
     */
    private String obj2str(Object object) {
        return apiLogPrinter.toJson(object);
    }

    private String getAppVer() {
        Integer appVersion = HttpUtil.getAppVersionCode();
        return appVersion > 0 ? appVersion.toString() : "NA";
    }

    private String getAdvId() {
        String retVal = HttpUtil.getHeader("device-id");
        return StringUtils.hasText(retVal) ? retVal : "NA";
    }

    private String getAppPlatform() {
        String retVal = HttpUtil.getHeader("app-platform");
        return StringUtils.hasText(retVal) ? retVal : "NA";
    }

    private String getCustomerId() {
        String retVal = HttpUtil.getRequestAttribute("customerId");
        return StringUtils.hasText(retVal) ? retVal : "NA";
    }

}