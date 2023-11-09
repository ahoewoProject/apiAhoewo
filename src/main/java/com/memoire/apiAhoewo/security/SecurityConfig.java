package com.memoire.apiAhoewo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.memoire.apiAhoewo.filter.CustomAuthenticationFilter;
import com.memoire.apiAhoewo.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    protected void configure(HttpSecurity http) throws Exception{
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
        http.csrf().disable();
        http.cors();
        //http.authorizeRequests()
             //   .anyRequest().permitAll();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/api/register/**",
                "/api/login/**", "/api/refresh-token/**",
                "/api/roles/**", "/api/request-reset-password",
                "/api/reset-password", "/api/document/demande-certification/{id}/**",
                "/api/logo/agence-immobiliere/{id}/**", "/api/image/bien-immobilier/{id}/**",
                "/api/image-principale/bien-immobilier/{id}/**").permitAll();
        //http.authorizeRequests().antMatchers(GET,"/api/user/**").hasAnyAuthority("ROLE_USER");
        //http.authorizeRequests().antMatchers(POST,"/api/user/save/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().anyRequest().authenticated();
        http.exceptionHandling()
                .authenticationEntryPoint((request, response, e) -> {
                    // Personnalisez le message d'accès refusé ici
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    OutputStream out = response.getOutputStream();
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("message", "Accès non autorisé.");
                    mapper.writeValue(out, errorResponse);
                    out.flush();
                });
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }
}
