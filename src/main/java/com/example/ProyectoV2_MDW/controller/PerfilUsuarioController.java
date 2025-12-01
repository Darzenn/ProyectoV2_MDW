package com.example.ProyectoV2_MDW.controller;

import com.example.ProyectoV2_MDW.model.Usuario;
import com.example.ProyectoV2_MDW.repository.UsuarioRepository;
import com.example.ProyectoV2_MDW.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/perfil")
public class PerfilUsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/{correo}")
    public String perfilUsuarioView(@PathVariable String correo, Model model) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));

        model.addAttribute("usuario", usuario);
        return "perfil";
    }

}
