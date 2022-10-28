package Clips.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class BackendApplicationConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000", // React.js
            "http://localhost:4200"  // Angular
        ));
        corsConfiguration.setAllowedHeaders(Arrays.asList(
            "Accept",
            "Access-Control-Allow-Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers",
            "Authorization",
            "Content-Type",
            "Jwt-Token",
            "Origin",
            "Origin, Accept",
            "X-Requested-With"
        ));
        corsConfiguration.setExposedHeaders(Arrays.asList(
            "Accept",
            "Access-Control-Allow-Origin",
            "Access-Control=Allow-Credentials",
            "Authorization",
            "Content-Type",
            "Filename",
            "Jwt-Token",
            "Origin"
        ));
        corsConfiguration.setAllowedMethods(Arrays.asList(
            "GET",
            "POST",
            "PUT",
            "PATCH",
            "DELETE",
            "OPTIONS"
        ));

        urlBasedCorsConfigurationSource.registerCorsConfiguration(
            "/**",
            corsConfiguration
        );

        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}
