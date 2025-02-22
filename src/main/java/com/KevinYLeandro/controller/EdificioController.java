package com.KevinYLeandro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.KevinYLeandro.model.Edificio;
import com.KevinYLeandro.model.Planta;
import com.KevinYLeandro.model.Aula;
import com.KevinYLeandro.model.Reserva;
import com.KevinYLeandro.service.EdificioService;
import com.KevinYLeandro.service.PlantaService;
import com.KevinYLeandro.service.AulaService;
import com.KevinYLeandro.service.ReservaService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/edificios")
public class EdificioController {

    @Autowired
    private EdificioService edificioService;

    @Autowired
    private PlantaService plantaService;

    @Autowired
    private AulaService aulaService;

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public String listarEdificios(Model model) {
        model.addAttribute("edificios", edificioService.findAll());
        return "edificios";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioAgregar(Model model) {
        model.addAttribute("edificio", new Edificio());
        model.addAttribute("esNuevo", true);
        return "add-edificio";
    }

    @PostMapping("/save")
    public String guardarEdificio(@ModelAttribute Edificio edificio) {
        edificioService.save(edificio);
        return "redirect:/edificios";
    }

    @GetMapping("/{id}")
    public String getEdificioById(@PathVariable Long id, Model model) {
        Optional<Edificio> edificioOptional = edificioService.findById(id);
        if (edificioOptional.isPresent()) {
            model.addAttribute("edificio", edificioOptional.get());
            model.addAttribute("vacio", false);
        } else {
            model.addAttribute("vacio", true);
            model.addAttribute("mensaje", "No existe edificio con ID " + id);
        }
        return "edificio";
    }

    @GetMapping("/editar/{id}")
    public String editarEdificio(@PathVariable Long id, Model model) {
        Optional<Edificio> edificio = edificioService.findById(id);
        if (edificio.isPresent()) {
            model.addAttribute("edificio", edificio.get());
            model.addAttribute("esNuevo", false);
            return "add-edificio";
        }
        return "redirect:/edificios";
    }

    @PostMapping("/update/{id}")
    public String actualizarEdificio(@PathVariable Long id, @ModelAttribute Edificio edificio) {
        edificio.setIdedificio(id);
        edificioService.save(edificio);
        return "redirect:/edificios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarEdificio(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Primero obtenemos todas las plantas del edificio
            List<Planta> plantas = plantaService.findByEdificioId(id);
            
            // Para cada planta, verificamos sus aulas y sus reservas
            for (Planta planta : plantas) {
                List<Aula> aulas = aulaService.findByPlantaId(planta.getIdplanta());
                for (Aula aula : aulas) {
                    List<Reserva> reservas = reservaService.findByAulaId(aula.getIdaula());
                    if (!reservas.isEmpty()) {
                        redirectAttributes.addFlashAttribute("error", 
                            "No se puede eliminar el edificio porque está asociado a una o más reservas existentes");
                        return "redirect:/edificios";
                    }
                }
            }
            
            edificioService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Edificio eliminado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el edificio");
        }
        return "redirect:/edificios";
    }
} 