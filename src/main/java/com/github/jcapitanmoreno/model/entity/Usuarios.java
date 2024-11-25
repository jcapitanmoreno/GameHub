package com.github.jcapitanmoreno.model.entity;

public class Usuarios {
    private int id;
    private String usuario;
    private String contrasena;
    private String correo;
    private boolean isAdmin;

    public Usuarios() {
    }

    public Usuarios(int id, String usuario, String contrasena, String correo, boolean isAdmin) {
        this.id = id;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.correo = correo;
        this.isAdmin = isAdmin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return "Usuarios{" +
                "id=" + id +
                ", usuario='" + usuario + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", correo='" + correo + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
