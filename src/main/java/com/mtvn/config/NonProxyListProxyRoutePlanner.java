package com.mtvn.config;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.protocol.HttpContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class NonProxyListProxyRoutePlanner extends DefaultProxyRoutePlanner {

    private static final Pattern WILDCARD = Pattern.compile("\\*");
    private static final String REGEX_WILDCARD = ".*";

    private List<Pattern> nonProxyHostPatterns;

    public NonProxyListProxyRoutePlanner(HttpHost proxy, Set<String> nonProxyHosts) {
        super(proxy, null);
        nonProxyHostPatterns = getNonProxyHostPatterns(nonProxyHosts);
    }

    private List<Pattern> getNonProxyHostPatterns(Set<String> nonProxyHosts) {
        if (nonProxyHosts == null) {
            return Collections.emptyList();
        }

        final List<Pattern> patterns = new ArrayList<>(nonProxyHosts.size());
        for (String nonProxyHost : nonProxyHosts) {
            // Replaces a wildcard to a regular expression
            patterns.add(Pattern.compile(WILDCARD.matcher(nonProxyHost).replaceAll(REGEX_WILDCARD)));
        }
        return Collections.unmodifiableList(patterns);
    }

    protected List<Pattern> getNonProxyHostPatterns() {
        return nonProxyHostPatterns;
    }
    @Override
    protected HttpHost determineProxy(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
        for (Pattern nonProxyHostPattern : nonProxyHostPatterns) {
            if (nonProxyHostPattern.matcher(target.getHostName()).matches()) {
                return null;
            }
        }
        return super.determineProxy(target, request, context);
    }
}