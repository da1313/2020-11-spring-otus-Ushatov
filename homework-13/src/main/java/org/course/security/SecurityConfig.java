package org.course.security;

import org.course.domain.DbPermissionEnum;
import org.course.domain.DbRoleEnum;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    private final AccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(@Qualifier("daoUserDetailsService") UserDetailsService userDetailsService,
                          @Qualifier("customAccessDeniedHandler") AccessDeniedHandler accessDeniedHandler) {
        this.userDetailsService = userDetailsService;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/manage").hasAuthority(DbPermissionEnum.BOOK_MANAGE.name())
                .antMatchers(HttpMethod.GET, "/manage/book/create").hasAuthority(DbPermissionEnum.BOOK_CREATE.name())
                .antMatchers(HttpMethod.POST, "/manage/book/create").hasAuthority(DbPermissionEnum.BOOK_CREATE.name())
                .antMatchers(HttpMethod.GET, "/manage/book/update/*").hasAuthority(DbPermissionEnum.BOOK_UPDATE.name())
                .antMatchers(HttpMethod.POST, "/manage/book/update/*").hasAuthority(DbPermissionEnum.BOOK_UPDATE.name())
                .antMatchers(HttpMethod.POST, "/manage/book/delete/*").hasAuthority(DbPermissionEnum.BOOK_DELETE.name())
                .antMatchers("/comments").hasRole(DbRoleEnum.USER.name())
                .antMatchers("/scores").hasRole(DbRoleEnum.USER.name())
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login")
                .and().logout().logoutSuccessUrl("/login")
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(getPasswordEncoder());
        provider.setUserDetailsService(userDetailsService);
        auth.authenticationProvider(provider);
    }

}
