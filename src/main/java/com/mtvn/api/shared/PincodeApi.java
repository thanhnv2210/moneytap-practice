package com.mtvn.api.shared;

import com.mtvn.common.string.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service("pincodeApi")
public class PincodeApi {

    private static Map<String, Set<String>> pincodeCities = new HashMap<>();
    private static Map<String, String> pincodeStateMap = new HashMap<>();
    private static Map<String, Set<String>> cityPincodeMap = new HashMap<>();
    private static Map<String, Set<String>> statePincodeMap = new HashMap<>();

    @PostConstruct
    private void init() {
        LocalDateTime start = LocalDateTime.now();
        loadPincodes();
        LocalDateTime end = LocalDateTime.now();
        log.debug("Loaded {} pincodes in {} ms.", pincodeCities.keySet().size(), ChronoUnit.MILLIS.between(start, end));
    }

    public static boolean isValidStateInPincode(String pincode, String state) {
        if(!StringUtils.hasText(getSanitizedArea(state)))
            return false;
        return Objects.equals(getState(pincode), getSanitizedArea(state));
    }

    public static String getCity(String pincode) {
        //		if(StringUtils.hasText(pincode) && pincodeCities.containsKey(pincode)){
        //			 Set<String> cities = pincodeCities.get(pincode);
        //			 for(String city : cities)
        //				 return city;
        //		}
        return null;
    }

    public static String getState(String pincode) {
        if(!StringUtils.hasText(pincode))
            return null;
        return pincodeStateMap.containsKey(pincode) ? pincodeStateMap.get(pincode) : getStateAsPerTuefGuide(pincode);
    }

    public static Set<String> getPincodesForCity(String city) {
        return StringUtils.hasText(getSanitizedArea(city)) ? cityPincodeMap.get(getSanitizedArea(city)) : new TreeSet<>();
    }

    public static Set<String> getPincodesForState(String state) {
        return StringUtils.hasText(getSanitizedArea(state)) ? statePincodeMap.get(getSanitizedArea(state)) : new TreeSet<>();
    }

