package com.axway.runners;

import com.azure.spring.cloud.autoconfigure.aad.AadWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

@EnableGlobalMethodSecurity(securedEnabled = true,
    prePostEnabled = true)
@EnableWebSecurity
public class WebSecurityConfig extends AadWebSecurityConfigurerAdapter {
    @Autowired
    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        super.configure(http);
        http
            .csrf().disable()
            .exceptionHandling()
            .and()
            .headers()
            .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
            .and()
            .frameOptions().sameOrigin()
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .antMatchers("/app/**").permitAll()
            .antMatchers("/api/auth-info").permitAll()
            .antMatchers("/v2/api-docs").permitAll()
            .antMatchers("/css/**").permitAll()
            .antMatchers("/js/**").permitAll()
            .antMatchers("/api/**").authenticated()
            .antMatchers("/").authenticated()
            .antMatchers("/callback/**").permitAll()
            .antMatchers("/callback").permitAll()
            .antMatchers("/management/health").permitAll()
            .antMatchers("/management/events/**").permitAll()
            .antMatchers("/management/info").permitAll()
            .antMatchers("/management/prometheus").permitAll()
            .antMatchers("/index.html").authenticated()
            .and()
            .oauth2Login()
            .defaultSuccessUrl("/")
            .userInfoEndpoint()
            .oidcUserService(oidcUserService);
    }
}
