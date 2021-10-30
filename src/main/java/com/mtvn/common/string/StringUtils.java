package com.mtvn.common.string;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.validator.routines.EmailValidator;

@Slf4j
@Component
public class StringUtils extends org.springframework.util.StringUtils {

    @Value("${mt.pattern.mobile:^[987654]{1}[\\d]{8,9}$}") private  String MOBILE_PATTERN_REGEX;

    private static final char SPACE = ' ';
    private static final Pattern WORDS = Pattern.compile("[a-zA-Z]+");
    private static final Pattern PATTERN_PAN = Pattern.compile("^[a-zA-Z]{3}[pP]{1}[a-zA-Z]{1}[0-9]{4}[a-zA-Z]{1}$");
    private static Pattern PATTERN_MOBILE = Pattern.compile("^[987654]{1}[\\d]{9}$");
    private static final Pattern PATTERN_EMAIL = Pattern.compile("^[^@]+@[^@]+\\.[a-zA-Z0-9_\\-]{2,20}$");
    private static final Pattern PATTERN_VIETNAM_NATIONAL_ID = Pattern.compile("^[0-9]{9,12}$");

    private final static String ANDROID_SOURCE = "moneytap";
    private final static String IOS_SOURCE = "moneytap_ios";

    @PostConstruct
    private void loadMobilePattern() {

        PATTERN_MOBILE = Pattern.compile(MOBILE_PATTERN_REGEX);

    }

    public static boolean isFakeEmail(String email){
        if(email == null){
            return true;
        }
        String[] split = email.split("@");
        String emailFirstPart = split[0];
        return (emailFirstPart ==null || PATTERN_MOBILE.matcher(emailFirstPart.trim()).matches())
                && !isValidEmail(email);
    }


    public static String trimUpto(String s, int maxLength) {
        if(!hasText(s))
            return s;
        if(s.length() <= maxLength)
            return s;
        return s.substring(0, maxLength - 1);
    }

    public static String sanitizeTextField(String text) {
        if(hasText(text)) {
            text = org.apache.commons.lang3.StringUtils.normalizeSpace(text);
            text = text.replaceAll("\\r\\n|\\r|\\n", " ");
            text = text.replaceAll("\\t", " ");
            text = text.trim();
        }
        return hasText(text) ? text : null;
    }

    public static String leftPad(String s, int size, String padChar) {
        return org.apache.commons.lang3.StringUtils.leftPad(s, size, padChar);
    }

    public static boolean isValidPAN(String pan) {
        return StringUtils.hasText(pan)
                && PATTERN_PAN.matcher(pan).matches();
    }

    public static boolean isValidVietnamNationalId(String nationalId) {
        return StringUtils.hasText(nationalId)
                && PATTERN_VIETNAM_NATIONAL_ID.matcher(nationalId).matches();
    }

    public static boolean isValidPhone(String contactNumber) {
        return StringUtils.hasText(contactNumber)
                && PATTERN_MOBILE.matcher(contactNumber.trim()).matches();
    }

    public static boolean isValidEmail(String emailId) {
        return StringUtils.hasText(emailId)
                && EmailValidator.getInstance().isValid(emailId)
                && emailId.length() <= 70;
    }

    public static Set<String> getWordsFromString(String address) {
        Set<String> retVal = new HashSet<>();
        Matcher m = WORDS.matcher(address);
        while(m.find()) {
            retVal.add(m.group());
        }
        return retVal;
    }

    public static boolean matches(Set<String> words1, Set<String> words2,
                                  float matchAccuracy, float wordMatchAccuracy, boolean matchNoOfWords) {
        Set<String> smaller = words1.size() <= words2.size() ? words1 : words2;
        Set<String> larger = words1.size() <= words2.size() ? words2 : words1;
        if(matchNoOfWords && smaller.size() != larger.size()) {
            return false;
        }
        int matchingWords = 0;
        for(String searchTerm : smaller) {
            if(larger.contains(searchTerm)) {
                matchingWords++;
                continue;
            }else {
                for(String searchIn : larger) {
                    int distance = getLevenshteinDistance(searchTerm, searchIn);
                    float matchPercentage = 100f - distance*1f/searchTerm.length()*100;
                    if(matchPercentage > wordMatchAccuracy) {
                        matchingWords++;
                        if(matchingWords == smaller.size())
                            break;
                    }
                }
            }
        }
        float wordMatchPercentage = matchingWords*1f/smaller.size()*100;
        return wordMatchPercentage > matchAccuracy;
    }

    public static boolean matches(Set<String> words1, Set<String> words2, float matchAccuracy, float wordMatchAccuracy) {
        return matches(words1, words2, matchAccuracy, wordMatchAccuracy, false);
    }


