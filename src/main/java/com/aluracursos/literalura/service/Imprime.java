package com.aluracursos.literalura.service;

import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.model.DatosLibro;
import com.aluracursos.literalura.model.Libro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Imprime {
    //Acá van los métodos de impresión por consola (los sout)
    public void mostrarElemento(Object elemento) {
        if (elemento instanceof Map) {
            mostrarMapa((Map<?, ?>) elemento);
        } else if(elemento instanceof ArrayList<?>){
            mostrarArrayList((ArrayList<?>) elemento);
        } else if (elemento instanceof DatosLibro) {
            DatosLibro datosLibro = (DatosLibro) elemento;
            System.out.println(datosLibro.titulo().toUpperCase() + " (de: " + datosLibro.autores() + ")");
        } else if (elemento instanceof Libro) {
            Libro libro = (Libro) elemento;
            System.out.println(libro.getTitulo().toUpperCase() + " (de: " + generaStringDeAutores(libro.getAutores()) + ")");
        } else if (elemento instanceof Autor) {
            Autor autor = (Autor) elemento;
            System.out.println(autor);
        } else {
            System.out.println("Tipo de objeto no soportado: " + elemento.getClass());
        }
    }

    private void mostrarMapa(Map<?, ?> mapa) {
        mapa.forEach((indice, valor) -> {
            String info = valor instanceof String[] ? Arrays.toString((String[]) valor) : valor.toString();
            System.out.println(indice + " - " + info);
        });
        System.out.println();
    }

    private <T> void mostrarArrayList(ArrayList<T> lista) {
        for (int i = 0; i < lista.size(); i++) {
            T elemento = lista.get(i);
            System.out.println("[" + (i+1) +"] " + elemento.toString());
        }
        System.out.println();
    }

    private String generaStringDeAutores(List<Autor> autores) {
        return autores.stream()
                .map(Autor::getNombre)
                .collect(Collectors.joining(", "));
    }
}
