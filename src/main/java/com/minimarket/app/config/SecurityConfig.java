package com.minimarket.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        // 1. Habilitar CORS con la configuración de abajo
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
         // 2. Deshabilitar CSRF (estándar cuando trabajamos con APIs y Angular)
            .csrf(csrf -> csrf.disable())
         // 3. Reglas de rutas
            .authorizeHttpRequests(auth -> auth
                
                // RUTAS PÚBLICAS
                .requestMatchers("/api/auth/login", "/img/**").permitAll()

                // PRODUCTOS
                // GET (Listar) permitido para ADMIN y USER
                .requestMatchers(HttpMethod.GET, "/api/productos/**").hasAnyRole("ADMIN", "USER")
                // POST y DELETE (Crear, Editar, Eliminar) solo para ADMIN
                .requestMatchers(HttpMethod.POST, "/api/productos/guardar").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/productos/eliminar/**").hasRole("ADMIN")

                // VENTAS
                // GET (Historial) y Exportaciones solo ADMIN
                .requestMatchers(HttpMethod.GET, "/api/ventas/historial", "/api/ventas/exportar/**").hasRole("ADMIN")
                // POST (Crear Venta) permitido para ADMIN y USER
                .requestMatchers(HttpMethod.POST, "/api/ventas/guardar").hasAnyRole("ADMIN", "USER")
                // DELETE (Eliminar Venta) solo ADMIN
                .requestMatchers(HttpMethod.DELETE, "/api/ventas/eliminar/**").hasRole("ADMIN")

                // Cualquier otra petición requiere autenticación
                .anyRequest().authenticated()
            )
         // 4. Configurar el Logout para REST (sin HTML)
            .logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler((req, res, auth) -> res.setStatus(200))
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            );

        return http.build();
    }

    
    // Configuración vital para que Angular y Spring compartan la cookie
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); 
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); 

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}