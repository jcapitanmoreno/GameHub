package com.github.jcapitanmoreno.test;

import com.github.jcapitanmoreno.model.dao.UsuariosDAO;
import com.github.jcapitanmoreno.model.entity.Usuarios;

import java.sql.SQLException;
import java.util.Scanner;

public class TestUsuariosDAO {
    public static void main(String[] args) {
        UsuariosDAO usuariosDAO = new UsuariosDAO();
        Scanner scanner = new Scanner(System.in);

        try {
            // Insertar un nuevo usuario
            System.out.println("Introduce los datos del nuevo usuario:");
            System.out.print("Nombre de usuario: ");
            String nombreUsuario = scanner.nextLine();

            System.out.print("Contraseña: ");
            String contrasena = scanner.nextLine();

            System.out.print("¿Es administrador? (true/false): ");
            boolean isAdmin = Boolean.parseBoolean(scanner.nextLine());

            System.out.print("Correo electrónico: ");
            String correo = scanner.nextLine();

            Usuarios nuevoUsuario = new Usuarios(0, nombreUsuario, contrasena, correo, isAdmin);
            Usuarios usuarioGuardado = usuariosDAO.save(nuevoUsuario);

            if (usuarioGuardado.getId() > 0) {
                System.out.println("Usuario insertado correctamente con ID: " + usuarioGuardado.getId());
            } else {
                System.out.println("Error al insertar el usuario.");
                return;
            }

            // Eliminar el usuario recién insertado
            System.out.print("¿Quieres eliminar este usuario? (si/no): ");
            String respuesta = scanner.nextLine();

            if (respuesta.equalsIgnoreCase("si")) {
                Usuarios usuarioEliminado = usuariosDAO.delete(usuarioGuardado);
                if (usuarioEliminado != null) {
                    System.out.println("Usuario eliminado correctamente: " + usuarioEliminado.getUsuario());
                } else {
                    System.out.println("No se pudo eliminar el usuario.");
                }
            } else {
                System.out.println("Operación de eliminación cancelada.");
            }

        } catch (SQLException e) {
            System.err.println("Error en la operación: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
