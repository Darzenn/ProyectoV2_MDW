package com.example.ProyectoV2_MDW.services;

import com.example.ProyectoV2_MDW.model.Categoria;
import com.example.ProyectoV2_MDW.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Transactional
    public Categoria crearCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

}
