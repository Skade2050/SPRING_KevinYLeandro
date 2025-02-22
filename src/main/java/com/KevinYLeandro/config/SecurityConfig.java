package com.KevinYLeandro.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.KevinYLeandro.service.UsuarioService;
import com.KevinYLeandro.model.Usuario;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UsuarioService usuarioService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/registro", "/login").permitAll()
                .antMatchers("/admin/**", "/edificios/**", "/edificio/**", "/usuarios/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("contrasenya")
                .failureHandler((request, response, exception) -> {
                    String email = request.getParameter("email");
                    Usuario usuario = usuarioService.findByEmail(email);
                    
                    if (usuario == null) {
                        response.sendRedirect("/login?error=notfound");
                    } else if (!usuario.getActivo()) {
                        response.sendRedirect("/login?error=inactive");
                    } else {
                        response.sendRedirect("/login?error=badcredentials");
                    }
                })
                .successHandler((request, response, authentication) -> {
                    Usuario usuario = usuarioService.findByEmail(authentication.getName());
                    if (!usuario.getActivo()) {
                        request.getSession().invalidate();
                        response.sendRedirect("/login?error=inactive");
                        return;
                    }
                    
                    if (usuario.getTipoUsuario() != null && 
                        "Admin".equals(usuario.getTipoUsuario().getTipo())) {
                        response.sendRedirect("/");
                    } else {
                        response.sendRedirect("/user/dashboard");
                    }
                })
                .permitAll()
                .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll();
        
        return http.build();
    }
} 