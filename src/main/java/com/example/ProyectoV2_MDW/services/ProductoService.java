package com.example.ProyectoV2_MDW.services;

import com.example.ProyectoV2_MDW.model.Categoria;
import com.example.ProyectoV2_MDW.model.Producto;
import com.example.ProyectoV2_MDW.repository.ProductoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaService categoriaService;

    //usamos postconstruct para inicializar datos al arrancar la aplicacion
    @PostConstruct
    @Transactional
    public void inicializarDatosA(){

        if(productoRepository.count()==0){
            inicializarDatos();
        }
    }

    @Transactional
    public void inicializarDatos(){
        
        Categoria deportivos = categoriaService.crearCategoria(new Categoria(null,"Deportivos","relojes resistentes",null));
        Categoria clasicos = categoriaService.crearCategoria(new Categoria(null,"Clasicos","relojes elegantes",null));
        Categoria inteligentes = categoriaService.crearCategoria(new Categoria(null,"Inteligentes","Smartwatches con tecnologia avanzada",null));
        Categoria elegantes = categoriaService.crearCategoria(new Categoria(null,"Elegantes","Relojes de lujo",null));
        productoRepository.save(new Producto(null,"Reloj Deportivo","RD-001",new BigDecimal("50.00"), 15, "resistente al agua", "Reloj_Deportivo.png", deportivos,null,null,null));
        productoRepository.save(new Producto(null,"Reloj Clasico","RC-001",new BigDecimal("80.00"), 10, "corread cuero", "Reloj_Clasico.png", clasicos,null,null,null));
        productoRepository.save(new Producto(null,"Reloj inteligente","RI-001",new BigDecimal("150.00"), 20, "Smartwatch, notificaciones inteligentes", "Reloj_Inteligente.png", inteligentes,null,null,null));
        productoRepository.save(new Producto(null,"Reloj con Correa de Acero","RCA-001",new BigDecimal("120.00"), 5, "Sumergible, resistente a golpes", "reloj_correa_acero.png", elegantes,null,null,null));
        productoRepository.save(new Producto(null,"Reloj de muñeca","RM-002",new BigDecimal("150.00"), 8, "Comodo y resistente para el uso diario", "Reloj_Muneca.png", deportivos,null,null,null));
        productoRepository.save(new Producto(null,"Reloj Digital","RD-002",new BigDecimal("300.00"), 4, "Pantalla digital moderno y funcional", "Reloj_Digital.png", inteligentes,null,null,null));
        productoRepository.save(new Producto(null,"Reloj para Damas","RPD-001",new BigDecimal("700.00"), 6, "Diseño elegante y sotisficado", "reloj mujer_1.png", elegantes,null,null,null));
        productoRepository.save(new Producto(null,"Reloj Deportivo Femenino","RDF-001",new BigDecimal("600.00"), 2, "Resistentes y ligeros", "reloj mujer_2.png", deportivos,null,null,null));
        productoRepository.save(new Producto(null,"Reloj porta auriculares","RPA-001",new BigDecimal("850.00"), 12, "Diseño innovador ", "reloj_potaAuriculares.jpg", inteligentes,null,null,null));
    }

    @Transactional(readOnly = true)
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }
}
