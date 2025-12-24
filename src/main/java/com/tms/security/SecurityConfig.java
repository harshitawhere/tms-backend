package com.tms.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.http.HttpMethod;
import org.springframework.core.env.Environment;
import java.util.Arrays;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final Environment env;

    public SecurityConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {

        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode("admin"))
                .roles("ADMIN")
                .build();

        UserDetails employee = User.withUsername("emp")
                .password(encoder.encode("emp"))
                .roles("EMPLOYEE")
                .build();

        return new InMemoryUserDetailsManager(admin, employee);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        boolean isDev = Arrays.asList(env.getActiveProfiles()).contains("dev");

        http
            // Enable CORS support (configuration provided by corsConfigurationSource())
            .cors(Customizer.withDefaults())
            // Disable CSRF for API clients (we use HTTP Basic)
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> {
                if (isDev) {
                    // In dev profile allow loading GraphiQL UI locally
                    auth.requestMatchers(HttpMethod.GET, "/graphiql", "/graphiql/**").permitAll();
                } else {
                    // In non-dev environments require auth even to load the UI
                    auth.requestMatchers(HttpMethod.GET, "/graphiql", "/graphiql/**").authenticated();
                }
                // Require authentication for GraphQL POSTs
                auth.requestMatchers(HttpMethod.POST, "/graphql").authenticated();
                // Everything else requires auth
                auth.anyRequest().authenticated();
            })
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        boolean isDev = Arrays.asList(env.getActiveProfiles()).contains("dev");

        if (isDev) {
            configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        } else {
            configuration.setAllowedOrigins(Arrays.asList("https://tms-frontend-266h.vercel.app"));
        }

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
