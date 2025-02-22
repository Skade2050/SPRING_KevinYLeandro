package com.KevinYLeandro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.KevinYLeandro.model.FamCurso;
import com.KevinYLeandro.service.FamCursoService;
import com.KevinYLeandro.service.SubUsoAulaService;
import com.KevinYLeandro.model.SubUsoAula;
import com.KevinYLeandro.service.UsoAulaService;
import com.KevinYLeandro.model.UsoAula;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.KevinYLeandro.model.Curso;
import com.KevinYLeandro.service.CursoService;
import com.KevinYLeandro.model.Reserva;
import com.KevinYLeandro.service.ReservaService;

@Controller
@RequestMapping("/familias-curso")
public class FamCursoController {

    @Autowired
    private FamCursoService famCursoService;
    
    @Autowired
    private SubUsoAulaService subUsoAulaService;
    
    @Autowired
    private UsoAulaService usoAulaService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public String listarFamiliasCurso(Model model) {
        List<FamCurso> familiasCurso = famCursoService.findAll();
        model.addAttribute("familiasCurso", familiasCurso);
        return "familias-curso";
    }

    @GetMapping("/add")
    public String mostrarFormularioAgregar(Model model) {
        model.addAttribute("famCurso", new FamCurso());
        model.addAttribute("subUsos", subUsoAulaService.findAll());
        return "add-familia-curso";
    }

    @PostMapping("/save")
    public String guardarFamiliaCurso(@ModelAttribute FamCurso famCurso) {
        UsoAula usoAula = usoAulaService.findAll().stream()
                .findFirst()
                .orElseGet(() -> {
                    UsoAula nuevoUso = new UsoAula();
                    nuevoUso.setDescripcion("Uso General");
                    nuevoUso.setActivo(true);
                    return usoAulaService.save(nuevoUso);
                });

        SubUsoAula subUsoAula = new SubUsoAula();
        subUsoAula.setDescripcion(famCurso.getSubUsoAula().getDescripcion());
        subUsoAula.setActivo(true);
        subUsoAula.setUsoAula(usoAula);
        subUsoAula = subUsoAulaService.save(subUsoAula);
        
        famCurso.setSubUsoAula(subUsoAula);
        famCursoService.save(famCurso);
        return "redirect:/familias-curso";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        famCursoService.findById(id).ifPresent(famCurso -> {
            model.addAttribute("famCurso", famCurso);
            model.addAttribute("subUsos", subUsoAulaService.findAll());
        });
        return "edit-familia-curso";
    }

    @PostMapping("/update/{id}")
    public String actualizarFamiliaCurso(@PathVariable Long id, @ModelAttribute FamCurso famCurso) {
        // Obtener la familia de curso existente
        FamCurso famCursoExistente = famCursoService.findById(id).orElse(null);
        if (famCursoExistente != null) {
            // Mantener el mismo SubUsoAula pero actualizar su descripción
            SubUsoAula subUsoAula = famCursoExistente.getSubUsoAula();
            subUsoAula.setDescripcion(famCurso.getSubUsoAula().getDescripcion());
            subUsoAulaService.save(subUsoAula);
            
            // Actualizar la familia de curso
            famCurso.setSubUsoAula(subUsoAula);
            famCursoService.save(famCurso);
        }
        return "redirect:/familias-curso";
    }

    @PostMapping("/delete/{id}")
    public String eliminarFamCurso(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Verificar si hay cursos que usen esta familia
            List<Curso> cursosAsociados = cursoService.findByFamCursoId(id);
            
            if (cursosAsociados.isEmpty()) {
                // Si no hay cursos asociados, podemos eliminar la familia
                famCursoService.deleteById(id);
                redirectAttributes.addFlashAttribute("success", "Familia de curso eliminada correctamente");
            } else {
                redirectAttributes.addFlashAttribute("error", 
                    "No se puede eliminar la familia de curso porque está asociada a uno o más cursos");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la familia de curso");
        }
        return "redirect:/familias-curso";
    }

    @PostMapping("/cambiar-estado/{id}")
    public String cambiarEstado(@PathVariable Long id) {
        FamCurso famCurso = famCursoService.findById(id).orElse(null);
        if (famCurso != null) {
            famCurso.setActivo(!famCurso.getActivo());
            famCursoService.save(famCurso);
        }
        return "redirect:/familias-curso";
    }
} 