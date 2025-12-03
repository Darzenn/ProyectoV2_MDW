package com.example.ProyectoV2_MDW.controller;

import com.example.ProyectoV2_MDW.model.Usuario;
import com.example.ProyectoV2_MDW.services.ProductoService;
import com.example.ProyectoV2_MDW.services.UsuarioService;
import com.example.ProyectoV2_MDW.jwt.JwtUtil; // tu clase utilitaria JWT
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class PaginaController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ProductoService productoService;

    // INDEX
    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/productos")
    public String productos(Model model) {
        model.addAttribute("productos", productoService.obtenerTodosLosProductos());
        return "productos";
    }

    // REGISTRO
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

    // LOGIN con JWT
    @PostMapping("/autenticar")
    public String loginUsuario(@RequestParam String correo,
                               @RequestParam String contrasena,
                               HttpServletResponse response,
                               RedirectAttributes redirectAttributes) {
        Optional<String> tokenOpt = usuarioService.loginUsuario(correo, contrasena);

        if (tokenOpt.isPresent()) {
            String token = tokenOpt.get();

            // Guardar el token en una cookie
            Cookie cookie = new Cookie("jwt-token", token);
            cookie.setHttpOnly(true); // protege contra acceso JS
            cookie.setPath("/");      // accesible en toda la app
            cookie.setMaxAge(60 * 60 * 10); // 10 horas
            response.addCookie(cookie);

            // Redirigir a productos
            return "redirect:/productos";
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", "Correo o contraseña incorrectos.");
            return "redirect:/registro";
        }
    }


    // LOGOUT
    @GetMapping("/logout")
    public String logoutUsuario(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        // Crear cookie vacía para eliminar el token
        Cookie cookie = new Cookie("jwt-token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // expira inmediatamente
        response.addCookie(cookie);

        redirectAttributes.addFlashAttribute("mensajeExito", "Sesión cerrada correctamente.");
        return "redirect:/";
    }

}
