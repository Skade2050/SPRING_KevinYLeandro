package com.KevinYLeandro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.KevinYLeandro.model.Aula;
import com.KevinYLeandro.model.Planta;
import com.KevinYLeandro.model.TipoAula;
import com.KevinYLeandro.model.Edificio;
import com.KevinYLeandro.service.AulaService;
import com.KevinYLeandro.service.PlantaService;
import com.KevinYLeandro.service.TipoAulaService;
import com.KevinYLeandro.service.EdificioService;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/aulas")
public class AulaController {

    @Autowired
    private AulaService aulaService;
    
    @Autowired
    private PlantaService plantaService;
    
    @Autowired
    private TipoAulaService tipoAulaService;

    @Autowired
    private EdificioService edificioService;

    @GetMapping
    public String listarAulas(Model model) {
        List<Aula> aulas = aulaService.findAll();
        model.addAttribute("aulas", aulas);
        return "aulas";
    }

    @GetMapping("/add")
    public String mostrarFormularioAgregar(Model model) {
        model.addAttribute("aula", new Aula());
        List<Edificio> edificios = edificioService.findAll()
            .stream()
            .filter(Edificio::getActivo)
            .collect(Collectors.toList());
        List<TipoAula> tiposAula = tipoAulaService.findAll()
            .stream()
            .filter(TipoAula::getActivo)
            .collect(Collectors.toList());
        model.addAttribute("edificios", edificios);
        model.addAttribute("tiposAula", tiposAula);
        return "add-aula";
    }

    @PostMapping("/save")
    public String guardarAula(@ModelAttribute Aula aula) {
        aula.setActivo(true);
        aulaService.save(aula);
        return "redirect:/aulas";
    }

    @GetMapping("/edit/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        aulaService.findById(id).ifPresent(aula -> {
            model.addAttribute("aula", aula);
            model.addAttribute("plantas", plantaService.findAll());
            List<TipoAula> tiposAula = tipoAulaService.findAll()
                .stream()
                .filter(TipoAula::getActivo)
                .collect(Collectors.toList());
            model.addAttribute("tiposAula", tiposAula);
        });
        return "edit-aula";
    }

    @PostMapping("/update/{id}")
    public String actualizarAula(@PathVariable Long id, @ModelAttribute Aula aula) {
        aula.setIdaula(id);
        aulaService.save(aula);
        return "redirect:/aulas";
    }

    @PostMapping("/delete/{id}")
    public String eliminarAula(@PathVariable Long id) {
        Aula aula = aulaService.findById(id).orElse(null);
        if (aula != null) {
            aula.setActivo(false);
            aulaService.save(aula);
        }
        return "redirect:/aulas";
    }

    @PostMapping("/cambiar-estado/{id}")
    public String cambiarEstado(@PathVariable Long id) {
        Aula aula = aulaService.findById(id).orElse(null);
        if (aula != null) {
            aula.setActivo(!aula.getActivo());
            aulaService.save(aula);
        }
        return "redirect:/aulas";
    }
} 