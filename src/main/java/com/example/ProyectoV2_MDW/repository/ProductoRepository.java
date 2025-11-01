package com.example.ProyectoV2_MDW.repository;

import com.example.ProyectoV2_MDW.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
