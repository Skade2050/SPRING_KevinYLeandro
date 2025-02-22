package com.KevinYLeandro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.KevinYLeandro.model.Usuario;
import com.KevinYLeandro.service.UsuarioService;
import com.KevinYLeandro.repository.TipoUsuarioRepository;
import com.KevinYLeandro.model.TipoUsuario;

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registro")
    public String showRegistrationForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute Usuario usuario,
                                 @RequestParam(value = "claveMaestra", required = false) String claveMaestra,
                                 @RequestParam(value = "tipoUsuario", required = false) String tipoUsuario) {
        
        System.out.println("Tipo de usuario seleccionado: " + tipoUsuario);
        System.out.println("Clave maestra ingresada: " + claveMaestra);
        
        // Si se seleccionó administrador
        if ("1".equals(tipoUsuario)) {
            // Verifica si la clave maestra es correcta
            if ("wapper".equals(claveMaestra.toLowerCase())) {
                TipoUsuario adminTipo = tipoUsuarioRepository.findByTipo("Admin");
                if (adminTipo != null) {
                    usuario.setTipoUsuario(adminTipo);
                }
            } else {
                // Clave maestra incorrecta
                TipoUsuario userTipo = tipoUsuarioRepository.findByTipo("User");
                if (userTipo != null) {
                    usuario.setTipoUsuario(userTipo);
                }
                return "redirect:/login?success&message=Clave maestra incorrecta, intentelo de nuevo.";
            }
        } else {
            // Usuario normal
            TipoUsuario userTipo = tipoUsuarioRepository.findByTipo("User");
            if (userTipo != null) {
                usuario.setTipoUsuario(userTipo);
            }
        }
        
        usuarioService.save(usuario);
        return "redirect:/login?success";
    }
} 