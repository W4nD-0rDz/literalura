package com.aluracursos.literalura.service;

import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

@Service
public class AutorService {
    private Scanner input = new Scanner(System.in);
    private AutorRepository repositorio;

    @Autowired
    public AutorService(AutorRepository repositorio) {
        this.repositorio = repositorio;
    }

    //Opción 3 - Lista los autores de la BD
    public List<Autor> obtenerTodosLosAutores() {
        return repositorio.findAll();
    }

    //Opción 4 - Lista autores por fecha
    public List<Autor> listarAutoresVivosEn() {
        System.out.println("Ingrese una año para buscar autores");
        int year = parseInt(input.nextLine());
        System.out.println("AÑO: " + year);
        List<Autor> autoresVivosEn = repositorio.findAutoresVivosEn(year);
        return autoresVivosEn;
    }

    //Opción 5 - Lista autores por nombre
    public List<Autor> buscarAutorPorNombre() {
        System.out.println("Ingrese al menos una letra para buscar el autor");
        String nombreBuscado = String.valueOf(input.nextLine());
        List<Autor> autorPorNombre = repositorio.buscarPorNombre(nombreBuscado);
        return autorPorNombre;
    }
}
