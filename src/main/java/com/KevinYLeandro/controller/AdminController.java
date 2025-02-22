package com.KevinYLeandro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.KevinYLeandro.model.Planta;
import com.KevinYLeandro.model.Aula;
import com.KevinYLeandro.model.Reserva;
import com.KevinYLeandro.service.PlantaService;
import com.KevinYLeandro.service.EdificioService;
import com.KevinYLeandro.service.TipoAulaService;
import com.KevinYLeandro.service.ReservaService;
import com.KevinYLeandro.service.CursoService;
import com.KevinYLeandro.service.AulaService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PlantaService plantaService;

    @Autowired
    private EdificioService edificioService;

    @Autowired
    private TipoAulaService tipoAulaService;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private AulaService aulaService;

    @GetMapping("/edificio/{id}/plantas")
    @ResponseBody
    public List<Planta> getPlantas(@PathVariable Long id) {
        return plantaService.findByEdificioId(id);
    }

    @GetMapping("/add-aula")
    public String mostrarFormularioAgregarAula(Model model) {
        model.addAttribute("aula", new Aula());
        model.addAttribute("edificios", edificioService.findAll());
        model.addAttribute("tiposAula", tipoAulaService.findAll());
        return "add-aula";
    }

    @GetMapping("/reserva/{id}/editar")
    public String editarReserva(@PathVariable Long id, Model model) {
        Reserva reserva = reservaService.findById(id);
        if (reserva != null) {
            model.addAttribute("reserva", reserva);
            model.addAttribute("cursos", cursoService.findAll());
            return "edit-reserva";
        }
        return "redirect:/admin/reservas";
    }

    @GetMapping("/reserva/{id}/eliminar")
    public String eliminarReserva(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reservaService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Reserva eliminada correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la reserva");
        }
        return "redirect:/reservas";
    }

    @PostMapping("/aula/{id}/eliminar")
    public String eliminarAula(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Primero verificar si hay reservas asociadas
            List<Reserva> reservasAsociadas = reservaService.findByAulaId(id);
            if (!reservasAsociadas.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", 
                    "No se puede eliminar el aula porque está asociada a una o más reservas existentes");
                return "redirect:/aulas";
            }
            
            aulaService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Aula eliminada correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error al eliminar el aula: " + e.getMessage());
        }
        return "redirect:/aulas";
    }
} 