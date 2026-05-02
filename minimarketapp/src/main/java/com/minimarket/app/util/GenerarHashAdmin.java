package com.minimarket.app.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//SE UTILIZO PARA CREAR EL ADMIN CON LA CONTRASEÑA HASHEADA, AHORA YA LO HACE EL ADMIN DESDE
//SU PANEL

public class GenerarHashAdmin {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("1234"); // Contraseña que quieras para admin
        System.out.println("Hash: " + hash);
    }
}