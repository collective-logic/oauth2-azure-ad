package uk.co.logic.collective.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * AADOAuth2LoginSecurityConfig
 *
 * Spring Security Configuration
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AADOAuth2LoginSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${azure.activedirectory.tenant-id:}")
    private String tenantId;

    private static final String ALLOWED_ROLES = "AUTH_POC_1, AUTH_POC_2";
    private static final String LOG_IN_PAGE = "/oauth2/authorization/azure";
    private static final String LOG_OUT_URL = "https://login.microsoftonline.com/%s/oauth2/logout";

    private static final String LOGIN_COOKIE = "JSESSIONID";
    private static final String LOG_OUT_PATH = "/logout";

    @Autowired
    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/*").hasAnyRole(ALLOWED_ROLES)
            .anyRequest().authenticated()
            .and()
                .oauth2Login()
                .userInfoEndpoint()
                .oidcUserService(oidcUserService)
            .and()
                .loginPage(LOG_IN_PAGE)
            .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher(LOG_OUT_PATH))
                .clearAuthentication(true)
                .logoutSuccessUrl(String.format(LOG_OUT_URL, tenantId))
                .deleteCookies(LOGIN_COOKIE)
                .invalidateHttpSession(true)
                ;
            }
}
