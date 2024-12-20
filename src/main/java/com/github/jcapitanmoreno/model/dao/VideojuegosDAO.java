package com.github.jcapitanmoreno.model.dao;

import com.github.jcapitanmoreno.model.connection.ConnectionXamp;
import com.github.jcapitanmoreno.model.entity.*;
import com.github.jcapitanmoreno.model.singleton.UsuarioSingleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing the "videojuegos" table.
 * Provides CRUD operations for the "videojuegos" entity.
 */
public class VideojuegosDAO {
    private final static String INSERT = "INSERT INTO videojuegos (nombre, descripcion, enlaceTrailer, idGenero, idUsuarios) VALUES (?, ?, ?, ?, ?)";
    private final static String UPDATE = "UPDATE videojuegos SET nombre = ?, descripcion = ?, enlaceTrailer = ?, idGenero = ?, idUsuarios = ? WHERE id = ?";
    private final static String DELETE = "DELETE FROM videojuegos WHERE nombre = ?";
    private final static String SELECT_ALL = "SELECT v.id, v.nombre, v.descripcion, v.enlaceTrailer, g.nombre AS genero_nombre," +
            "u.usuario AS usuario_nombre, u.correo AS usuario_correo " +
            "FROM videojuegos v" +
            "JOIN genero g ON v.idGenero = g.id" +
            "JOIN usuarios u ON v.idUsuarios = u.id";
    private final static String SELECT_BY_ID = "SELECT v.id, v.nombre, v.descripcion, v.enlaceTrailer, g.nombre AS genero_nombre, \n" +
            "       u.usuario AS usuario_nombre, u.correo AS usuario_correo " +
            "FROM videojuegos v" +
            "JOIN genero g ON v.idGenero = g.id" +
            "JOIN usuarios u ON v.idUsuarios = u.id " +
            "WHERE v.id = ?";
    private final static String INSERT_FULL = "INSERT INTO videojuegos (nombre, descripcion, enlaceTrailer, idGenero, idUsuarios) VALUES (?, ?, ?, ?, ?)";
    private final static String INSERT_DISPONIBLE = "INSERT INTO disponible (idVideojuego, idPlataforma, fechaLanzamiento) VALUES (?, ?, ?)";

    private final static String GET_DATA = "SELECT v.nombre AS titulo, v.descripcion AS descripcion, v.enlaceTrailer AS enlaceTrailer, v.id AS id, " +
            "g.nombre AS genero, " +
            "p.nombre AS plataforma, " +
            "d.fechaLanzamiento AS fecha, " +
            "u.usuario AS usuario " +
            "FROM videojuegos v " +
            "JOIN genero g ON v.idGenero = g.id " +
            "JOIN usuarios u ON v.idUsuarios = u.id " +
            "JOIN disponible d ON v.id = d.idVideojuego " +
            "JOIN plataformas p ON d.idPlataforma = p.id";

    private final static String GET_DATA_BY_USER = "SELECT v.nombre AS titulo, v.descripcion AS descripcion, v.enlaceTrailer AS enlaceTrailer, " +
            "g.nombre AS genero, " +
            "p.nombre AS plataforma, " +
            "d.fechaLanzamiento AS fecha, " +
            "u.usuario AS usuario " +
            "FROM videojuegos v " +
            "JOIN genero g ON v.idGenero = g.id " +
            "JOIN usuarios u ON v.idUsuarios = u.id " +
            "JOIN disponible d ON v.id = d.idVideojuego " +
            "JOIN plataformas p ON d.idPlataforma = p.id " +
            "WHERE u.usuario = ?";

    private final static String SELECT_GAME_BY_NAME = "SELECT * FROM videojuegos WHERE nombre = ?";



