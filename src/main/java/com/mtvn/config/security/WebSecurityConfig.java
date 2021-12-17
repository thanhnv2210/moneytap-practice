package com.mtvn.config.security;

import com.netflix.config.DynamicPropertyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled=true, proxyTargetClass=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    //@Autowired private OAuthUserDetailService userDetailsService;
    /*@Autowired private MwynAuthenticationProvider authProvider;*/

    /**
     * Register authentication provider service in spring context and
     * register authenticationManager bean in spring context
     *
     * @param auth
     * @throws Exception
     */
//    @Autowired
//    protected void registerAuthentication(
//            final AuthenticationManagerBuilder auth) throws Exception {
//        /*if (isGoogleOauth2Enabled()) {
//            auth.authenticationProvider(authProvider);
//        } else {
//            auth.userDetailsService(userDetailsService);
//        }*/
//        //auth.userDetailsService(userDetailsService);
//    }

//    @Override
//    @Bean
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

    /**
     * The configure handle all incoming request and take decision whether
     * the request proceed further or not
     * All ignoring url pattern defined here
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        String[] urlsToBeIgnored = getNoOauthUrls().split(",");
        for (String url : urlsToBeIgnored) {
            log.info("OAUTH IGNORED URL = " + url.trim());
            web.ignoring().antMatchers(url.trim());
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] urlsToBeIgnored = getNoOauthUrls().split(",");
        List<String> urls = new ArrayList<>();
        for(String sURL : urlsToBeIgnored) {
            urls.add(sURL.trim());
        }
        http
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers(urls.toArray(new String[] {})).permitAll()
                .and()
                .logout()
                .permitAll();
    }

    public static String getNoOauthUrls()  {
        return DynamicPropertyFactory.getInstance().getStringProperty("noOauthUrls", "").getValue();
    }

    public static boolean isGoogleOauth2Enabled() {
        return DynamicPropertyFactory.getInstance().getBooleanProperty("isGoogleOauth2Enabled", false).getValue();
    }
}
