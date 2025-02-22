package com.KevinYLeandro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.KevinYLeandro.service.EdificioService;
import com.KevinYLeandro.service.PlantaService;
import com.KevinYLeandro.service.UsuarioService;
import com.KevinYLeandro.service.AulaService;
import com.KevinYLeandro.service.CursoService;
import com.KevinYLeandro.service.ReservaService;
import com.KevinYLeandro.service.TipoAulaService;
import com.KevinYLeandro.model.Edificio;
import com.KevinYLeandro.model.Usuario;
import com.KevinYLeandro.model.Planta;
import com.KevinYLeandro.model.Aula;
import com.KevinYLeandro.model.Reserva;
import com.KevinYLeandro.model.Curso;
import com.KevinYLeandro.model.TipoAula;

import java.util.List;
import java.time.LocalDate;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private EdificioService edificioService;
    
    @Autowired
    private PlantaService plantaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AulaService aulaService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private TipoAulaService tipoAulaService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Usuario usuario = usuarioService.findByEmail(email);
        String nombreUsuario = usuario.getNombre();
        
        model.addAttribute("nombreUsuario", nombreUsuario);
        model.addAttribute("edificios", edificioService.findAll());
        return "user/dashboard";
    }
    
    @GetMapping("/edificio/{id}/plantas")
    public String verPlantasEdificio(@PathVariable Long id, Model model) {
        Edificio edificio = edificioService.findById(id).orElse(null);
        if (edificio != null && edificio.getActivo()) {
            model.addAttribute("edificio", edificio);
            model.addAttribute("plantas", plantaService.findByEdificioId(id));
            return "user/plantas";
        }
        return "redirect:/user/dashboard";
    }

    @GetMapping("/planta/{id}/aulas/buscar")
    @ResponseBody
    public ResponseEntity<List<Aula>> buscarAulas(
            @PathVariable Long id,
            @RequestParam(required = false) String nombreAula,
            @RequestParam(required = false) Integer capacidadMinima,
            @RequestParam(required = false) Long tipoAula) {
        
        List<Aula> aulas = aulaService.findByPlantaId(id);
        
        // Aplicar filtros
        List<Aula> aulasFiltradas = aulas.stream()
            .filter(aula -> {
                boolean cumpleFiltros = true;
                
                if (nombreAula != null && !nombreAula.trim().isEmpty()) {
                    cumpleFiltros = aula.getNombreaula().toLowerCase()
                        .contains(nombreAula.toLowerCase()) ||
                        aula.getDescripcion().toLowerCase()
                        .contains(nombreAula.toLowerCase());
                }
                
                if (capacidadMinima != null && capacidadMinima > 0) {
                    cumpleFiltros = cumpleFiltros && 
                        aula.getCapacidad() >= capacidadMinima;
                }
                
                if (tipoAula != null && tipoAula > 0) {
                    cumpleFiltros = cumpleFiltros && 
                        aula.getTipoAula().getIdtipo().equals(tipoAula);
                }
                
                return cumpleFiltros;
            })
            .collect(Collectors.toList());
            
        return ResponseEntity.ok(aulasFiltradas);
    }

    @GetMapping("/planta/{id}/aulas")
    public String verAulasPlanta(@PathVariable Long id, Model model) {
        Planta planta = plantaService.findById(id).orElse(null);
        if (planta != null && planta.getActivo()) {
            model.addAttribute("planta", planta);
            model.addAttribute("aulas", aulaService.findByPlantaId(id));
            model.addAttribute("tiposAula", tipoAulaService.findAll());
            return "user/aulas";
        }
        return "redirect:/user/dashboard";
    }

    @GetMapping("/aula/{id}/reservar")
    public String mostrarFormularioReserva(@PathVariable Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Usuario usuario = usuarioService.findByEmail(email);
        
        Aula aula = aulaService.findById(id).orElse(null);
        if (aula != null && aula.getActivo()) {
            Reserva reserva = new Reserva();
            reserva.setAula(aula);
            reserva.setUsuario(usuario);
            reserva.setActivo(true);
            reserva.setValidar(false);
            
            // Obtener reservas para los próximos 30 días
            LocalDate today = LocalDate.now();
            LocalDate endDate = today.plusDays(30);
            List<Reserva> reservasExistentes = reservaService.findReservasByAulaAndDateRange(id, today, endDate);
            
            model.addAttribute("reserva", reserva);
            model.addAttribute("cursos", cursoService.findAll().stream()
                .filter(Curso::getActivo)
                .collect(Collectors.toList()));
            model.addAttribute("reservasExistentes", reservasExistentes);
            return "user/reservar-aula";
        }
        return "redirect:/user/dashboard";
    }

    @PostMapping("/aula/reservar")
    public String procesarReserva(@ModelAttribute Reserva reserva, RedirectAttributes redirectAttributes) {
        if (!reservaService.esReservaValida(reserva)) {
            String mensaje;
            if (reserva.getFechahasta().isBefore(reserva.getFechadesde())) {
                mensaje = "La fecha de fin no puede ser anterior a la fecha de inicio";
            } else if (reserva.getFechadesde().equals(reserva.getFechahasta()) && 
                       !reserva.getHorahasta().isAfter(reserva.getHoradesde())) {
                mensaje = "La hora de fin debe ser posterior a la hora de inicio";
            } else {
                mensaje = "Ya existe una reserva para esta aula en el horario seleccionado";
            }
            redirectAttributes.addFlashAttribute("error", mensaje);
            return "redirect:/user/aula/" + reserva.getAula().getIdaula() + "/reservar";
        }

        reserva.setActivo(true);
        reserva.setValidar(false);
        reservaService.save(reserva);
        redirectAttributes.addFlashAttribute("success", "Reserva realizada con éxito");
        return "redirect:/user/aula/" + reserva.getAula().getIdaula() + "/reservar";
    }

    @GetMapping("/mis-reservas")
    public String misReservas(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Usuario usuario = usuarioService.findByEmail(email);
        
        List<Reserva> reservas = reservaService.findReservasByUsuario(usuario);
        model.addAttribute("reservas", reservas);
        return "user/mis-reservas";
    }

    @GetMapping("/reserva/{id}/editar")
    public String editarReserva(@PathVariable Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Usuario usuario = usuarioService.findByEmail(email);
        
        Reserva reserva = reservaService.findById(id);
        if (reserva != null && reserva.getUsuario().getIdusuario().equals(usuario.getIdusuario())) {
            // Obtener reservas para los próximos 30 días, excluyendo la reserva actual
            LocalDate today = LocalDate.now();
            LocalDate endDate = today.plusDays(30);
            List<Reserva> reservasExistentes = reservaService.findReservasByAulaAndDateRange(
                reserva.getAula().getIdaula(), 
                today, 
                endDate
            ).stream()
            .filter(r -> !r.getIdreserva().equals(id))
            .collect(Collectors.toList());
            
            model.addAttribute("reserva", reserva);
            model.addAttribute("cursos", cursoService.findAll().stream()
                .filter(Curso::getActivo)
                .collect(Collectors.toList()));
            model.addAttribute("reservasExistentes", reservasExistentes);
            return "user/editar-reserva";
        }
        return "redirect:/user/mis-reservas";
    }

    @PostMapping("/reserva/{id}/actualizar")
    public String actualizarReserva(@PathVariable Long id, @ModelAttribute Reserva reserva, 
                                   RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Usuario usuario = usuarioService.findByEmail(email);
        
        Reserva reservaExistente = reservaService.findById(id);
        if (reservaExistente != null && 
            reservaExistente.getUsuario().getIdusuario().equals(usuario.getIdusuario())) {
            
            reserva.setIdreserva(id); // Importante para la validación de superposición
            if (!reservaService.esReservaValida(reserva)) {
                String mensaje;
                if (reserva.getFechahasta().isBefore(reserva.getFechadesde())) {
                    mensaje = "La fecha de fin no puede ser anterior a la fecha de inicio";
                } else if (reserva.getFechadesde().equals(reserva.getFechahasta()) && 
                           !reserva.getHorahasta().isAfter(reserva.getHoradesde())) {
                    mensaje = "La hora de fin debe ser posterior a la hora de inicio";
                } else {
                    mensaje = "Ya existe una reserva para esta aula en el horario seleccionado";
                }
                redirectAttributes.addFlashAttribute("error", mensaje);
                return "redirect:/user/reserva/" + id + "/editar";
            }
            
            reserva.setUsuario(usuario);
            reserva.setActivo(true);
            reserva.setValidar(false);
            reservaService.save(reserva);
            redirectAttributes.addFlashAttribute("success", "Reserva actualizada con éxito");
        }
        return "redirect:/user/mis-reservas";
    }

    @PostMapping("/reserva/{id}/eliminar")
    public String eliminarReserva(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reservaService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Reserva eliminada correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la reserva");
        }
        return "redirect:/user/mis-reservas";
    }

    @GetMapping("/cuenta")
    public String editarCuenta(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.findByEmail(auth.getName());
        model.addAttribute("usuario", usuario);
        return "user/edit-cuenta";
    }

    @PostMapping("/cuenta/actualizar")
    public String actualizarCuenta(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioExistente = usuarioService.findByEmail(auth.getName());
        
        boolean requiereCierreSesion = false;
        
        // Verificar si el email o la contraseña han cambiado
        if (!usuarioExistente.getEmail().equals(usuario.getEmail())) {
            requiereCierreSesion = true;
        }
        
        // Mantener los campos que no se modifican
        usuario.setIdusuario(usuarioExistente.getIdusuario());
        usuario.setActivo(usuarioExistente.getActivo());
        usuario.setFechaactivacion(usuarioExistente.getFechaactivacion());
        usuario.setFechadesactivacion(usuarioExistente.getFechadesactivacion());
        usuario.setTipoUsuario(usuarioExistente.getTipoUsuario());
        
        // Manejar la contraseña
        if (usuario.getContrasenya() == null || usuario.getContrasenya().trim().isEmpty()) {
            usuario.setContrasenya(usuarioExistente.getContrasenya());
        } else {
            usuario.setContrasenya(passwordEncoder.encode(usuario.getContrasenya()));
            requiereCierreSesion = true;
        }
        
        usuarioService.save(usuario);
        
        if (requiereCierreSesion) {
            redirectAttributes.addFlashAttribute("mensaje", 
                "Los cambios se han guardado correctamente. Por favor, inicia sesión nuevamente.");
            return "redirect:/logout";
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Los cambios se han guardado correctamente.");
            return "redirect:/user/dashboard";
        }
    }

    @GetMapping("/planta/{id}/aulas/filtradas")
    public String mostrarAulasFiltradas(
            @PathVariable Long id,
            @RequestParam(required = false) String nombreAula,
            @RequestParam(required = false) Integer capacidadMinima,
            @RequestParam(required = false) Long tipoAula,
            Model model) {
        
        // Obtener la planta usando Optional correctamente
        Planta planta = plantaService.findById(id).orElse(null);
        
        List<Aula> aulas = aplicarFiltros(id, nombreAula, capacidadMinima, tipoAula);
        
        model.addAttribute("aulas", aulas);
        model.addAttribute("planta", planta);
        model.addAttribute("tiposAula", tipoAulaService.findAll());
        return "user/aulas-filtradas";
    }

    private List<Aula> aplicarFiltros(Long idPlanta, String nombreAula, Integer capacidadMinima, Long tipoAula) {
        List<Aula> aulas = aulaService.findByPlantaId(idPlanta);
        
        return aulas.stream()
            .filter(aula -> {
                boolean cumple = true;
                
                if (nombreAula != null && !nombreAula.trim().isEmpty()) {
                    cumple = aula.getNombreaula().toLowerCase().contains(nombreAula.toLowerCase()) ||
                             aula.getDescripcion().toLowerCase().contains(nombreAula.toLowerCase());
                }
                
                if (capacidadMinima != null && capacidadMinima > 0) {
                    cumple = cumple && aula.getCapacidad() >= capacidadMinima;
                }
                
                if (tipoAula != null && tipoAula > 0) {
                    cumple = cumple && aula.getTipoAula().getIdtipo().equals(tipoAula);
                }
                
                return cumple;
            })
            .collect(Collectors.toList());
    }
} 