    /**
     * Retrieves a video game by its name from the database.
     *
     * @param name The name of the video game to retrieve.
     * @return The {@code Videojuegos} entity with the given name, or {@code null} if not found.
     * @throws SQLException If an error occurs during the database operation.
     */
    public Videojuegos findGameByName(String name) throws SQLException {
        try (Connection conn = ConnectionXamp.getConnection();
             PreparedStatement statement = conn.prepareStatement(SELECT_GAME_BY_NAME)) {
            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToVideojuego(resultSet);
                }
            }
        }
        return null;
    }

    /**
     * Retrieves all video games related to the currently logged-in user.
     *
     * @return A list of {@code Videojuegos} entities associated with the logged-in user.
     * @throws SQLException If an error occurs during the database operation.
     */
    public List<Videojuegos> getDataByUser() throws SQLException {
        List<Videojuegos> videojuegos = new ArrayList<>();
        Connection conn = ConnectionXamp.getConnection();

        Usuarios usuarioLogueado = UsuarioSingleton.get_Instance().getPlayerLoged();
        if (usuarioLogueado == null) {
            throw new IllegalStateException("No hay un usuario logueado.");
        }
        String usuarioActual = usuarioLogueado.getUsuario();

        try (PreparedStatement statement = conn.prepareStatement(GET_DATA_BY_USER)) {
            statement.setString(1, usuarioActual);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String titulo = resultSet.getString("titulo");
                String genero = resultSet.getString("genero");
                String plataforma = resultSet.getString("plataforma");
                String fecha = resultSet.getString("fecha");
                String usuario = resultSet.getString("usuario");
                String descripcion = resultSet.getString("descripcion");
                String enlace = resultSet.getString("enlaceTrailer");

                Videojuegos videojuego = new Videojuegos();
                videojuego.setNombre(titulo);
                videojuego.setDescripcion(descripcion);
                videojuego.setEnlaceTrailer(enlace);

                Genero generoObj = new Genero();
                generoObj.setNombre(genero);

                Usuarios usuarioObj = new Usuarios();
                usuarioObj.setUsuario(usuario);

                Disponible disponible = new Disponible();
                disponible.setFechaLanzamiento(fecha);
                ArrayList<Plataformas> plataformas = new ArrayList<>();
                Plataformas plataformaObj = new Plataformas();
                plataformaObj.setNombre(plataforma);
                plataformas.add(plataformaObj);
                disponible.setPlataforma(plataformas);

                videojuego.setGenero(generoObj);
                videojuego.setUsuario(usuarioObj);
                videojuego.setDisponible(disponible);

                videojuegos.add(videojuego);
            }
        }
        return videojuegos;
    }


    /**
     * Retrieves all video game data (with availability and related user) from the database.
     *
     * @return A list of all {@code Videojuegos} entities with related data.
     * @throws SQLException If an error occurs during the database operation.
     */
    public List<Videojuegos> getAllData() throws SQLException {
        List<Videojuegos> videojuegos = new ArrayList<>();
        Connection conn = ConnectionXamp.getConnection();

        try (PreparedStatement statement = conn.prepareStatement(GET_DATA)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String titulo = resultSet.getString("titulo");
                String genero = resultSet.getString("genero");
                String plataforma = resultSet.getString("plataforma");
                String fecha = resultSet.getString("fecha");
                String usuario = resultSet.getString("usuario");
                String descripcion = resultSet.getString("descripcion");
                String enlace = resultSet.getString("enlaceTrailer");

                Videojuegos videojuego = new Videojuegos();
                videojuego.setNombre(titulo);
                videojuego.setDescripcion(descripcion);
                videojuego.setEnlaceTrailer(enlace);
                videojuego.setId(id);

                Genero generoObj = new Genero();
                generoObj.setNombre(genero);

                Usuarios usuarioObj = new Usuarios();
                usuarioObj.setUsuario(usuario);


                Disponible disponible = new Disponible();
                disponible.setFechaLanzamiento(fecha);
                ArrayList<Plataformas> plataformas = new ArrayList<>();
                Plataformas plataformaObj = new Plataformas();
                plataformaObj.setNombre(plataforma);
                plataformas.add(plataformaObj);
                disponible.setPlataforma(plataformas);

                videojuego.setGenero(generoObj);
                videojuego.setUsuario(usuarioObj);
                videojuego.setDisponible(disponible);

                videojuegos.add(videojuego);
            }
        }
        return videojuegos;
    }


    /**
     * Saves a new video game or updates an existing one.
     *
     * @param videojuego The {@code Videojuegos} entity to save or update.
     * @return The saved or updated {@code Videojuegos} entity.
     * @throws SQLException If an error occurs during the database operation.
     */
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

    /**
     * Saves a new video game and its availability information in the database.
     * This method inserts the video game into the "videojuegos" table and
     * the availability information into the "disponible" table.
     *
     * @param nombre           The name of the video game.
     * @param descripcion      A description of the video game.
     * @param enlace           A URL link to the trailer of the video game.
     * @param idGenero         The ID of the genre associated with the video game.
     * @param idUsuario        The ID of the user who added the video game.
     * @param idPlataforma     The ID of the platform where the video game is available.
     * @param fechaLanzamiento The release date of the video game.
     * @throws SQLException If an error occurs during the database operations.
     */
    public void saveFull(String nombre, String descripcion, String enlace, int idGenero, int idUsuario, int idPlataforma, String fechaLanzamiento) throws SQLException {
        try (

                PreparedStatement statementVideojuegos = ConnectionXamp.getConnection().prepareStatement(INSERT_FULL, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement statementDisponible = ConnectionXamp.getConnection().prepareStatement(INSERT_DISPONIBLE)
        ) {

            statementVideojuegos.setString(1, nombre);
            statementVideojuegos.setString(2, descripcion);
            statementVideojuegos.setString(3, enlace);
            statementVideojuegos.setInt(4, idGenero);
            statementVideojuegos.setInt(5, idUsuario);
            statementVideojuegos.executeUpdate();


            try (ResultSet generatedKeys = statementVideojuegos.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idVideojuego = generatedKeys.getInt(1);


                    statementDisponible.setInt(1, idVideojuego);
                    statementDisponible.setInt(2, idPlataforma);
                    statementDisponible.setString(3, fechaLanzamiento);
                    statementDisponible.executeUpdate();
                } else {
                    throw new SQLException("No se pudo obtener el ID generado para el videojuego.");
                }
            }
        }
    }

    /**
     * Deletes a video game from the database.
     *
     * @param videojuego The {@code Videojuegos} entity to delete.
     * @return The deleted {@code Videojuegos} entity.
     * @throws SQLException If an error occurs during the database operation.
     */
    public Videojuegos delete(Videojuegos videojuego) throws SQLException {
        if (videojuego == null || videojuego.getNombre() == "") {
            return null;
        } else {
            try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(DELETE)) {
                statement.setString(1, videojuego.getNombre());
                statement.executeUpdate();
            }
        }
        return videojuego;
    }

    /**
     * Retrieves a video game by its ID.
     *
     * @param id The ID of the video game to retrieve.
     * @return The {@code Videojuegos} entity with the given ID, or {@code null} if not found.
     * @throws SQLException If an error occurs during the database operation.
     */
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

    /**
     * Retrieves all video games from the database.
     *
     * @return A list of all {@code Videojuegos} entities.
     * @throws SQLException If an error occurs during the database operation.
     */
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
                false
        );

        Disponible disponible = new Disponible();
        disponible.setFechaLanzamiento(resultSet.getString("fechaLanzamiento"));

        ArrayList<Plataformas> plataformas = new ArrayList<>();
        Plataformas plataforma = new Plataformas();
        plataforma.setNombre(resultSet.getString("plataforma"));
        plataformas.add(plataforma);
        disponible.setPlataforma(plataformas);

        return new Videojuegos(
                resultSet.getInt("id"),
                resultSet.getString("nombre"),
                resultSet.getString("descripcion"),
                resultSet.getString("enlaceTrailer"),
                genero,
                usuario,
                disponible
        );
    }

}
