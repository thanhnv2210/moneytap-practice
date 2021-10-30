package com.mtvn.common.http;

import com.mtvn.common.utilities.EqualsHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class HttpUtil {

    private static final String HEADER_X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String GEO_LOCATION = "geo-location";

    public static HttpServletRequest getRequest(){
        try{
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            if(attr != null){
                return attr.getRequest();
            }
        }catch(IllegalStateException e){
            log.trace("Method is not called from withing a web request.");
        }
        return null;
    }

    public static String remoteAddr() {
        HttpServletRequest request = HttpUtil.getRequest();
        if (request == null) return "unavailable";
        String remoteAddr = request.getRemoteAddr();
        String x;
        if ((x = request.getHeader(HEADER_X_FORWARDED_FOR)) != null) {
            remoteAddr = x;
            int idx = remoteAddr.indexOf(',');
            if (idx > -1) {
                remoteAddr = remoteAddr.substring(0, idx);
            }
        }
        return remoteAddr;
    }

    public static String getHeader(String header) {
        HttpServletRequest request = HttpUtil.getRequest();
        if (request == null)
            return null;
        return request.getHeader(header);
    }

    public static Integer getAppVersionCode() {
        String s = getHeader("app-versionCode");
        if(StringUtils.hasText(s))
            return Integer.valueOf(s);
        return 0;
    }

    public static String getAppVersion() {
        String s = getHeader("app-version");
        if(StringUtils.hasText(s))
            return s;
        return null;
    }

    public static String getAppPlatform() {
        String s = getHeader("app-platform");
        if(StringUtils.hasText(s))
            return s;
        return null;
    }

    public static String getGeoLocationHeader() {
        return HttpUtil.getHeader(GEO_LOCATION);
    }
    public static String getMtRequestId() {
        String s = getHeader("mt-request-id");
        return StringUtils.hasText(s) ? s : "NA";
    }

    public static String getMtPartnerCode() {
        String s = getHeader("mt-partner-code");
        return StringUtils.hasText(s) ? s : "NA";
    }

    public static String getRequestAttribute(String attrName) {
        HttpServletRequest request = HttpUtil.getRequest();
        return request != null ? (String)request.getAttribute(attrName) : "NA";
    }

    public static void setRequestAttribute(String attrName, String attrVal) {
        HttpServletRequest request = HttpUtil.getRequest();
        if(request != null) {
            request.setAttribute(attrName, attrVal);
        }
    }

    public static String getCustomerInfo() {
        String retVal = HttpUtil.getHeader("customer-id");
        return com.mtvn.common.string.StringUtils.hasText(retVal) ? retVal : "NA";
    }

    public static String getComponentContext() {
        String retVal = HttpUtil.getHeader("req-context");
        return com.mtvn.common.string.StringUtils.hasText(retVal) ? retVal : "NA";
    }

    public static boolean isAndroid() {
        return EqualsHelper.equalsIgnoreCase(getAppPlatform(), "money-tap-android-app");
    }

    public static boolean isIOS() {
        return EqualsHelper.equalsIgnoreCase(getAppPlatform(), "money-tap-ios-app");
    }
}
