package com.KevinYLeandro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.KevinYLeandro.model.Planta;
import com.KevinYLeandro.model.Edificio;
import com.KevinYLeandro.model.Aula;
import com.KevinYLeandro.model.Reserva;
import com.KevinYLeandro.service.PlantaService;
import com.KevinYLeandro.service.EdificioService;
import com.KevinYLeandro.service.AulaService;
import com.KevinYLeandro.service.ReservaService;

import java.util.List;

@Controller
@RequestMapping("/plantas")
public class PlantaController {

    @Autowired
    private PlantaService plantaService;
    
    @Autowired
    private EdificioService edificioService;

    @Autowired
    private AulaService aulaService;

    @Autowired
    private ReservaService reservaService;

    @GetMapping("/edificio/{idedificio}")
    public String listarPlantasPorEdificio(@PathVariable Long idedificio, Model model) {
        Edificio edificio = edificioService.findById(idedificio).orElse(null);
        if (edificio != null) {
            List<Planta> plantas = plantaService.findByEdificioId(idedificio);
            model.addAttribute("plantas", plantas);
            model.addAttribute("edificio", edificio);
            return "plantas";
        }
        return "redirect:/edificios";
    }

    @GetMapping("/add/{idedificio}")
    public String mostrarFormularioAgregar(@PathVariable Long idedificio, Model model) {
        Edificio edificio = edificioService.findById(idedificio).orElse(null);
        if (edificio != null) {
            Planta planta = new Planta();
            planta.setEdificio(edificio);
            planta.setActivo(true);
            model.addAttribute("planta", planta);
            return "add-planta";
        }
        return "redirect:/edificios";
    }

    @PostMapping("/save")
    public String guardarPlanta(@ModelAttribute Planta planta) {
        plantaService.save(planta);
        return "redirect:/plantas/edificio/" + planta.getEdificio().getIdedificio();
    }

    @GetMapping("/edit/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Planta planta = plantaService.findById(id).orElse(null);
        if (planta != null) {
            model.addAttribute("planta", planta);
            return "edit-planta";
        }
        return "redirect:/edificios";
    }

    @PostMapping("/update/{id}")
    public String actualizarPlanta(@PathVariable Long id, @ModelAttribute Planta planta) {
        Planta plantaExistente = plantaService.findById(id).orElse(null);
        if (plantaExistente != null) {
            planta.setEdificio(plantaExistente.getEdificio());
            planta.setIdplanta(id);
            planta.setActivo(plantaExistente.getActivo());
            plantaService.save(planta);
            return "redirect:/plantas/edificio/" + planta.getEdificio().getIdedificio();
        }
        return "redirect:/edificios";
    }

    @PostMapping("/planta/{id}/eliminar")
    public String eliminarPlanta(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Obtener la planta y su edificio antes de eliminarla
            Planta planta = plantaService.findById(id).orElse(null);
            if (planta == null) {
                redirectAttributes.addFlashAttribute("error", "No se encontró la planta especificada");
                return "redirect:/edificios";
            }
            Long idEdificio = planta.getEdificio().getIdedificio();

            // Verificar si hay aulas con reservas asociadas
            List<Aula> aulas = aulaService.findByPlantaId(id);
            boolean tieneReservas = false;
            
            for (Aula aula : aulas) {
                List<Reserva> reservasAsociadas = reservaService.findByAulaId(aula.getIdaula());
                if (!reservasAsociadas.isEmpty()) {
                    tieneReservas = true;
                    break;
                }
            }

            if (tieneReservas) {
                redirectAttributes.addFlashAttribute("error", 
                    "No se puede eliminar la planta porque tiene aulas con reservas asociadas");
                return "redirect:/plantas/edificio/" + idEdificio;
            }
            
            // Si no hay reservas, eliminamos primero las aulas y luego la planta
            for (Aula aula : aulas) {
                aulaService.deleteById(aula.getIdaula());
            }
            
            plantaService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Planta y sus aulas eliminadas correctamente");
            return "redirect:/plantas/edificio/" + idEdificio;
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error al eliminar la planta: " + e.getMessage());
            return "redirect:/edificios";
        }
    }

    @PostMapping("/cambiar-estado/{id}")
    public String cambiarEstado(@PathVariable Long id) {
        Planta planta = plantaService.findById(id).orElse(null);
        if (planta != null) {
            planta.setActivo(!planta.getActivo());
            plantaService.save(planta);
            return "redirect:/plantas/edificio/" + planta.getEdificio().getIdedificio();
        }
        return "redirect:/edificios";
    }
} 