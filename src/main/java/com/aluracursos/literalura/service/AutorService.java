package com.aluracursos.literalura.service;

import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public List<Autor> obtenerTodosLosAutores(){
        return repositorio.findAll();
    }

    public List<Autor> listarAutoresVivosEn(){
        System.out.println("Ingrese una año para buscar autores");
        int year = parseInt(input.nextLine());
        System.out.println("AÑO: " + year);
        List<Autor> autoresVivosEn = repositorio.findAutoresVivosEn(year);
        return autoresVivosEn;
    }

    public List<Autor> buscarAutorPorNombre(){
        System.out.println("Ingrese al menos una letra para buscar el autor");
        String nombreBuscado = String.valueOf(input.nextLine());
        List<Autor> autorPorNombre = repositorio.buscarPorNombre(nombreBuscado);
        return autorPorNombre;
    }
    }
