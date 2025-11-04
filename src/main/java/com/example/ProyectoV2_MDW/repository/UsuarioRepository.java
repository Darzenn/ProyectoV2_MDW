package com.example.ProyectoV2_MDW.repository;

import com.example.ProyectoV2_MDW.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    //buscara un usuario por su correo
    Optional<Usuario> findByCorreo(String correo);
    //se fija si el correo existe para el registro
    boolean existsByCorreo(String correo);
}
