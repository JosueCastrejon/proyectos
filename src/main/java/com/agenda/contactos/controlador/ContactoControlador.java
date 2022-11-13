package com.agenda.contactos.controlador;

import com.agenda.contactos.modelo.Contacto;
import com.agenda.contactos.repositorio.ContactoRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContactoControlador {

    @Autowired
    private ContactoRepositorio repositorio;

    @GetMapping("/")
    public String verPaginaDeInicio(Model model) {
        List<Contacto> contactos = repositorio.findAll();
        model.addAttribute("contactos", contactos);
        return "index";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("contacto", new Contacto());
        return "nuevo";
    }

    @PostMapping("/nuevo")
    public String guardarContacto(@Validated Contacto contacto, BindingResult bindingResult, RedirectAttributes redirect, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("contacto", contacto);
            return "nuevo";
        }
        repositorio.save(contacto);
        redirect.addFlashAttribute("mensaje", "El contacto ha sido agregado con exito");
        return "redirect:/";
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioDeEditar(@PathVariable Integer id, Model model) {
        Contacto contacto = repositorio.getById(id);
        model.addAttribute("contacto", contacto);
        return "nuevo";
    }

    @PostMapping("/{id}/editar")
    public String actualizarContacto(@PathVariable Integer id, @Validated Contacto contacto, BindingResult bindingResult, RedirectAttributes redirect, Model model) {
        Contacto contactoDB = repositorio.getById(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("contacto", contacto);
            return "nuevo";
        }

        contactoDB.setNombre(contacto.getNombre());
        contactoDB.setCelular(contacto.getCelular());
        contactoDB.setEmail(contacto.getEmail());
        contactoDB.setFechaNacimiento(contacto.getFechaNacimiento());
        repositorio.save(contactoDB);
        redirect.addFlashAttribute("mensaje", "El contacto ha sido actualizado con exito");
        return "redirect:/";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarComtacto(@PathVariable Integer id, RedirectAttributes redirect) {
        Contacto contacto = repositorio.getById(id);
        repositorio.delete(contacto);
        redirect.addFlashAttribute("mensaje", "El contacto ha sido eliminado con exito");
        return "redirect:/";
    }

}
