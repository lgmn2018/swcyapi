package com.lgmn.swcyapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import javax.servlet.http.HttpServletResponse;

/**
 * 〈OAuth资源服务配置〉
 *
 * @author Curise
 * @create 2018/12/14
 * @since 1.0.0
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .requestMatchers().antMatchers(
                                      "/auth/**/api/**",
                                                    "/home/getHomePage",
                                                    "/order/*",
                                                    "/person/*",
                                                    "/store/getShopTypeAndEssentialMessage",
                                                    "/store/addStoreForUnlicensed",
                                                    "/store/addStore",
                                                    "/store/getMyStorePage",
                                                    "/store/followAndCleanFollow",
                                                    "/store/getMyFollowPage",
                                                    "/store/createLeagueStore",
                                                    "/store/leagueStoreAddCommodity",
                                                    "/store/delStoreById",
                                                    "/wxApi/wxPay",
                                                    "/wxApi/supplierUnifiedOrderWxPay",
                                                    "/supplier/getSupplierOrderPage",
                                                    "/supplier/supplierOrderConfirmReceipt",
                                                    "/supplier/applyForReturn",
                                                    "/supplier/leagueStoreGetSupplier",
                                                    "/supplier/getLeagueStoreOrderPage",
                                                    "/supplier/getLeagueStoreOrderDetails",
                                                    "/supplier/leagueStoreOrderConfirmReceipt")
                .and()
                .authorizeRequests()
                .antMatchers( "/auth/**/api/**",
                                            "/home/getHomePage",
                                            "/order/*",
                                            "/person/*",
                                            "/store/getShopTypeAndEssentialMessage",
                                            "/store/addStoreForUnlicensed",
                                            "/store/addStore",
                                            "/store/getMyStorePage",
                                            "/store/followAndCleanFollow",
                                            "/store/getMyFollowPage",
                                            "/store/createLeagueStore",
                                            "/store/delStoreById",
                                            "/store/leagueStoreAddCommodity",
                                            "/wxApi/wxPay",
                                            "/wxApi/supplierUnifiedOrderWxPay",
                                            "/supplier/getSupplierOrderPage",
                                            "/supplier/supplierOrderConfirmReceipt",
                                            "/supplier/applyForReturn",
                                            "/supplier/leagueStoreGetSupplier",
                                            "/supplier/getLeagueStoreOrderPage",
                                            "/supplier/getLeagueStoreOrderDetails",
                                            "/supplier/leagueStoreOrderConfirmReceipt").authenticated()
                .and()
                .httpBasic();
    }
}
