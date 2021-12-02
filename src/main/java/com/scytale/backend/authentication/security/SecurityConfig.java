package com.scytale.backend.authentication.security;

import com.scytale.backend.authentication.filter.CustomAuthenticationFilter;
import com.scytale.backend.authentication.filter.CustomAuthorizationFilter;
import com.scytale.backend.authentication.model.ERole;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/auth/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeHttpRequests().antMatchers("/auth/**").permitAll();
        http.authorizeHttpRequests().antMatchers(GET,"/admin/**").hasAnyAuthority(ERole.ROLE_ADMIN.name());
        http.authorizeHttpRequests().antMatchers(GET,"/admin/user/**").hasAnyAuthority(ERole.ROLE_ADMIN.name());
        http.authorizeHttpRequests().antMatchers(POST,"/admin/**").hasAnyAuthority(ERole.ROLE_ADMIN.name());
        http.authorizeHttpRequests().antMatchers(POST,"/admin/user/**").hasAnyAuthority(ERole.ROLE_ADMIN.name());
        http.authorizeHttpRequests().anyRequest().hasAuthority(ERole.ROLE_USER.name());
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
