package com.example.project.config;


import com.example.project.model.enums.UserRoleEnum;
import com.example.project.repository.UserRepository;
import com.example.project.service.ApplicationUserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
public class SecurityConfiguration {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http,
      SecurityContextRepository securityContextRepository) throws Exception {

    http
        .authorizeHttpRequests(
            authorizeHttpRequests ->
                authorizeHttpRequests.
                    requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                    .permitAll().
                    requestMatchers("/", "/users/login", "/users/register", "/users/login-error")
                    .permitAll().
                    requestMatchers("/categories/all").hasRole(UserRoleEnum.MODERATOR.name()).
                    requestMatchers("/admin").hasRole(UserRoleEnum.ADMIN.name()).
                    anyRequest().authenticated()
        )
        .formLogin(
            (formLogin) ->
                formLogin.
                    loginPage("/users/login").
                    usernameParameter(
                        UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY).
                    passwordParameter(
                        UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY).
                    defaultSuccessUrl("/").
                    failureForwardUrl("/users/login-error")
        )
        .logout((logout) ->
            logout.logoutUrl("/users/logout").
                logoutSuccessUrl("/").
                invalidateHttpSession(true)
        ).securityContext(
            securityContext -> securityContext.
                securityContextRepository(securityContextRepository)
        );

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserDetailsService userDetailsService(UserRepository userRepository) {
    return new ApplicationUserDetailsService(userRepository);
  }

  @Bean
  public SecurityContextRepository securityContextRepository() {
    return new DelegatingSecurityContextRepository(
        new RequestAttributeSecurityContextRepository(),
        new HttpSessionSecurityContextRepository()
    );
  }

}
