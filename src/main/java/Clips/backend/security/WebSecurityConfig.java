package Clips.backend.security;

import Clips.backend.security.jwt.AuthEntryPointJwt;
import Clips.backend.security.jwt.AuthTokenFilter;
import Clips.backend.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Config for web security including HttpSecurity and AuthenticationManagerBuilder,
// using UserService and BCryptPasswordEncoder
@Configuration
// Provides Aspect-Oriented Programming security on methods.
// It enables @PreAuthorize, @PostAuthorize
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    // Spring Security will load User details to perform authentication & authorization.
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Configure:
        http
            // Enable Cross-Origin Resource Sharing,
            // this is a communication between two servers
            .cors().and()
            // Disable cross-site request forgery protection
            // because we are using our token mechanism.
            // In a successful CSRF attack,
            // the attacker might be able to gain full control over the user's account,
            // and might cause the victim user to carry out an action unintentionally.
            .csrf().disable()
            // we use unauthorizedHandler to handle the case for unauthorized entry exception.
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
            // No session will be created or used by Spring Security (STATELESS)
            // since HTTP requests are stateless. We are using token to store the user's
            // state so that it is transferred to the server in every HTTP request to
            // authenticate the user.
            // [LINK] https://sherryhsu.medium.com/session-vs-token-based-authentication-11a6c5ac45e4
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            // Permit all request for the following paths:
            .authorizeRequests().antMatchers("/api/v*/auth/**").permitAll()
            .antMatchers("/api/v*/clip/db/getClips/**").permitAll()
            .antMatchers("/api/v*/clip/db/getClipByDocID/**").permitAll()
            .antMatchers("/api/v*/clip/storage/getFile/**").permitAll()
            // All others need authentication:
            .anyRequest().authenticated();

        // Set authentication Provider:
        http.authenticationProvider(authenticationProvider());

        // We want to process the JSON Web Token authentication filter
        // before the username password authentication filter
        // because we have created a UsernamePasswordAuthenticationToken in
        // authenticationJwtTokenFilter
        http.addFilterBefore(
            authenticationJwtTokenFilter(),
            UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    // AuthenticationManager is a ProviderManager that configured
    // to use an AuthenticationProvider of type DaoAuthenticationProvider
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // DaoAuthenticationProvider is an AuthenticationProvider implementation
    // that leverages a UserDetailsService and PasswordEncoder
    // to authenticate a username and password.
    // DaoAuthenticationProvider will looks up the UserDetails from the UserDetailsService
    // and then uses the PasswordEncoder to validate the password on the UserDetails.
    // [LINK] https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/dao-authentication-provider.html#:~:text=DaoAuthenticationProvider%20is%20an%20AuthenticationProvider%20implementation,authenticate%20a%20username%20and%20password.
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        // Create a DaoAuthenticationProvider object:
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        // Set user detail service and password encoder:
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
