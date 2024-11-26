package com.github.jcapitanmoreno.model.dao;

public class VideojuegosDAO {
    private final static String INSERT = "INSERT INTO videojuegos (nombre, genero_id, plataforma_id, fecha_lanzamiento, usuario_id) VALUES (?, ?, ?, ?, ?)";
    private final static String UPDATE = "UPDATE videojuegos SET nombre = ?, genero_id = ?, plataforma_id = ?, fecha_lanzamiento = ?, usuario_id = ? WHERE id = ?";
    private final static String DELETE = "DELETE FROM videojuegos WHERE id = ?";
    private final static String SELECT_ALL = "SELECT * FROM videojuegos";
    private final static String SELECT_BY_ID = "SELECT * FROM videojuegos WHERE id = ?";


}
