package com.example.crudapp.security;

import com.example.crudapp.Entity.User;
import com.example.crudapp.service.CustomUserDetails;
import com.example.crudapp.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserService userService){
        return username -> {
            User user = userService.findByEmail(username);
            if(user == null){
                throw new UsernameNotFoundException("UserName not found: "+username);
            }

            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_"+user.getRole());

            UserDetails ud = new CustomUserDetails(
                    user.getEmail(),
                    user.getPassword(),
                    List.of(authority),
                    user.getRole()
            );
                    return ud;
        };

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/employee/login",
                                         "/employee/register",
                                         "/employee/registerUser",
                                         "/",
                                         "/index.html",
                                         "/forms.css").permitAll()
                .requestMatchers("/employee/list").hasAnyRole("EMPLOYEE","ADMIN","HR","MANAGER")
                .requestMatchers("/employee/showFormAdd").hasAnyRole("HR","ADMIN")
                .requestMatchers("/employee/showFormUpdate").hasAnyRole("ADMIN","HR","MANAGER")
                .requestMatchers("/employee/deleteEmployee").hasAnyRole("ADMIN","MANAGER")
                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/employee/login")
                        .loginProcessingUrl("/employee/loginUser")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/employee/list",true)
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll);
        return http.build();
    }
}
