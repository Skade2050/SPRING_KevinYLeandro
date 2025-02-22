package com.KevinYLeandro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.KevinYLeandro.model.TipoAula;
import com.KevinYLeandro.service.TipoAulaService;
import com.KevinYLeandro.service.AulaService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tipos-aula")
public class TipoAulaController {

    @Autowired
    private TipoAulaService tipoAulaService;
    
    @Autowired
    private AulaService aulaService;

    @GetMapping
    public String listarTiposAula(Model model) {
        model.addAttribute("tiposAula", tipoAulaService.findAll());
        return "tipos-aula";
    }

    @GetMapping("/add")
    public String mostrarFormularioAgregar(Model model) {
        model.addAttribute("tipoAula", new TipoAula());
        return "add-tipo-aula";
    }

    @PostMapping("/save")
    public String guardarTipoAula(@ModelAttribute TipoAula tipoAula) {
        tipoAula.setActivo(true);
        tipoAulaService.save(tipoAula);
        return "redirect:/tipos-aula";
    }

    @GetMapping("/edit/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        TipoAula tipoAula = tipoAulaService.findById(id);
        if (tipoAula != null) {
            model.addAttribute("tipoAula", tipoAula);
            return "edit-tipo-aula";
        }
        return "redirect:/tipos-aula";
    }

    @PostMapping("/update/{id}")
    public String actualizarTipoAula(@PathVariable Long id, @ModelAttribute TipoAula tipoAula) {
        TipoAula tipoAulaExistente = tipoAulaService.findById(id);
        if (tipoAulaExistente != null) {
            tipoAula.setIdtipo(id);
            tipoAula.setActivo(tipoAulaExistente.getActivo()); // Mantener el estado actual
            tipoAulaService.save(tipoAula);
        }
        return "redirect:/tipos-aula";
    }

    @PostMapping("/delete/{id}")
    public String eliminarTipoAula(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        // Primero verificamos si hay aulas usando este tipo
        if (aulaService.findByTipoAulaId(id).isEmpty()) {
            try {
                tipoAulaService.deleteById(id);
                redirectAttributes.addFlashAttribute("success", "Tipo de aula eliminado correctamente");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "No se puede eliminar el tipo de aula porque está asociada a una o más aulas existentes");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar el tipo de aula porque está asociada a una o más aulas existentes");
        }
        return "redirect:/tipos-aula";
    }

    @PostMapping("/cambiar-estado/{id}")
    public String cambiarEstado(@PathVariable Long id) {
        TipoAula tipoAula = tipoAulaService.findById(id);
        if (tipoAula != null) {
            tipoAula.setActivo(!tipoAula.getActivo()); // Cambia el estado actual al opuesto
            tipoAulaService.save(tipoAula);
        }
        return "redirect:/tipos-aula";
    }
} 