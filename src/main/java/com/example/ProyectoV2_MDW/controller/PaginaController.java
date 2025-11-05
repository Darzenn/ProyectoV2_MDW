package com.example.ProyectoV2_MDW.controller;

import org.springframework.stereotype.Controller;

import com.example.ProyectoV2_MDW.model.Usuario;
import com.example.ProyectoV2_MDW.services.ProductoService;
import com.example.ProyectoV2_MDW.services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class PaginaController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ProductoService productoService;

    private void agregarUsuarioAlModelo(Model model, HttpSession session){
        model.addAttribute("usuarioLogueado", session.getAttribute("usuarioLogueado"));
    }

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        agregarUsuarioAlModelo(model, session);
        return "index";
    }

    @GetMapping("/productos")
    public String productos(Model model, HttpSession session) {
        
        agregarUsuarioAlModelo(model, session);
        model.addAttribute("productos", productoService.obtenerTodosLosProductos());
        return "productos";
    }

    //REGISTROOOO

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "zonaRegistro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.registrarUsuario(usuario);
            redirectAttributes.addFlashAttribute("mensajeExito", "Registro exitoso. Ahora puedes iniciar sesión.");
            
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/registro";
    }

    //LOGINNNN

    @PostMapping("/login")
    public String loginUsuario(@RequestParam String correo,
                               @RequestParam String contrasena,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOpt = usuarioService.loginUsuario(correo, contrasena);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            session.setAttribute("usuarioLogueado", usuario);
            return "redirect:/productos";
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", "Correo o contraseña incorrectos.");
            return "redirect:/registro";
        }
    }


    @GetMapping("/logout")
    public String logoutUsuario(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        return "redirect:/";
    }



}
