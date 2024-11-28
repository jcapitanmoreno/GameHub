package com.github.jcapitanmoreno.model.entity;

public class Plataformas {
    private int id;
    private String nombre;

    private Disponible disponible;

    public Plataformas() {
    }

    public Plataformas(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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

    @Override
    public String toString() {
        return "Plataformas{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
