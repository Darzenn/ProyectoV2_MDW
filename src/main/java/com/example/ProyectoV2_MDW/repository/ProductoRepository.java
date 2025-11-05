package com.example.ProyectoV2_MDW.repository;

import com.example.ProyectoV2_MDW.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    //busca productos por categoria
    List<Producto> findByCategoriaId(Long categoriaId);
}
