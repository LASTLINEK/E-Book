package edu.bjtu.sei.simplecrud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import edu.bjtu.sei.simplecrud.service.UserService;
import org.springframework.web.cors.CorsUtils;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    CustomizeAuthenticationEntryPoint authenticationEntryPoint; // 没登陆

    @Autowired
    private CustomizeAuthenticationSuccessHandler authenticationSuccessHandler; //登陆成功

    @Autowired
    private CustomizeAuthenticationFailureHandler authenticationFailureHandler; // 登陆失败

    @Autowired
    CustomizeLogoutSuccessHandler logoutSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable().authorizeRequests()
                //处理跨域请求中的Preflight请求
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers(
                            "/registration**",
                            "/js/**",
                            "/css/**",
                            "/img/**",
                            "/webjars/**").permitAll()
                    .anyRequest().authenticated()
                //登入
                .and().formLogin().
                    permitAll().//允许所有用户
                        successHandler(authenticationSuccessHandler).//登录成功处理逻辑
                        failureHandler(authenticationFailureHandler)//登录失败处理逻辑
                //异常处理(权限拒绝、登录失效等)
                .and()
                    .exceptionHandling()
                        .authenticationEntryPoint(authenticationEntryPoint)//匿名用户访问无权限资源时的异常处理
                .and()
                    .logout()
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .permitAll()
                    .logoutSuccessHandler(logoutSuccessHandler)
                        .deleteCookies("JSESSIONID");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

}
