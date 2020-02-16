package com.metalheart.test.integration.rest;

import com.metalheart.security.DelayedTaskFilter;
import com.metalheart.test.integration.BaseIntegrationTest;
import java.util.List;
import javax.servlet.Filter;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.filter.CorsFilter;

import static org.junit.Assert.assertTrue;


@EnableAutoConfiguration
public class SecurityConfigurationIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private FilterChainProxy filterChainProxy;

    @Test
    public void securityTest() {

        SecurityFilterChain chain = filterChainProxy.getFilterChains().get(0);

        List<Filter> filters = chain.getFilters();

        var i = 0;
        assertTrue(filters.get(i++) instanceof WebAsyncManagerIntegrationFilter);
        assertTrue(filters.get(i++) instanceof SecurityContextPersistenceFilter);
        assertTrue(filters.get(i++) instanceof HeaderWriterFilter);
        assertTrue(filters.get(i++) instanceof CorsFilter);
        assertTrue(filters.get(i++) instanceof LogoutFilter);
        assertTrue(filters.get(i++) instanceof OAuth2AuthorizationRequestRedirectFilter);
        assertTrue(filters.get(i++) instanceof OAuth2LoginAuthenticationFilter);
        assertTrue(filters.get(i++) instanceof BasicAuthenticationFilter);
        assertTrue(filters.get(i++) instanceof RequestCacheAwareFilter);
        assertTrue(filters.get(i++) instanceof SecurityContextHolderAwareRequestFilter);
        assertTrue(filters.get(i++) instanceof DelayedTaskFilter);
        assertTrue(filters.get(i++) instanceof AnonymousAuthenticationFilter);
        assertTrue(filters.get(i++) instanceof SessionManagementFilter);
        assertTrue(filters.get(i++) instanceof ExceptionTranslationFilter);
        assertTrue(filters.get(i++) instanceof FilterSecurityInterceptor);
    }
}
