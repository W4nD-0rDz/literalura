package com.aluracursos.literalura.service;

import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.model.DatosLibro;
import com.aluracursos.literalura.model.Libro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class Muestra {
    @Autowired
    private LibroService servicio;
    @Autowired
    private AutorService servicio1;

    //Acá van los métodos de impresión por pantalla (los sout)
    public void muestraLibro(Libro libro) {
        System.out.println(libro.getTitulo().toUpperCase() + " (de: " + generaStringDeAutores(libro.getAutores()) + ")");
    }

    public void muestraLibro(DatosLibro datosLibro) {
        System.out.println(datosLibro.titulo().toUpperCase() + " (de: " + datosLibro.autores() + ")");
    }

    public String generaStringDeAutores(List<Autor> autores) {
        StringBuilder stringDeAutores = new StringBuilder();
        if (autores.size() > 1) {
            autores.forEach(autor -> stringDeAutores.append(autor.getNombre()).append("; "));
            // Eliminar la última coma y espacio
            stringDeAutores.setLength(stringDeAutores.length() - 2);
        } else if (autores.size() == 1) {
            stringDeAutores.append(autores.get(0).getNombre());
        } else {
            stringDeAutores.append("autor desconocido");
        }
        return stringDeAutores.toString();
    }


    public void muestraMapaDatosLibro(Map<Integer, DatosLibro> mapaDeDatosLibro) {
        mapaDeDatosLibro.forEach((Integer, DatosLibro) ->
                System.out.println("[" + Integer + "] " + DatosLibro.titulo().toUpperCase() + " - " + DatosLibro.autores()));
    }

    public void muestraMapa(Map<Integer, Libro> mapaDeLibros) {
        mapaDeLibros.forEach((index, libro) -> {
            System.out.print("[" + index + "] ");
            muestraLibro(libro);
        });
    }

    public void muestraListaDeLibros() {
        List<Libro> listaDeLibros = servicio.obtenerTodoLosLIbros();
        listaDeLibros.stream()
                .forEach(System.out::println);
    }

    public void muestraAutores() {
        List<Autor> autores = servicio1.obtenerTodosLosAutores();
        mostrarAutores(autores, "No hay autores listados en la base de datos");
    }

    public void muestraAutoresVivosEn() {
        List<Autor> autores = servicio1.listarAutoresVivosEn();
        mostrarAutores(autores, "La base de datos no cuenta con autores para ese periodo");
    }

    public void muestraAutoresPorNombre(){
        List<Autor> autores = servicio1.buscarAutorPorNombre();
        mostrarAutores(autores, "No hay autores con ese nombre");
    }

    private void mostrarAutores(List<Autor> autores, String mensajeVacio) {
        if (!autores.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            autores.forEach(a -> sb.append(a).append(System.lineSeparator()));
            System.out.print(sb);
        } else {
            System.out.println(mensajeVacio);
        }
    }



}
