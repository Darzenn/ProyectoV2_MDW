package com.example.ProyectoV2_MDW.controller;

import com.example.ProyectoV2_MDW.model.Carrito;
import com.example.ProyectoV2_MDW.model.Factura;
import com.example.ProyectoV2_MDW.model.Usuario;
import com.example.ProyectoV2_MDW.services.CarritoService;
import com.example.ProyectoV2_MDW.services.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoService carritoService;
    private final ProductoService productoService;

    public CarritoController(CarritoService carritoService, ProductoService productoService) {
        this.carritoService = carritoService;
        this.productoService = productoService;
    }

    // Agregar producto al carrito
    @PostMapping("/agregar/{productoId}")
    public String agregarAlCarrito(@PathVariable Long productoId,
                                   @RequestParam(defaultValue = "1") int cantidad,
                                   HttpSession session,
                                   Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/login";
        }

        carritoService.agregarProducto(usuario.getId(), productoId, cantidad);

        // Volvemos a cargar la página de productos con el carrito actualizado
        Carrito carritoActualizado = carritoService.obtenerCarritoDeUsuario(usuario.getId());
        model.addAttribute("productos", productoService.obtenerTodosLosProductos());
        model.addAttribute("carrito", carritoActualizado);
        model.addAttribute("usuarioLogueado", usuario);

        return "productos"; // renderiza productos.html
    }

    // Ver carrito (para el offcanvas)
    @GetMapping("/ver")
    public String verCarrito(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        Carrito carrito = carritoService.obtenerCarritoDeUsuario(usuario.getId());
        model.addAttribute("carrito", carrito);

        return "fragments/carrito :: listaCarrito";
    }

    // Eliminar producto del carrito
    @PostMapping("/eliminar/{idDetalle}")
    public String eliminarDelCarrito(@PathVariable Long idDetalle,
                                     HttpSession session,
                                     Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        carritoService.eliminarDetalle(idDetalle);

        // Actualiza productos.html después de eliminar
        Carrito carrito = carritoService.obtenerCarritoDeUsuario(usuario.getId());
        model.addAttribute("productos", productoService.obtenerTodosLosProductos());
        model.addAttribute("carrito", carrito);
        model.addAttribute("usuarioLogueado", usuario);

        return "productos";
    }

    // Ir a zona de pago
    @GetMapping("/pago")
    public String irZonaPago(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        Carrito carrito = carritoService.obtenerCarritoDeUsuario(usuario.getId());
        model.addAttribute("carrito", carrito);
        return "zonapago";
    }

    // Confirmar compra
    @PostMapping("/confirmar-compra")
    public String confirmarCompra(HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        try {
            Factura factura = carritoService.procesarCompra(usuario.getId());
            redirectAttributes.addFlashAttribute("mensajeExito", 
                "¡Compra realizada con éxito! Factura N° " + factura.getId());
            return "redirect:/carrito/pago";  
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/carrito/pago";
        }
    }
}
