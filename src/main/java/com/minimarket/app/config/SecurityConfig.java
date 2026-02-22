package com.minimarket.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;




@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
    private LoginSuccess loginSuccessHandler; // Usar handler

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas
                .requestMatchers("/login", "/css/*", "/js/", "/images/*").permitAll()

                // PRODUCTOS
                .requestMatchers("/admin/productos").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/user/productos").hasRole("USER") // Nuevo endpoint para user

                // VENTAS
                .requestMatchers("/admin/ventas").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/admin/ventas/crear").hasAnyRole("ADMIN", "USER")// permite al user tambien procesar venta
                .requestMatchers("/admin/ventas/guardar").hasRole("ADMIN") // form anticucho
                .requestMatchers("/admin/ventas/eliminar/**").hasRole("ADMIN")
                .requestMatchers("/admin/ventas/historial").hasRole("ADMIN")
                .requestMatchers("/admin/ventas/exportar/**").hasRole("ADMIN")
                
                // PRODUCTOS (solo admin puede modificar)
                .requestMatchers("/admin/productos/guardar").hasRole("ADMIN")
                .requestMatchers("/admin/productos/editar/**").hasRole("ADMIN")
                .requestMatchers("/admin/productos/eliminar/**").hasRole("ADMIN")

                // DASHBOARD solo ADMIN
                .requestMatchers("/admin/dashboard").hasRole("ADMIN")

                // Cualquier otra requiere autenticación
                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(loginSuccessHandler) // Usamos tu handler
                .permitAll()
            )
            	//JSESSIONID para borrar las cookies y no pueda ingresar el usuario
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build();
    }
}