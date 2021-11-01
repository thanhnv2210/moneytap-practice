package com.mtvn.application;

import com.netflix.config.DynamicPropertyFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public enum PartnerCode {
    DEFAULT("P0", false, false, 10000, 0, 0, false, false, 0),
    RBL("P1", false, false, 20000, 23, 60, false, false, 4),
    IDFC("P2", true, true, 15000, 21, 56, true, true, 2),
    ABFL("P3", true, true, 10000, 23, 60, false, true, 3),
    DMI("P4", true, true, 20000, 23, 59, true, false, 5),
    SFL("P5", true, true, 25000, 0, 0, false, false, 1),
    FEC("P6", true, true, 25000, 15, 60, false, false, 1);

    @Getter
    private boolean lmsInternal;
    @Getter private boolean scorecardInternal;
    private double defaultMinSalaryForAip;
    private int defaultMinAgeForAip;
    private int defaultMaxAgeForAip;
    @Getter private boolean creditReportPullInternal;
    @Getter private boolean perfiosMandatory;
    @Getter private int weight = 1;
    @Getter private String alias;

    private PartnerCode(String alias, boolean lmsInternal, boolean scorecardInternal, double minSalaryForAip, int minAge, int maxAge,
                        boolean perfiosMandatory, boolean creditReportPullInternal, int weight) {
        this.alias = alias;
        this.lmsInternal = lmsInternal;
        this.scorecardInternal = scorecardInternal;
        this.defaultMinSalaryForAip = minSalaryForAip;
        this.defaultMinAgeForAip = minAge;
        this.defaultMaxAgeForAip = maxAge;
        this.perfiosMandatory = perfiosMandatory;
        this.creditReportPullInternal = creditReportPullInternal;
        this.weight = weight;
    }

    public static Set<PartnerCode> getRealBankCodes() {
        Set<PartnerCode> retVal = new HashSet<>(Arrays.asList(PartnerCode.values()));
        retVal.remove(DEFAULT);
        return retVal;
    }

    public boolean isDefault() {
        return this.equals(DEFAULT);
    }

    public static PartnerCode getPartnerFromString(String partnerCode) {
        if(StringUtils.hasText(partnerCode)) {
            try {
                return PartnerCode.valueOf(partnerCode.toUpperCase());
            }catch (Exception e) {
                log.warn("No partner found for {}", partnerCode);
            }
        }
        return null;
    }

    public int getMinAge() {
        int minAge = DynamicPropertyFactory.getInstance().getIntProperty(name() + ".age.min", -1).getValue();
        return minAge < 0 ? defaultMinAgeForAip : minAge;
    }

    public int getMaxAge() {
        int maxAge = DynamicPropertyFactory.getInstance().getIntProperty(name() + ".age.max", -1).getValue();
        return maxAge < 0 ? defaultMaxAgeForAip : maxAge;
    }

    public double getMinSalaryForAIP() {
        double d = DynamicPropertyFactory.getInstance().getDoubleProperty(name() + ".salary.min", -1).getValue();
        return d < 0 ? defaultMinSalaryForAip : d;
    }

}
