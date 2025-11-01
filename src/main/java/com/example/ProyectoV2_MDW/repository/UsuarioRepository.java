package com.example.ProyectoV2_MDW.repository;

import com.example.ProyectoV2_MDW.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
