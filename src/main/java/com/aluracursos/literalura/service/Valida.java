package com.aluracursos.literalura.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Valida {
    private final Scanner input = new Scanner(System.in);

    public boolean valida(){
        ArrayList<String> respuestasValidas = new ArrayList<>(Arrays.asList("si", "SI", "s", "S", "Y", "Ye", "Yes", "yes", "yeah", "YES", "YEAH"));
        System.out.println("¿Desea almacenar el libro en la base de datos? S/N");
        String respuesta = String.valueOf(input.nextLine());
        if(respuestasValidas.contains(respuesta)){
            return true;
        }else{
            return false;
        }

    }

}
