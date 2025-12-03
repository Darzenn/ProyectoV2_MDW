package com.example.ProyectoV2_MDW.services;

import com.example.ProyectoV2_MDW.jwt.JwtUtil;
import com.example.ProyectoV2_MDW.model.Usuario;
import com.example.ProyectoV2_MDW.model.Carrito;
import com.example.ProyectoV2_MDW.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Autowired
    private JwtUtil jwtUtil;


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
    public Optional<String> loginUsuario(String correo, String contrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            if (passwordEncoder.matches(contrasena, usuario.getContrasena())) {
                // Generar token JWT usando el correo como "username"
                String token = jwtUtil.generateToken(
                        org.springframework.security.core.userdetails.User
                                .withUsername(usuario.getCorreo())
                                .password(usuario.getContrasena())
                                .authorities("USER") // puedes ajustar roles
                                .build()
                );
                return Optional.of(token);
            }
        }
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    // Metodo para cargar usuario por correo y devolver UserDetails
    public UserDetails loadUserByUsername(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));

        return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getCorreo())
                .password(usuario.getContrasena())
                .authorities("USER") // Ajusta roles según tu modelo
                .build();
    }
}