    /**
     * https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
     * @param lhs
     * @param rhs
     * @return
     */
    public static int getLevenshteinDistance(CharSequence lhs, CharSequence rhs) {
        int len0 = lhs.length() + 1;
        int len1 = rhs.length() + 1;
        // the array of distances
        int[] cost = new int[len0];
        int[] newcost = new int[len0];
        // initial cost of skipping prefix in String s0
        for (int i = 0; i < len0; i++) cost[i] = i;
        // dynamically computing the array of distances
        // transformation cost for each letter in s1
        for (int j = 1; j < len1; j++) {
            // initial cost of skipping prefix in String s1
            newcost[0] = j;
            // transformation cost for each letter in s0
            for(int i = 1; i < len0; i++) {
                // matching current letters in both strings
                int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;
                // computing cost for each transformation
                int cost_replace = cost[i - 1] + match;
                int cost_insert  = cost[i] + 1;
                int cost_delete  = newcost[i - 1] + 1;
                // keep minimum cost
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }
            // swap cost/newcost arrays
            int[] swap = cost; cost = newcost; newcost = swap;
        }
        // the distance is the cost for transforming all letters in both strings
        return cost[len0 - 1];
    }

    public static String sanitizeAddressForBureau(String sAddress) {
        if(StringUtils.hasText(sAddress)) {
            sAddress = sAddress.replaceAll("[^a-zA-Z0-9 ./,-]", " ");
            sAddress = sAddress.replaceAll(" ,", ",");
            sAddress = sAddress.replaceAll(",", ", ");
            sAddress = sAddress.replaceAll(" \\.", ".");
            sAddress = sAddress.replaceAll("\\.", ". ");
            sAddress = sAddress.replaceAll("&", " and ");
        }
        return StringUtils.sanitizeTextField(sAddress);
    }

    public static boolean isCompressed(final String s) {
        byte[] compressed = s.getBytes();
        return (compressed[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) && (compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
    }

    /**
     * Encoding the data to Base64 to avoid platform dependency on  bytes.
     * Not every byte[] can be converted to a string, and the conversion back could give other bytes.
     *
     * @param data
     * @return
     */
    public static String compress(String data) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length());
            GZIPOutputStream gzip = new GZIPOutputStream(bos);
            gzip.write(data.getBytes(StandardCharsets.UTF_8));
            gzip.close();
            byte[] compressed = bos.toByteArray();
            bos.close();
            return Base64.encodeBase64String(compressed);
        } catch (Exception e) {
            log.error("Unable to compress the given data", e);
        }
        return null;
    }

    /**
     * Decoding the data to Base64  as zip is using base64 conversion.
     *
     * @param data
     * @return
     */
    public static String decompress(String data) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(Base64.decodeBase64(data));
            GZIPInputStream gis = new GZIPInputStream(bis);
            byte[] bytes = IOUtils.toByteArray(gis);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Unable to decompress the given data", e);
        }
        return null;
    }

    public static String removeAllNonAlphaNumeric(String text) {
        if(StringUtils.hasText(text))
            text = text.toLowerCase().replaceAll("[^a-z0-9\\- ]", "").replaceAll("\\s+", " ").trim();
        return text;
    }

    /**
     * @Author Aniruddha
     * This method replaces non alphabet characters with spaces and after that removes consecutive spaces
     * @param inputString String from which non-alphabets needs to be removed
     * @return Trimmed string with only alphabets and spaces
     */
    public static String removeNonAlphabets(final String inputString) {
        final char[] charArray = inputString.trim().toCharArray();
        final int trimmedStringLength = charArray.length;

        char[] replacedWithSpace = new char[trimmedStringLength];

        for (int i = 0; i < trimmedStringLength; ++i) {
            char currChar = charArray[i];
            if (!isAlphabetOrSpace(currChar)) {
                replacedWithSpace[i] = SPACE;
            } else {
                replacedWithSpace[i] = charArray[i];
            }
        }

        StringBuilder sBuilder = new StringBuilder(trimmedStringLength);
        for (int i = 0; i < trimmedStringLength; ++i) {
            if (i > 0 && replacedWithSpace[i-1] == SPACE && replacedWithSpace[i] == SPACE) {
                continue;
            }
            sBuilder.append(replacedWithSpace[i]);
        }
        return sBuilder.toString().trim();
    }

    private static boolean isAlphabetOrSpace(char c) {
        return (c == SPACE || (Character.toUpperCase(c) >= 65 && Character.toUpperCase(c) <= 90));
    }

//    public static MoengageIdType getMoengageIdType(String customerSource) {
//
//        return (customerSource.equalsIgnoreCase(ANDROID_SOURCE) || customerSource.equalsIgnoreCase(IOS_SOURCE) || customerSource.startsWith("cb_")) ? MoengageIdType.EMAIL : MoengageIdType.PHONE;
//
//    }

    public static String maskString(String strText, int start, int end, char maskChar) {

        if(strText == null || strText.equals(""))
            return "";

        if(start < 0)
            start = 0;

        if( end > strText.length() )
            end = strText.length();

        if(start > end)
            throw new RuntimeException("End index cannot be greater than start index");

        int maskLength = end - start;

        if(maskLength == 0)
            return strText;

        StringBuilder sbMaskString = new StringBuilder(maskLength);

        for(int i = 0; i < maskLength; i++){
            sbMaskString.append(maskChar);
        }

        return strText.substring(0, start)
                + sbMaskString.toString()
                + strText.substring(start + maskLength);
    }

}
