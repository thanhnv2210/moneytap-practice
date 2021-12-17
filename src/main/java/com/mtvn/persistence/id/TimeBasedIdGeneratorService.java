package com.mtvn.persistence.id;

import com.netflix.config.DynamicPropertyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class TimeBasedIdGeneratorService {
    private static final AtomicInteger sequenceTill999 = new AtomicInteger(0);
    private static final int MAX_SEQUENCE = 500;

    public String getUniqueId() {
        String datePrefix = getPKDatePrefixFromDate();
        String serverId = getServiceId().trim().substring(0, 3).trim(); //ensure this is fixed length as well, say 3 chars
        String uniqueNess = String.format("%04d", Math.abs(Thread.currentThread().getId() % 10000)).trim();
        String suffix = getNextSequence().trim();
        String retVal = datePrefix + serverId + uniqueNess + suffix;
        if(retVal.length() > 25) {
            log.error("Generated a long PK {} from {}, {}, {}, {}", retVal, datePrefix, serverId, uniqueNess, suffix);
            retVal = retVal.substring(0, 25);
        }
        return retVal;
    }

    public String getPKDatePrefixFromDate(){
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        return now.format(DateTimeFormatter.ofPattern("yyMMddHHmmssSSS"));
    }

    private String getNextSequence() {
        if (sequenceTill999.get() > MAX_SEQUENCE) {
            sequenceTill999.set(0);
        }
        return String.format("%03d", sequenceTill999.incrementAndGet());
    }

    private String getServiceId() {
        return DynamicPropertyFactory.getInstance().getStringProperty("persistence.ThreeCharServiceId","E01").getValue();
    }
}
