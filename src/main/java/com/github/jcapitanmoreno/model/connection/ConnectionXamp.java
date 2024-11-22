package com.github.jcapitanmoreno.model.connection;

import com.github.jcapitanmoreno.utils.XMLManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionXamp {
    private final static String FILE="connection.xml";
    private static ConnectionXamp _instance;
    private static Connection conn;
    /**
     * Constructor for creating a MariaDB connection using connection properties from an XML file.
     */
    private ConnectionXamp(){
        ConnectionProperties properties = (ConnectionProperties) XMLManager.readXML(new ConnectionProperties(),FILE);

        try {
            conn = DriverManager.getConnection(properties.getURL(),properties.getUser(),properties.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
            conn=null;
        }
    }
    /**
     * Retrieves the connection to the MariaDB database.
     *
     * @return The connection to the MariaDB database.
     */
    public static Connection getConnection(){
        if(_instance==null){
            _instance = new ConnectionXamp();
        }
        return conn;
    }
    /**
     * Closes the connection to the MariaDB database.
     */
    public static void closeConnection(){
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
