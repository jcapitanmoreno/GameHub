package com.github.jcapitanmoreno.model.entity;

public class Videojuegos {
    private int id;
    private String nombre;
    private String descripcion;
    private String enlaceTrailer;
    private Genero genero;
    private Usuarios usuario;
    private Disponible disponible;

    public Videojuegos() {
    }

    public Videojuegos(int id, String nombre, String descripcion, String enlaceTrailer, Genero genero, Usuarios usuario,Disponible disponible) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.enlaceTrailer = enlaceTrailer;
        this.genero = genero;
        this.usuario = usuario;
        this.disponible = disponible;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEnlaceTrailer() {
        return enlaceTrailer;
    }

    public void setEnlaceTrailer(String enlaceTrailer) {
        this.enlaceTrailer = enlaceTrailer;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public Disponible getDisponible() {
        return disponible;
    }

    public void setDisponible(Disponible disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "Videojuegos{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", enlaceTrailer='" + enlaceTrailer + '\'' +
                ", genero=" + genero +
                ", usuario=" + usuario +
                ", disponible=" + disponible +
                '}';
    }
}
