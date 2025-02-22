package com.KevinYLeandro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.KevinYLeandro.model.Usuario;
import com.KevinYLeandro.service.UsuarioService;
import com.KevinYLeandro.service.TipoUsuarioService;
import com.KevinYLeandro.model.Reserva;
import com.KevinYLeandro.service.ReservaService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TipoUsuarioService tipoUsuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public String listarUsuarios(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioActual = usuarioService.findByEmail(auth.getName());
        
        model.addAttribute("usuarios", usuarioService.findAll());
        model.addAttribute("usuarioActual", usuarioActual);
        return "usuarios";
    }

    @PostMapping("/cambiar-estado/{id}")
    public String cambiarEstado(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioActual = usuarioService.findByEmail(auth.getName());
        
        if (usuarioActual != null && usuarioActual.getIdusuario().equals(id)) {
            return "redirect:/usuarios?error=self_status";
        }
        
        usuarioService.toggleActivo(id);
        return "redirect:/usuarios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioActual = usuarioService.findByEmail(auth.getName());
        
        if (id.equals(usuarioActual.getIdusuario())) {
            redirectAttributes.addFlashAttribute("error", "No puedes eliminarte a ti mismo");
            return "redirect:/usuarios";
        }
        
        try {
            List<Reserva> reservasUsuario = reservaService.findReservasByUsuario(usuarioService.findById(id).orElse(null));
            if (!reservasUsuario.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "No se puede eliminar el usuario porque está asociado a una o más reservas existentes");
                return "redirect:/usuarios";
            }
            
            usuarioService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el usuario");
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.findById(id).orElse(null);
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            return "edit-usuario";
        }
        return "redirect:/usuarios";
    }

    @PostMapping("/update/{id}")
    public String actualizarUsuario(@PathVariable Long id, @ModelAttribute Usuario usuario) {
        Usuario usuarioExistente = usuarioService.findById(id).orElse(null);
        if (usuarioExistente != null) {
            // Mantener los campos que no se modifican en el formulario
            usuario.setIdusuario(id);
            usuario.setActivo(usuarioExistente.getActivo());
            usuario.setFechaactivacion(usuarioExistente.getFechaactivacion());
            usuario.setFechadesactivacion(usuarioExistente.getFechadesactivacion());
            usuario.setTipoUsuario(usuarioExistente.getTipoUsuario());
            
            // Manejar la contraseña
            if (usuario.getContrasenya() == null || usuario.getContrasenya().trim().isEmpty()) {
                usuario.setContrasenya(usuarioExistente.getContrasenya());
            } else {
                usuario.setContrasenya(passwordEncoder.encode(usuario.getContrasenya()));
            }
            
            usuarioService.save(usuario);
        }
        return "redirect:/usuarios";
    }
} 