package com.mtvn.config;

import com.netflix.config.DynamicPropertyFactory;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.Objects;

@Component
public class CoreConfig {

    @Autowired
    private Environment env;
    @Value("${mt.cronEnabled:false}") private boolean cronsEnabled;
    @Getter
    @Value("${api.acs.app_score.url:http://10.99.164.28:8081/acs}") private String acsAppScoreApiUrl;

    public boolean isProd() {
        return env.acceptsProfiles("prod");
    }

    public boolean isCronEnabled() {
        return cronsEnabled;
    }

    public String getSqsRegion() {
        if(env.acceptsProfiles("prod"))
            return "ap-southeast-1"; //TEST FOR VN
        if(env.acceptsProfiles("staging"))
            return "ap-southeast-1";
        if(env.acceptsProfiles("demo"))
            return "ap-southeast-1";
        if(env.acceptsProfiles("dev"))
            return "ap-southeast-1";
        return "ap-southeast-1";
    }

    public String getS3Region() {
        if(env.acceptsProfiles("prod"))
            return "ap-southeast-1";
        if(env.acceptsProfiles("staging"))
            return "ap-southeast-1";
        if(env.acceptsProfiles("demo"))
            return "ap-southeast-1";
        if(env.acceptsProfiles("dev"))
            return "ap-southeast-1";
        return "ap-southeast-1";
    }

    public static String getServiceId() {
        return DynamicPropertyFactory.getInstance().getStringProperty("persistence.ThreeCharServiceId","E01").getValue();
    }

    public static Boolean isPGRequeryEnabled() {
        return DynamicPropertyFactory.getInstance().getBooleanProperty("isPGRequeryEnabled", false).getValue();
    }

    public static BigDecimal getImpsTransferTaxPercentage() {
        double tax = DynamicPropertyFactory.getInstance().getDoubleProperty("ImpsTransferTaxPercentage", 18.00).getValue();
        return new BigDecimal(tax).setScale(0, RoundingMode.CEILING);
    }

    public static String getLineSetupFeeIncludingTaxes() {
        BigDecimal lineSetupFee = new BigDecimal(499).multiply(new BigDecimal(1.18)).setScale(0, RoundingMode.CEILING);
        return String.valueOf(lineSetupFee.doubleValue());
    }

    public static BigDecimal getLSFIncludingTaxes() {
        return new BigDecimal(499).multiply(new BigDecimal(1.18)).setScale(0, RoundingMode.CEILING);
    }

    public static String getTUReferenceSequenceTableName() {
        return DynamicPropertyFactory.getInstance().getStringProperty("TUReferenceSequenceTableName", "rbl_reference_id").getValue();
    }

    public static int getNumberOfDaysForEmiRepayment() {
        return DynamicPropertyFactory.getInstance().getIntProperty("rbl.emi.repayment_days", 20).getValue();
    }

    public static String getNsdlJksPassword() {
        return DynamicPropertyFactory.getInstance().getStringProperty("nsdljksPassword", "pass@123").getValue();
    }

    public static String getNsdlJksName() {
        return DynamicPropertyFactory.getInstance().getStringProperty("nsdljksName", "nsdl.jks").getValue();
    }

//    public static String getNSDLServerUrl(PartnerCode partnerCode) {
//        return DynamicPropertyFactory.getInstance().getStringProperty("nsdlServerUrl." + partnerCode.name().toLowerCase(), "").getValue();
//    }
//
//    public static String getDefaultCompanyCategory(PartnerCode partnerCode) {
//        if(Objects.equals(PartnerCode.RBL, partnerCode))
//            return DynamicPropertyFactory.getInstance().getStringProperty(partnerCode.name().toLowerCase() + ".company.category.default","U").getValue();
//        return DynamicPropertyFactory.getInstance().getStringProperty(partnerCode.name().toLowerCase() + ".company.category.default","OTHER").getValue();
//    }

    public static int getCoolOffPeriodForRejectedCustomers() {
        return DynamicPropertyFactory.getInstance().getIntProperty("reapply.min.rejected.interval", 93).getValue();
    }

    public static int getCoolOffPeriodForQualBlockedCustomers() {
        return DynamicPropertyFactory.getInstance().getIntProperty("reapply.min.qualblock.interval", 32).getValue();
    }

    public String getFacebookAppId() {
        String appId;
        if(env.acceptsProfiles("prod"))
            appId = DynamicPropertyFactory.getInstance().getStringProperty("fb.app.id", "1832882913602396").getValue();
        else if(env.acceptsProfiles("staging"))
            appId = DynamicPropertyFactory.getInstance().getStringProperty("fb.app.id", "1832882913602396").getValue();
        else if(env.acceptsProfiles("dev"))
            appId = DynamicPropertyFactory.getInstance().getStringProperty("fb.app.id", "1832882913602396").getValue();
        else
            appId = "615605825558941";
        return appId;
    }

//    public static boolean isTuServiceAvailable(PartnerCode pc) {
//        boolean isDown = CoreConfig.isTuDown(pc);
//        if(isDown)
//            return false;
//
//        //rbl.tu.downtime=02:55:00-04:05:00
//        String downtime = CoreConfig.getTuDowntime(pc);//Assuming JVM running in Asia/Kolkata
//        String[] parts = downtime.split("-");
//        LocalTime from = LocalTime.parse(parts[0]);
//        LocalTime to = LocalTime.parse(parts[1]);
//        LocalTime now = LocalTime.now();
//        return now.isBefore(from) || now.isAfter(to);
//    }
//
//    public static String getTuDowntime(PartnerCode pc) {
//        return DynamicPropertyFactory.getInstance().getStringProperty(pc.name().toLowerCase() + ".tu.downtime","02:55:00-04:05:00").getValue();
//    }
//
//    public static boolean isTuDown(PartnerCode pc) {
//        return DynamicPropertyFactory.getInstance().getBooleanProperty(pc.name().toLowerCase() + ".tu.down", false).getValue();
//    }

}
