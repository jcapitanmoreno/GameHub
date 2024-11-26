package com.github.jcapitanmoreno.model.dao;

import com.github.jcapitanmoreno.model.connection.ConnectionXamp;
import com.github.jcapitanmoreno.model.entity.Genero;
import com.github.jcapitanmoreno.model.entity.Usuarios;
import com.github.jcapitanmoreno.model.entity.Videojuegos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VideojuegosDAO {
    private final static String INSERT = "INSERT INTO videojuegos (nombre, descripcion, enlaceTrailer, idGenero, idUsuarios) VALUES (?, ?, ?, ?, ?)";
    private final static String UPDATE = "UPDATE videojuegos SET nombre = ?, descripcion = ?, enlaceTrailer = ?, idGenero = ?, idUsuarios = ? WHERE id = ?";
    private final static String DELETE = "DELETE FROM videojuegos WHERE id = ?";
    private final static String SELECT_ALL = "SELECT v.*, g.nombre AS genero_nombre, u.usuario AS usuario_nombre, u.correo AS usuario_correo " +
            "FROM videojuegos v " +
            "JOIN genero g ON v.idGenero = g.id " +
            "JOIN usuarios u ON v.idUsuarios = u.id";
    private final static String SELECT_BY_ID = "SELECT v.*, g.nombre AS genero_nombre, u.usuario AS usuario_nombre, u.correo AS usuario_correo " +
            "FROM videojuegos v " +
            "JOIN genero g ON v.idGenero = g.id " +
            "JOIN usuarios u ON v.idUsuarios = u.id " +
            "WHERE v.id = ?";



    public Videojuegos save(Videojuegos videojuego) throws SQLException {
        if (videojuego.getId() == 0) {
            try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, videojuego.getNombre());
                statement.setString(2, videojuego.getDescripcion());
                statement.setString(3, videojuego.getEnlaceTrailer());
                statement.setString(4, videojuego.getGenero().getNombre());
                statement.setString(5, videojuego.getUsuario().getUsuario());
                statement.executeUpdate();


                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        videojuego.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } else {
            try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(UPDATE)) {
                statement.setString(1, videojuego.getNombre());
                statement.setString(2, videojuego.getDescripcion());
                statement.setString(3, videojuego.getEnlaceTrailer());
                statement.setString(4, videojuego.getGenero().getNombre());
                statement.setString(5, videojuego.getUsuario().getUsuario());
                statement.setInt(6, videojuego.getId());
                statement.executeUpdate();
            }
        }
        return videojuego;
    }

    public Videojuegos delete(Videojuegos videojuego) throws SQLException {
        if (videojuego == null || videojuego.getId() == 0) {
            return null;
        } else {
            try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(DELETE)) {
                statement.setInt(1, videojuego.getId());
                statement.executeUpdate();
            }
        }
        return videojuego;
    }
    public Videojuegos findById(int id) throws SQLException {
        try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(SELECT_BY_ID)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToVideojuego(resultSet);
                }
            }
        }
        return null;
    }

    public List<Videojuegos> findAll() throws SQLException {
        List<Videojuegos> videojuegos = new ArrayList<>();
        try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                videojuegos.add(mapResultSetToVideojuego(resultSet));
            }
        }
        return videojuegos;
    }
    private Videojuegos mapResultSetToVideojuego(ResultSet resultSet) throws SQLException {
        Genero genero = new Genero(
                resultSet.getInt("idGenero"),
                resultSet.getString("genero_nombre")
        );

        Usuarios usuario = new Usuarios(
                resultSet.getInt("idUsuarios"),
                resultSet.getString("usuario_nombre"),
                null,
                resultSet.getString("usuario_correo"),
                false // Este campo se debe manejar para saber si es admin o no
        );

        return new Videojuegos(
                resultSet.getInt("id"),
                resultSet.getString("nombre"),
                resultSet.getString("descripcion"),
                resultSet.getString("enlaceTrailer"),
                genero,
                usuario
        );
    }

}
