package com.github.jcapitanmoreno.model.entity;

import java.util.ArrayList;
import java.util.List;

public class Disponible {
    private Videojuegos videojuego;
    private List<Plataformas> plataforma;
    private String fechaLanzamiento;


    public Disponible() {
    }

    public Disponible(Videojuegos videojuego, ArrayList<Plataformas> plataforma, String fechaLanzamiento) {
        this.videojuego = videojuego;
        this.plataforma = plataforma;
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public Disponible(Videojuegos videojuego, List<Plataformas> plataforma, String fechaLanzamiento) {
        this.videojuego = videojuego;
        this.plataforma = plataforma;
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public Disponible(int videojuegoId, int idPlataforma, String fechaLanzamiento) {


    }

    public Videojuegos getVideojuego() {
        return videojuego;
    }

    public void setVideojuego(Videojuegos videojuego) {
        this.videojuego = videojuego;
    }

    public List<Plataformas> getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(ArrayList<Plataformas> plataforma) {
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
