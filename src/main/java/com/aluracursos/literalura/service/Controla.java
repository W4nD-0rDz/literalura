package com.aluracursos.literalura.service;

import com.aluracursos.literalura.model.Autor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Controla {
    @Autowired
    private LibroService servicio;
    @Autowired
    private AutorService servicio1;

    private Imprime impresor = new Imprime();

    //Opción 2
    public void muestraTodosLosLibros(){
        impresor.mostrarElemento(servicio.obtenerTodosLosLibros());
    }
    //Opción 3
    public void muestraAutores() {
        mostrarAutores(servicio1.obtenerTodosLosAutores(), "No hay autores listados en la base de datos");
    }
    //Opción 4
    public void muestraAutoresVivosEn() {
        mostrarAutores(servicio1.listarAutoresVivosEn(), "La base de datos no cuenta con autores para ese periodo");
    }
    //Opción 5
    public void muestraAutoresPorNombre(){
        mostrarAutores(servicio1.buscarAutorPorNombre(), "No hay autores con ese nombre");
    }
    //Opción 4 y 5
    private void mostrarAutores(List<Autor> autores, String mensajeVacio) {
        if (!autores.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            autores.forEach(a -> sb.append(a).append(System.lineSeparator()));
            System.out.print(sb);
        } else {
            System.out.println(mensajeVacio);
        }
    }
    //Opción 6
    public void muestraLibrosPorIdioma(){
        impresor.mostrarElemento(servicio.buscaLibrosPorIdioma());
    }
    //Opción 7
    public void muestra10LibrosMasDescargados(){
        impresor.mostrarElemento(servicio.librosMasDescargados());
    }
    //Opción 8
    public void muestraLibrosPorAutor(){
        impresor.mostrarElemento(servicio.buscaLibrosPorAutor());
    }
}