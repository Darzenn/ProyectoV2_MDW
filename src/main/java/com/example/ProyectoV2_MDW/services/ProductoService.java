package com.example.ProyectoV2_MDW.services;

import com.example.ProyectoV2_MDW.model.Categoria;
import com.example.ProyectoV2_MDW.model.Producto;
import com.example.ProyectoV2_MDW.repository.ProductoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaService categoriaService;

    @PostConstruct
    @Transactional
    public void inicializarDatosA(){

        if(productoRepository.count()==0){
            inicializarDatos();
        }
    }

    @Transactional
    public void inicializarDatos(){
        
        Categoria deportivos = categoriaService.crearCategoria(new Categoria("Deportivos","relojes resistentes"));
        
        productoRepository.save(new Producto("Reloj Deportivo","RD-001",new BigDecimal("50.00"), 15, "resistente al agua", "Reloj_Deportivo.png", deportivos));
    }




    @Transactional(readOnly = true)
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

     @Transactional(readOnly = true)
    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Producto> obtenerProductosPorCategoriaId(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }
}
