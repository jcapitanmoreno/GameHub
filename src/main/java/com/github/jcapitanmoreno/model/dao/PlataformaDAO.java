package com.github.jcapitanmoreno.model.dao;

import com.github.jcapitanmoreno.model.connection.ConnectionXamp;
import com.github.jcapitanmoreno.model.entity.Plataformas;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PlataformaDAO {

    private final static String INSERT = "INSERT INTO plataformas (nombre) VALUES (?)";
    private final static String UPDATE = "UPDATE plataformas SET nombre = ? WHERE id = ?";
    private final static String DELETE = "DELETE FROM plataformas WHERE id = ?";
    private final static String SELECT_ALL = "SELECT * FROM plataformas";
    private final static String SELECT_BY_ID = "SELECT * FROM plataformas WHERE id = ?";
    private final static String SELECT_BY_NOMBRE = "SELECT * FROM plataformas WHERE nombre = ?";

    public static Plataformas save(Plataformas plataforma) throws SQLException {
        if (plataforma.getId() == 0) {
            try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, plataforma.getNombre());
                statement.executeUpdate();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        plataforma.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } else {
            try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(UPDATE)) {
                statement.setString(1, plataforma.getNombre());
                statement.setInt(2, plataforma.getId());
                statement.executeUpdate();
            }
        }
        return plataforma;
    }

    public static Plataformas delete(Plataformas plataforma) throws SQLException {
        if (plataforma == null || plataforma.getId() == 0) {
            plataforma = null;
        } else {
            try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(DELETE)) {
                statement.setInt(1, plataforma.getId());
                statement.executeUpdate();
            }
        }
        return plataforma;
    }
    public Plataformas findById(int id) throws SQLException {
        try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(SELECT_BY_ID)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToPlataformas(resultSet);
                }
            }
        }
        return null;
    }
    public List<Plataformas> findAll() throws SQLException {
        List<Plataformas> plataformas = new ArrayList<>();
        try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                plataformas.add(mapResultSetToPlataformas(resultSet));
            }
        }
        return plataformas;
    }
    public Plataformas findByName(String id) throws SQLException {
        try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(SELECT_BY_NOMBRE)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToPlataformas(resultSet);
                }
            }
        }
        return null;
    }

    private Plataformas mapResultSetToPlataformas(ResultSet resultSet) throws SQLException {
        return new Plataformas(
                resultSet.getInt("id"),
                resultSet.getString("nombre")
        );
    }

}
