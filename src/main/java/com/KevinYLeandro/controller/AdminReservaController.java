package com.KevinYLeandro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.KevinYLeandro.model.Reserva;
import com.KevinYLeandro.service.ReservaService;
import com.KevinYLeandro.service.CursoService;
import java.util.List;

@Controller
@RequestMapping("/reservas")
public class AdminReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public String listarReservas(Model model) {
        List<Reserva> reservas = reservaService.findAll();
        // Asegurarnos de que ninguna reserva tenga activo como null
        reservas.forEach(reserva -> {
            if (reserva.getActivo() == null) {
                reserva.setActivo(true); // valor por defecto
                reservaService.save(reserva);
            }
        });
        model.addAttribute("reservas", reservas);
        return "reservas";
    }

    @GetMapping("/edit/{id}")
    public String editarReserva(@PathVariable Long id, Model model) {
        Reserva reserva = reservaService.findById(id);
        if (reserva != null) {
            model.addAttribute("reserva", reserva);
            model.addAttribute("cursos", cursoService.findAll());
            return "edit-reserva";
        }
        return "redirect:/reservas";
    }

    @PostMapping("/update/{id}")
    public String actualizarReserva(@PathVariable Long id, @ModelAttribute Reserva reserva, RedirectAttributes redirectAttributes) {
        Reserva reservaExistente = reservaService.findById(id);
        if (reservaExistente != null) {
            reserva.setIdreserva(id);
            reservaService.save(reserva);
            redirectAttributes.addFlashAttribute("success", "Reserva actualizada con éxito");
        }
        return "redirect:/reservas";
    }

    @PostMapping("/delete/{id}")
    public String eliminarReserva(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Eliminar la reserva en lugar de cambiar su estado
            reservaService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Reserva eliminada con éxito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la reserva");
        }
        return "redirect:/reservas";
    }

    @PostMapping("/cambiar-estado/{id}")
    public String cambiarEstado(@PathVariable Long id) {
        Reserva reserva = reservaService.findById(id);
        if (reserva != null) {
            reserva.setActivo(!reserva.getActivo());
            reservaService.save(reserva);
        }
        return "redirect:/reservas";
    }
} 