package com.aluracursos.literalura.service;

import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class Conversa {
    Scanner input = new Scanner(System.in);

    public boolean valida(){
        boolean validez = false;
        System.out.print("¿Desea guardar el libro en la base de datos? S/N");
        var respuesta = String.valueOf(input.nextLine());
        if(respuesta.equalsIgnoreCase("S")){
            validez = true; return validez;
        } else return validez;
    }







}
