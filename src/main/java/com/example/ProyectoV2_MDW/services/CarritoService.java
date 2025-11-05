package com.example.ProyectoV2_MDW.services;

import com.example.ProyectoV2_MDW.model.*;
import com.example.ProyectoV2_MDW.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final DetalleCarritoRepository detalleCarritoRepository;

    public CarritoService(CarritoRepository carritoRepository,
                          ProductoRepository productoRepository,
                          UsuarioRepository usuarioRepository,
                          DetalleCarritoRepository detalleCarritoRepository) {
        this.carritoRepository = carritoRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
        this.detalleCarritoRepository = detalleCarritoRepository;
    }

    // 🔹 Agregar producto al carrito
    @Transactional
    public void agregarProducto(Long idUsuario, Long idProducto, int cantidad) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Carrito carrito = usuario.getCarrito();
        if (carrito == null) {
            carrito = new Carrito();
            carrito.setUsuario(usuario);
            usuario.setCarrito(carrito);
        }

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        // Verificar si el producto ya existe en el carrito
        DetalleCarrito existente = carrito.getItems()
                .stream()
                .filter(dc -> dc.getProducto().getId().equals(producto.getId()))
                .findFirst()
                .orElse(null);

        if (existente != null) {
            existente.setCantidad(existente.getCantidad() + cantidad);
            existente.setSubtotal(producto.getPrecio()
                    .multiply(new BigDecimal(existente.getCantidad())));
        } else {
            DetalleCarrito nuevo = new DetalleCarrito(producto, cantidad);
            nuevo.setCarrito(carrito);
            carrito.getItems().add(nuevo);
        }

        carritoRepository.save(carrito);
    }

    // 🔹 Obtener carrito de un usuario
    public Carrito obtenerCarritoDeUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return usuario.getCarrito();
    }

    // 🔹 Eliminar un producto del carrito
    @Transactional
    public void eliminarDetalle(Long idDetalle) {
        detalleCarritoRepository.deleteById(idDetalle);
    }
}
