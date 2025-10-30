package com.example.ProyectoV2_MDW.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginaController {

    @GetMapping("/")
    public String index() {

        return "index.html";
    }

    @GetMapping("/productos")
    public String productos() {

        return "productos.html";
    }

}
