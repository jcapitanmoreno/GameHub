package com.github.jcapitanmoreno.model.dao;

import com.github.jcapitanmoreno.model.connection.ConnectionXamp;
import com.github.jcapitanmoreno.model.entity.Usuarios;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuariosDAO {

    private final static String INSERT = "INSERT INTO usuarios (usuario, contrasena, isAdmin, correo) VALUES (?, ?, ?, ?)";
    private final static String UPDATE = "UPDATE usuarios SET usuario = ?, contrasena = ?, isAdmin = ?, correo = ? WHERE id = ?";
    private final static String DELETE = "DELETE FROM usuarios WHERE id = ?";
    private final static String SELECT_ALL = "SELECT * FROM usuarios";
    private final static String SELECT_BY_ID = "SELECT * FROM usuarios WHERE id = ?";


    public Usuarios save(Usuarios entity) throws SQLException {
        if (entity.getId() == 0) {
            try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, entity.getUsuario());
                statement.setString(2, entity.getContrasena());
                statement.setBoolean(3, entity.isAdmin());
                statement.setString(4, entity.getCorreo());
                statement.executeUpdate();

                // Recuperar el ID generado automáticamente
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        entity.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } else {
            try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(UPDATE)) {
                statement.setString(1, entity.getUsuario());
                statement.setString(2, entity.getContrasena());
                statement.setBoolean(3, entity.isAdmin());
                statement.setString(4, entity.getCorreo());
                statement.setInt(5, entity.getId());
                statement.executeUpdate();
            }
        }
        return entity;
    }

    public Usuarios delete(Usuarios entity) throws SQLException {
        if(entity==null || entity.getId()==-1){
            entity = null;
        }else {
            try(PreparedStatement pst = ConnectionXamp.getConnection().prepareStatement(DELETE)) {
                pst.setInt(1,entity.getId());
                pst.executeUpdate();
            }
        }
        return entity;
    }

    public Usuarios findById(int id) throws SQLException {
        try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(SELECT_BY_ID)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToUsuario(resultSet);
                }
            }
        }
        return null;
    }

    public List<Usuarios> findAll() throws SQLException {
        List<Usuarios> usuarios = new ArrayList<>();
        try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                usuarios.add(mapResultSetToUsuario(resultSet));
            }
        }
        return usuarios;
    }

    private Usuarios mapResultSetToUsuario(ResultSet resultSet) throws SQLException {
        return new Usuarios(
                resultSet.getInt("id"),
                resultSet.getString("usuario"),
                resultSet.getString("contrasena"),
                resultSet.getString("correo"),
                resultSet.getBoolean("isAdmin")
        );
    }

}

