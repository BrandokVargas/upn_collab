package com.api_colllab.api_collab.config;


import com.api_colllab.api_collab.config.Filter.JwtTokenValidator;
import com.api_colllab.api_collab.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtUtils jwtUtils;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //Trabajamos con el patron builder
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    //PUBLICS ENDPOINT
                    http.requestMatchers(HttpMethod.POST, "/auth/**").permitAll();
                    http.requestMatchers(HttpMethod.GET,"/api/test/pong").permitAll();
                    http.requestMatchers(HttpMethod.GET,"/api/careers").permitAll();

                    /////////////////////////////////////////////////////////////////////////////////
                    //PRIVATE ENDPOINT
                    //ADMIN
                    http.requestMatchers(HttpMethod.GET, "/api/all-forum-pending").hasAnyRole("ADMIN");
                    http.requestMatchers(HttpMethod.PUT, "/api/update-state/{forumId}").hasAnyRole("ADMIN");

                    //USERS
                    http.requestMatchers(HttpMethod.POST, "/api/add-forum").hasAnyRole("USER","ADMIN");
                    http.requestMatchers(HttpMethod.GET, "/api/all-forum").hasAnyRole("USER","ADMIN");
                    http.requestMatchers(HttpMethod.GET, "/api/type-forums").hasAnyRole("USER","ADMIN");
                    http.requestMatchers(HttpMethod.GET, "/api/my-forums").hasAnyRole("USER","ADMIN");
                    http.requestMatchers(HttpMethod.POST, "/api/add-favorite").hasAnyRole("USER","ADMIN");
                    http.requestMatchers(HttpMethod.DELETE, "/api/remove-favorite/{foroId}").hasAnyRole("USER","ADMIN");
                    http.requestMatchers(HttpMethod.GET, "/api/my-forums-favorites").hasAnyRole("USER","ADMIN");
                    http.requestMatchers(HttpMethod.POST, "/api/add-comment").hasAnyRole("USER","ADMIN");
                    http.requestMatchers(HttpMethod.GET, "/api/comments/{id_forum}").hasAnyRole("USER","ADMIN");
                    http.requestMatchers(HttpMethod.GET, "/api/all-reactions").hasAnyRole("USER","ADMIN");
                    http.requestMatchers(HttpMethod.GET, "/api/entry-forum-comment/{id_forum}").hasAnyRole("USER","ADMIN");
                    http.requestMatchers(HttpMethod.POST, "/api/register-device").hasAnyRole("USER","ADMIN");
                    http.requestMatchers(HttpMethod.POST, "/api/sendNotification/{id_user}").hasAnyRole("USER","ADMIN");
                    http.requestMatchers(HttpMethod.POST, "/api/sendAllNotification").hasAnyRole("USER","ADMIN");
                    http.requestMatchers(HttpMethod.GET, "/api/search-forum/{text}").hasAnyRole("USER","ADMIN");
                    http.requestMatchers(HttpMethod.GET, "/api/search-my-forum/{text}").hasAnyRole("USER","ADMIN");
                    http.requestMatchers(HttpMethod.GET, "/api/profile").hasAnyRole("USER","ADMIN");
                    http.requestMatchers(HttpMethod.PUT, "/api/update-nivel").hasAnyRole("USER","ADMIN");

                    //TEST ENDPOINTS
                    http.requestMatchers(HttpMethod.GET, "/api/test/pong_users").hasAnyRole("USER");
                    http.requestMatchers(HttpMethod.GET, "/api/test/pong_admin").hasAnyRole("ADMIN");

                    //hasAnyAuthority
                    http.anyRequest().denyAll();
                })
                .addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
