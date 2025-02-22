package com.KevinYLeandro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.KevinYLeandro.model.Curso;
import com.KevinYLeandro.service.CursoService;
import com.KevinYLeandro.service.FamCursoService;
import com.KevinYLeandro.service.AulaService;
import java.util.List;
import java.util.stream.Collectors;
import com.KevinYLeandro.model.FamCurso;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;
    
    @Autowired
    private FamCursoService famCursoService;
    
    @Autowired
    private AulaService aulaService;

    @GetMapping
    public String listarCursos(Model model) {
        // Obtener todos los cursos sin filtrar por activo
        List<Curso> cursos = cursoService.findAll();
            
        List<FamCurso> familiasCurso = famCursoService.findAll()
            .stream()
            .filter(FamCurso::getActivo)
            .collect(Collectors.toList());
            
        model.addAttribute("cursos", cursos);
        model.addAttribute("familiasCurso", familiasCurso);
        return "cursos";
    }

    @GetMapping("/add")
    public String mostrarFormularioAgregar(Model model) {
        model.addAttribute("curso", new Curso());
        // Filtrar solo las familias de curso activas
        List<FamCurso> familiasCurso = famCursoService.findAll()
            .stream()
            .filter(FamCurso::getActivo)
            .collect(Collectors.toList());
        model.addAttribute("familiasCurso", familiasCurso);
        model.addAttribute("aulas", aulaService.findAll());
        return "add-curso";
    }

    @PostMapping("/save")
    public String guardarCurso(@ModelAttribute Curso curso) {
        cursoService.save(curso);
        return "redirect:/cursos";
    }

    @GetMapping("/edit/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        cursoService.findById(id).ifPresent(curso -> {
            model.addAttribute("curso", curso);
            // Filtrar solo las familias de curso activas
            List<FamCurso> familiasCurso = famCursoService.findAll()
                .stream()
                .filter(FamCurso::getActivo)
                .collect(Collectors.toList());
            model.addAttribute("familiasCurso", familiasCurso);
            model.addAttribute("aulas", aulaService.findAll());
        });
        return "edit-curso";
    }

    @PostMapping("/update/{id}")
    public String actualizarCurso(@PathVariable Long id, @ModelAttribute Curso curso) {
        Curso cursoExistente = cursoService.findById(id).orElse(null);
        if (cursoExistente != null) {
            curso.setIdcurso(id);
            curso.setActivo(cursoExistente.getActivo());
            cursoService.save(curso);
        }
        return "redirect:/cursos";
    }

    @PostMapping("/delete/{id}")
    public String eliminarCurso(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            cursoService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Curso eliminado correctamente");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", 
                "No se puede eliminar el curso porque está asociado a una o más reservas existentes");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el curso");
        }
        return "redirect:/cursos";
    }

    @PostMapping("/cambiar-estado/{id}")
    public String cambiarEstado(@PathVariable Long id) {
        Curso curso = cursoService.findById(id).orElse(null);
        if (curso != null) {
            curso.setActivo(!curso.getActivo());
            cursoService.save(curso);
        }
        return "redirect:/cursos";
    }
} 