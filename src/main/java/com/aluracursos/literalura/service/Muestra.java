package com.aluracursos.literalura.service;

import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.model.DatosLibro;
import com.aluracursos.literalura.model.Libro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class Muestra {
    @Autowired
    private LibroService servicio;
    @Autowired
    private AutorService servicio1;

    public void muestraTodosLosLibros(){
        List<Libro> listaDeLibros = servicio.obtenerTodosLosLibros();
        muestraLista(listaDeLibros);
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



    public void muestraLibrosPorIdioma(){
        List<Libro>listaDeLibrosPorIdioma = servicio.buscaLibrosPorIdioma();
        muestraLista(listaDeLibrosPorIdioma);

    }

    public void muestra10LibrosMasDescargados(){
        List<Libro>listaLibrosMasDescargados = servicio.librosMasDescargados();
        muestraLista(listaLibrosMasDescargados);
    }

    public void muestraLibrosPorAutor(){
        List<Libro>listaDeLibrosPorAutor = servicio.buscaLibrosPorAutor();
        muestraLista(listaDeLibrosPorAutor);
    }


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
            // Borra el último punto y coma y el espacio
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

    public <T> void muestraLista(List<T> lista) {
        lista.stream()
                .forEach(System.out::println);
    }


    //Este método muestra el contenido de mapas
    public <T> void muestraGenerico(Map<Integer, T> mapa) {
        for (Map.Entry<Integer, T> entrada : mapa.entrySet()) {
            Integer indice = entrada.getKey();
            T valor = entrada.getValue();
            String info;
            if (valor instanceof String[]) {
                String[] dato = (String[]) valor;
                info = Arrays.toString(dato);
            } else {
                info = valor.toString();
            }
            System.out.println(indice + " - " + info);
        }
        System.out.println(" ");
    }




}
