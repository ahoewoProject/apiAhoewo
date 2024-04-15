package com.memoire.apiAhoewo.securities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.memoire.apiAhoewo.filters.CustomAuthenticationFilter;
import com.memoire.apiAhoewo.filters.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    protected void configure(HttpSecurity http) throws Exception{
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
        http.csrf().disable();
        http.cors();
        //http.authorizeRequests()
             //   .anyRequest().permitAll();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/api/register/**",
                "/api/login/**",
                "/api/refresh-token/**",
                "/api/roles/**",
                "/api/request-reset-password",
                "/api/reset-password",
                "/api/cni/demande-certification/{id}/**",
                "/api/carte-cfe/demande-certification/{id}/**",
                "/api/logo/agence-immobiliere/{id}/**",
                "/api/image/bien-immobilier/{id}/**",
                "/api/images/bien-immobilier/{id}/**",
                "/api/premiere-image/bien-immobilier/{id}/**",
                "/api/caracteristiques/bien-immobilier/{id}/**",
                "/api/types-de-bien/location/**",
                "/api/types-de-bien/vente/**",
                "/api/types-de-bien/actifs/**",
                "/api/pays/actifs/**",
                "/api/regions/actifs/**",
                "/api/villes/actifs/**",
                "/api/quartiers/actifs/**",
                "/api/regions/pays/{id}/**",
                "/api/villes/region/{id}/**",
                "/api/quartiers/ville/{id}/**",
                "/api/agences-immobilieres/actives/**",
                "/api/agence/{nomAgence}/**",
                "/api/services/agence/{nomAgence}/**",
                "/api/agences/actives/region/{id}/**",
                "/api/agences/actives/ville/{id}/**",
                "/api/agences/actives/quartier/{id}/**",
                "/api/publications/actives/**",
                "/api/publications/actives/recherche-simple/**",
                "/api/publications/actives/recherche-avancee/**",
                "/api/publications/actives/region/{libelle}/**",
                "/api/publications/actives/type-de-bien/{designation}/**",
                "/api/publications/actives/region-list/**",
                "/api/publications/actives/type-de-bien-list/**",
                "/api/publication/{id}/**",
                "/api/publication/code/{code}/**",
                "/api/contrat-location/generer-pdf/{id}/**",
                "/api/contrat-vente/generer-pdf/{id}/**",
                "/api/contactez-nous/**").permitAll();
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
