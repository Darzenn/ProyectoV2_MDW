package com.example.ProyectoV2_MDW.services;

import com.example.ProyectoV2_MDW.model.Usuario;
import com.example.ProyectoV2_MDW.model.Carrito;
import com.example.ProyectoV2_MDW.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    //Registrar nuevo usuario
    @Transactional
    public Usuario registrarUsuario(Usuario usuario) {
        
        // Verificar si el correo ya existe
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado.");
        }
        
        // Crear un carrito vacio para el nuevo usuario
        Carrito carritoNuevo = new Carrito();
        carritoNuevo.setUsuario(usuario);
        usuario.setCarrito(carritoNuevo);
        
        // Guardar usuario (el carrito se guardara automaticamente por CascadeType.ALL)
        return usuarioRepository.save(usuario);
    }

    //Login de usuario
    @Transactional(readOnly = true)
    public Optional<Usuario> loginUsuario(String correo, String contrasena) {
        
        // Buscar usuario por correo
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
        
        // Verificar si existe y si la contraseña coincide
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            
            // Comparacion simple de contraseña 
            if (usuario.getContrasena().equals(contrasena)) {
                return Optional.of(usuario);
            }
        }
        
        return Optional.empty(); // el login fallo
    }

    // Obtener usuario por ID
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    // Obtener usuario por correo
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerUsuarioPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }


}
