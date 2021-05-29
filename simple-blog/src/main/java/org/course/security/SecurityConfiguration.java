package org.course.security;

import org.course.configuration.BlogConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    private final BlogConfiguration blogConfiguration;

    private final JwtTokenVerifier jwtTokenVerifier;

    public SecurityConfiguration(@Qualifier("daoUserDetailsService") UserDetailsService userDetailsService,
                                 BlogConfiguration blogConfiguration, JwtTokenVerifier jwtTokenVerifier) {
        this.userDetailsService = userDetailsService;
        this.blogConfiguration = blogConfiguration;
        this.jwtTokenVerifier = jwtTokenVerifier;
    }

    public JwtUsernameAndPasswordAuthenticationFilter jwtUsernameAndPasswordAuthenticationFilter() throws Exception {
        return new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), blogConfiguration);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(jwtUsernameAndPasswordAuthenticationFilter())
                .addFilterAfter(jwtTokenVerifier, JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
//                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .antMatchers("/moderate/posts").authenticated()
                .antMatchers("/votes/**").authenticated()
                .antMatchers(HttpMethod.POST, "/comments/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/comments/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/comments/**").authenticated()
                .antMatchers("/images/upload").authenticated()
                .antMatchers("/posts/user/**").authenticated()
                .antMatchers("/images/header/upload").authenticated()
                .antMatchers(HttpMethod.POST, "/users/**").permitAll()
                .antMatchers("/users/activate/**").permitAll()
                .antMatchers("/users/**").authenticated()
                .antMatchers("/users/avatar/**").authenticated()
                .anyRequest().permitAll();

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        auth.authenticationProvider(provider);
    }
}
