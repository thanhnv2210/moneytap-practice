package com.mtvn.config.security;

import com.mtvn.auth.server.model.RBACPermission;
import com.mtvn.auth.server.repository.RBACPermissionDao;
import com.mtvn.auth.server.service.OAuthUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;

import java.util.List;

@Slf4j
@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    public static final String REST_RESOURCE_ID = "mwyn-resource";
    public static final String SCOPE_READ = "read";
    public static final String SCOPE_WRITE = "write";
    public static final String SCOPE_DELETE = "delete";

    @Autowired
    private RBACPermissionDao rbacPermissionDao;
    @Autowired private OAuthUserDetailService userDetailsService;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(REST_RESOURCE_ID).stateless(false);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        configureRBAC(http);
        http
                .userDetailsService(userDetailsService)
                .anonymous()
                .disable();
        http
                .authorizeRequests()
                .antMatchers("/**")
                .fullyAuthenticated();
        http
                .httpBasic()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /**
     * The RBAC setting dynamically load from the database and configured
     *
     * @param http
     * @throws Exception
     */
    private void configureRBAC(HttpSecurity http) throws Exception {
        log.info("Reading rbac permissions from file");
        List<RBACPermission> rbacs = rbacPermissionDao.getRBACPermissionsFromFile();
        for (RBACPermission rbac : rbacs) {
            if (rbac.getScope() != null) {
                if (rbac.getScope().contains(",")) {
                    for (String scope : rbac.getScope().split(",")) {
                        roleAndScopeBasedAccess(rbac, scope, http);
                    }
                } else {
                    roleAndScopeBasedAccess(rbac, rbac.getScope(), http);
                }
            } else {
                // Setting url access permission with out scope
                http.authorizeRequests()
                        .regexMatchers(rbac.getUrl()).access("hasAnyRole(" + rbac.getRolesAsString() + ")");
            }
        }
    }

    /**
     * Function to provide the resource server configuration with the
     * regmax to provide the authorize resource server access in oauth server
     * here scope to validate the authenticated user have the scope or not
     * and the role has been verified whether the authenticated principle have the
     * rights to access the resources. The RBAC setting carried here
     *
     * @param rbac
     * @param scope
     * @param http
     * @throws Exception
     */
    private void roleAndScopeBasedAccess(RBACPermission rbac, String scope, HttpSecurity http) throws Exception{
        log.info("roles {} scope {} url {}", rbac.getRolesAsString(),  rbac.getScope(), rbac.getUrl() );
        if(scope.equalsIgnoreCase(SCOPE_READ)) {
            http
                    .authorizeRequests().antMatchers(HttpMethod.GET, rbac.getUrl()).fullyAuthenticated()
                    .and()
                    .authorizeRequests().antMatchers(HttpMethod.GET, rbac.getUrl()).access("#oauth2.hasScope('read')")
                    .and()
                    .authorizeRequests().antMatchers(HttpMethod.GET, rbac.getUrl()).hasAnyAuthority(rbac.getRolesAsString())
                    .expressionHandler(new OAuth2WebSecurityExpressionHandler());

        } else if(scope.equalsIgnoreCase(SCOPE_WRITE)){
            http
                    .authorizeRequests().antMatchers(HttpMethod.POST, rbac.getUrl()).fullyAuthenticated()
                    .and()
                    .authorizeRequests().antMatchers(HttpMethod.POST, rbac.getUrl()).access("#oauth2.hasScope('write')")
                    .and()
                    .authorizeRequests().antMatchers(HttpMethod.POST, rbac.getUrl()).hasAnyAuthority(rbac.getRolesAsString())
                    .expressionHandler(new OAuth2WebSecurityExpressionHandler());

            http
                    .authorizeRequests().antMatchers(HttpMethod.PUT, rbac.getUrl()).fullyAuthenticated()
                    .and()
                    .authorizeRequests().antMatchers(HttpMethod.PUT, rbac.getUrl()).access("#oauth2.hasScope('write')")
                    .and()
                    .authorizeRequests().antMatchers(HttpMethod.PUT, rbac.getUrl()).hasAnyAuthority(rbac.getRolesAsString())
                    .expressionHandler(new OAuth2WebSecurityExpressionHandler());

        } else if(scope.equalsIgnoreCase(SCOPE_DELETE)) {
            http
                    .authorizeRequests().antMatchers(HttpMethod.DELETE, rbac.getUrl()).fullyAuthenticated()
                    .and()
                    .authorizeRequests().antMatchers(HttpMethod.DELETE, rbac.getUrl()).access("#oauth2.hasScope('delete')")
                    .and()
                    .authorizeRequests().antMatchers(HttpMethod.DELETE, rbac.getUrl()).hasAnyAuthority(rbac.getRolesAsString())
                    .expressionHandler(new OAuth2WebSecurityExpressionHandler());

        }
    }
}
