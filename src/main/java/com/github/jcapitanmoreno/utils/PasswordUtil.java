package com.github.jcapitanmoreno.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {
    /**
     * Método que genera un hash SHA-256 a partir de una contraseña dada.
     *
     * @param password La contraseña en texto plano que se desea hashear.
     * @return El hash de la contraseña en formato hexadecimal.
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder hashString = new StringBuilder();
            for (byte b : hashBytes) {
                hashString.append(String.format("%02x", b));
            }
            return hashString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al crear hash de contraseña", e);
        }
    }
}

