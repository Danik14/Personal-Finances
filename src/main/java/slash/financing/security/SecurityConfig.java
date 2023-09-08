package slash.financing.security;

import static slash.financing.enums.UserRole.ADMIN;
import static slash.financing.enums.UserRole.VERIFIED_USER;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import slash.financing.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http)
                        throws Exception {
                http
                                .csrf()
                                .disable()
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/api/v1/admin/**").hasRole(ADMIN.name())
                                                .requestMatchers("/api/v1/verified/**").hasRole(VERIFIED_USER.name())
                                                .requestMatchers("/api/v1/auth/**").permitAll()
                                                // /error/** is a default endpoint for springs handler
                                                // if i remove it every uknown request will get 403 instead of 404
                                                .requestMatchers("/error/**", "/api/v1/healthcheck").permitAll()
                                                // any other request should be authenticated
                                                .anyRequest().authenticated())
                                .sessionManagement()
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                .and()
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                                .cors().configurationSource(corsConfigurationSource());

                return http.build();

        }

        @Bean
        protected CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOriginPatterns(List.of("*"));
                configuration.setAllowedMethods(List.of("*"));
                configuration.setAllowCredentials(true);
                configuration.setAllowedHeaders(List.of("*"));
                configuration.setExposedHeaders(
                                List.of("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials",
                                                "Access-Control-Allow-Headers", "Access-Control-Allow-Methods"));

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}
