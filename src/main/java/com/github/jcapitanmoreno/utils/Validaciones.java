package com.github.jcapitanmoreno.utils;

/**
 * The Validations class provides static methods to validate input fields such as
 * username, email, password, and admin password. If any validation fails, a custom
 * ValidationException is thrown with an error message.
 */
public class Validaciones {
    private static final String CLAVE_ADMIN_VALIDA = "1234"; // Clave fija para administrador

    /**
     * Validates input fields such as username, email, password, and admin password
     * if necessary. If any validation fails, a ValidationException is thrown with the
     * error message.
     *
     * @param usuario The username to validate.
     * @param correo The email address to validate.
     * @param contrasena The password to validate.
     * @param isAdmin Whether the user is an admin.
     * @param claveAdmin The admin password if the user is an admin.
     * @throws ValidationException If any of the validations fail.
     */
    public static void validarCampos(String usuario, String correo, String contrasena, boolean isAdmin, String claveAdmin) throws ValidationException {
        if (usuario.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || (isAdmin && claveAdmin.isEmpty())) {
            throw new ValidationException("Por favor, completa todos los campos requeridos.");
        }

        if (!validarCorreo(correo)) {
            throw new ValidationException("Por favor, introduce un correo electrónico válido.");
        }

        if (!validarLongitudContrasena(contrasena)) {
            throw new ValidationException("La contraseña debe tener al menos 8 caracteres.");
        }

        if (!validarComplejidadContrasena(contrasena)) {
            throw new ValidationException("La contraseña debe contener al menos una letra mayúscula, una letra minúscula, un número y un carácter especial.");
        }

        if (!validarUsuario(usuario)) {
            throw new ValidationException("El nombre de usuario solo puede contener letras, números y guiones bajos, y debe tener entre 3 y 20 caracteres.");
        }

        if (isAdmin && !validarClaveAdmin(claveAdmin)) {
            throw new ValidationException("La clave de administrador es incorrecta.");
        }
    }

    /**
     * Validates if the email address is in a valid format using a regular expression.
     *
     * @param correo The email address to validate.
     * @return true if the email is valid, false otherwise.
     */
    public static boolean validarCorreo(String correo) {
        return correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    /**
     * Checks if the password has at least 8 characters.
     *
     * @param contrasena The password to validate.
     * @return true if the password is at least 8 characters long, false otherwise.
     */
    public static boolean validarLongitudContrasena(String contrasena) {
        return contrasena.length() >= 8;
    }

    /**
     * Checks if the password meets the complexity requirements (uppercase, lowercase, number,
     * and special character) and has a minimum length of 8 characters.
     *
     * @param contrasena The password to validate.
     * @return true if the password meets all complexity requirements, false otherwise.
     */
    public static boolean validarComplejidadContrasena(String contrasena) {
        return contrasena.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#]).{8,}$");
    }

    /**
     * Checks if the username contains only letters, numbers, and underscores, and is between
     * 3 and 20 characters long.
     *
     * @param usuario The username to validate.
     * @return true if the username is valid, false otherwise.
     */
    public static boolean validarUsuario(String usuario) {
        return usuario.matches("^[A-Za-z0-9_]{3,20}$");
    }

    /**
     * Validates if the admin password is correct. In this case, the valid password is "1234".
     *
     * @param claveAdmin The admin password to validate.
     * @return true if the password is correct, false otherwise.
     */
    public static boolean validarClaveAdmin(String claveAdmin) {
        return claveAdmin.equals(CLAVE_ADMIN_VALIDA);
    }

    /**
     * Inner class that defines a custom exception for validation errors.
     */
    public static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }
}
