package com.github.jcapitanmoreno.model.entity;

public class Disponible {
    private Videojuegos videojuego;
    private Plataformas plataforma;
    private String fechaLanzamiento;


    public Disponible() {
    }

    public Disponible(Videojuegos videojuego, Plataformas plataforma, String fechaLanzamiento) {
        this.videojuego = videojuego;
        this.plataforma = plataforma;
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public Videojuegos getVideojuego() {
        return videojuego;
    }

    public void setVideojuego(Videojuegos videojuego) {
        this.videojuego = videojuego;
    }

    public Plataformas getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(Plataformas plataforma) {
        this.plataforma = plataforma;
    }

    public String getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public void setFechaLanzamiento(String fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }

    @Override
    public String toString() {
        return "Disponible{" +
                "videojuego=" + videojuego +
                ", plataforma=" + plataforma +
                ", fechaLanzamiento='" + fechaLanzamiento + '\'' +
                '}';
    }
}
