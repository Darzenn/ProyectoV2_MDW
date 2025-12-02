package com.example.ProyectoV2_MDW.services;

import com.example.ProyectoV2_MDW.model.Usuario;
import com.example.ProyectoV2_MDW.model.Carrito;
import com.example.ProyectoV2_MDW.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Registrar nuevo usuario
    @Transactional
    public Usuario registrarUsuario(Usuario usuario) {
        
        // Verificar si el correo ya existe
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado.");
        }
        
        //  CIFRAR la contraseña antes de guardar
        String contrasenaEncriptada = passwordEncoder.encode(usuario.getContrasena());
        usuario.setContrasena(contrasenaEncriptada);
        
        // Crear un carrito vacío para el nuevo usuario
        Carrito carritoNuevo = new Carrito();
        carritoNuevo.setUsuario(usuario);
        usuario.setCarrito(carritoNuevo);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        
        return usuarioGuardado;
    }

    // Login de usuario
    @Transactional(readOnly = true)
    public Optional<Usuario> loginUsuario(String correo, String contrasena) {
        
        // Buscar usuario por correo
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
        
        // Verificar si existe y si la contraseña coincide
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            
            //  COMPARAR contraseña ingresada con la cifrada
            if (passwordEncoder.matches(contrasena, usuario.getContrasena())) {
                return Optional.of(usuario);
            }
        }
        
        return Optional.empty(); // el login falló
    }
}