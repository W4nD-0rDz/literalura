package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.service.Busca;
import com.aluracursos.literalura.service.Muestra;

import java.util.Scanner;

public class Principal {

    Busca buscador = new Busca();
    Muestra mostrador = new Muestra();
    Scanner input = new Scanner(System.in);

    String menuDelUsuario = """
            ********************************
            Bienvenido a la Biblioteca Alura

            1 - Buscar Libro por Título
            2 - Listar libros registrados
            3 - Listar autores registrados
            4 - Listar autores por fecha
            5 - Listar libros por idioma
            0 - salir
            *********************************
            """;

    //Menu del usuario
    public void menu() {
        int option;
        do {
            System.out.println(menuDelUsuario);
            System.out.print("Por favor, seleccione una acción a realizar: ");
            option = Integer.parseInt(input.nextLine());
            switch (option) {
                case 1:
                    buscador.buscaLibros();
                    break;
                case 2:
                    mostrador.todosLosLibros();
                    break;
                case 3:
                    mostrador.todosLosAutores();
                    break;
                case 4:
                    mostrador.autoresPorFecha();
                    break;
                case 5:
                    mostrador.librosPorIdioma();
                    break;

                case 0:
                    System.out.println("Hasta pronto. \n¡Gracias por visitar Biblioteca Alura!");
                    break;
                default:
                    System.out.println("Elija una opción válida");
                    break;
            }
        } while (option != 0);
    }
}
}
