package com.aluracursos.literalura.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Muestra {
    @Autowired
    private LibroService servicio;

        //Acá van los métodos de impresión por pantalla (los sout)
}