    private void loadPincodes() {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("pincode.properties");
        try {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if(log.isTraceEnabled())
                        log.trace("Reading {}", line);
                    if(StringUtils.hasText(line)) {
                        String[] parts = line.split(",");
                        String pincode = parts[0];
                        String city = getSanitizedArea(parts[1].toUpperCase());
                        String state = getSanitizedArea(parts[2].toUpperCase());
                        if(StringUtils.hasText(city) && !Objects.equals("NULL", city)) {
                            if(pincodeCities.containsKey(pincode))
                                log.trace("Pincode {} already added: {}", pincode, line);
                            else
                                pincodeCities.put(pincode, new TreeSet<>());
                            pincodeCities.get(pincode).add(city);
                            if(!cityPincodeMap.containsKey(city))
                                cityPincodeMap.put(city, new TreeSet<>());
                            Set<String> cityPincodes = cityPincodeMap.get(city);
                            cityPincodes.add(pincode);
                        }
                        if(StringUtils.hasText(state) && !Objects.equals("NULL", state)) {
                            if(pincodeStateMap.containsKey(pincode))
                                log.trace("Pincode {} already added: {}", pincode, line);
                            pincodeStateMap.put(pincode, state);
                            if(!statePincodeMap.containsKey(state))
                                statePincodeMap.put(state, new TreeSet<>());
                            Set<String> statePincodes = statePincodeMap.get(state);
                            statePincodes.add(pincode);
                        }
                    }
                }
                IOUtils.closeQuietly(br);
            }
        }catch(Exception e) {
            log.error("Unable to load Pincodes", e);
        }finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    private static String getStateAsPerTuefGuide(String sPincode) {
        Integer iPincode = null;
        if(StringUtils.hasText(sPincode)) {
            try {
                iPincode = Integer.parseInt(sPincode);
            }catch (Exception e) {
                log.error("Invalid Pincode: {}", sPincode);
            }
        }
        if(iPincode == null || iPincode < 100000 || iPincode > 999999 || iPincode % 1000 == 0 )
            return null;
        int prefix2digit = new Double(iPincode * 1d / 10000).intValue();
        String retVal = null;
        if(sPincode.startsWith("744"))
            retVal = "Andaman and Nicobar Islands";
        else if(sPincode.startsWith("3962"))
            retVal = "Dadra and Nagar Haveli";
        else if(sPincode.startsWith("396") || sPincode.startsWith("362"))
            retVal = "Daman and Diu";
        else if(sPincode.startsWith("799"))
            retVal = "Tripura";
        else if(sPincode.startsWith("799"))
            retVal = "Tripura";
        else if(sPincode.startsWith("403"))
            retVal = "Goa";
        else if(sPincode.startsWith("682"))
            retVal = "Lakshadweep";
        else if(sPincode.startsWith("795"))
            retVal = "Manipur";
        else if(sPincode.startsWith("796"))
            retVal = "Mizoram";
        else if(sPincode.startsWith("797"))
            retVal = "Nagaland";
        else if(sPincode.startsWith("793") || sPincode.startsWith("794") || sPincode.startsWith("783"))
            retVal = "Meghalaya";
        else if(sPincode.startsWith("737"))
            retVal = "Sikkim";
        else if(sPincode.startsWith("605") || sPincode.startsWith("607") || sPincode.startsWith("609"))
            retVal = "Pondicherry";
        else if(prefix2digit == 11)
            retVal = "Delhi";
        else if(prefix2digit >= 12 && prefix2digit <= 13)
            retVal = "Haryana";
        else if(prefix2digit >= 14 && prefix2digit <= 15)
            retVal = "Punjab";
        else if(prefix2digit >= 16 && prefix2digit <= 16)
            retVal = "Chandigarh";
        else if(prefix2digit >= 17 && prefix2digit <= 17)
            retVal = "Himachal Pradesh";
        else if(prefix2digit >= 18 && prefix2digit <= 19)
            retVal = "Jammu and Kashmir";
        else if(prefix2digit >= 20 && prefix2digit <= 28)
            retVal = "Uttar Pradesh"; // Uttarakhand
        else if(prefix2digit >= 30 && prefix2digit <= 34)
            retVal = "Rajasthan";
        else if(prefix2digit >= 36 && prefix2digit <= 39)
            retVal = "Gujarat";
        else if(prefix2digit >= 40 && prefix2digit <= 44)
            retVal = "Maharashtra";
        else if(prefix2digit >= 45 && prefix2digit <= 48)
            retVal = "Madhya Pradesh";
        else if(prefix2digit >= 49 && prefix2digit <= 49)
            retVal = "Chhattisgarh";
        else if(prefix2digit >= 50 && prefix2digit <= 53)
            retVal = "Andhra Pradesh"; //Telangana
        else if(prefix2digit >= 56 && prefix2digit <= 59)
            retVal = "Karnataka";
        else if(prefix2digit >= 60 && prefix2digit <= 64)
            retVal = "Tamil Nadu";
        else if(prefix2digit >= 67 && prefix2digit <= 69)
            retVal = "Kerala";
        else if(prefix2digit >= 70 && prefix2digit <= 74)
            retVal = "West Bengal";
        else if(prefix2digit >= 75 && prefix2digit <= 77)
            retVal = "Odisha";
        else if(prefix2digit >= 78 && prefix2digit <= 78)
            retVal = "Assam";
        else if(prefix2digit >= 79 && prefix2digit <= 79)
            retVal = "Arunachal Pradesh";
        else if(prefix2digit >= 80 && prefix2digit <= 85)
            retVal = "Bihar"; 		//Jharkhand
        else if(prefix2digit >= 99 && prefix2digit <= 99)
            retVal = "APO Address";
        log.debug("Determined State {} from Pincode {}", retVal, sPincode);
        return getSanitizedArea(retVal);
    }

    private static String getSanitizedArea(String area) {
        return StringUtils.hasText(area) ? area.toUpperCase().replaceAll(" & ", " AND ") : null;
    }

}
