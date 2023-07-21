package de.zappler2k.api.mysql;

import de.zappler2k.api.config.ConfigManager;
import de.zappler2k.api.mysql.impl.MySQLConfig;

import java.sql.*;

public class MySQLConnector {

    private MySQLConfig mySQLConfig;
    private Connection connection;

    public MySQLConnector(ConfigManager configManager) {
        configManager.addIConfig(new MySQLConfig());
        this.mySQLConfig = (MySQLConfig) configManager.getIConfig(MySQLConfig.class);
    }


    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + mySQLConfig.getHost() + ":" + mySQLConfig.getPort() + "/" + mySQLConfig.getDatabase() + "?autoReconnect=true", mySQLConfig.getUsername(), mySQLConfig.getPassword());
            System.out.println("Connected to " + mySQLConfig.getHost() + ":" + mySQLConfig.getPort() + "/" + mySQLConfig.getDatabase());
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error while connecting to " + mySQLConfig.getHost() + ":" + mySQLConfig.getPort() + "/" + mySQLConfig.getDatabase() + "Error: Wrong host, port, database or user/password");
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Disconnected from " + mySQLConfig.getHost() + ":" + mySQLConfig.getPort() + "/" + mySQLConfig.getDatabase());
            }
        } catch (SQLException e) {
            System.out.println("Error while disconnecting from " + mySQLConfig.getHost() + ":" + mySQLConfig.getPort() + "/" + mySQLConfig.getDatabase() + "Error: " + e.getMessage());
        }
    }

    public void update(String qry, Object... objects) {
        try {
            PreparedStatement ps = connection.prepareStatement(qry);
            for (int i = 1; i <= objects.length; i++) {
                ps.setObject(i, objects[i - 1]);
            }
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            connect();
            System.err.println(e);
        }
    }

    public ResultSet query(String qry, Object... objects) {
        ResultSet rs = null;

        try {
            PreparedStatement ps = connection.prepareStatement(qry);
            for (int i = 1; i <= objects.length; i++) {
                ps.setObject(i, objects[i - 1]);
            }
            rs = ps.executeQuery();
        } catch (SQLException e) {
            connect();
            System.err.println(e);
        }
        return rs;
    }

    public boolean isConnected() {
        if (this.connection == null) {
            return false;
        }
        try {
            return connection.isValid(1);
        } catch (SQLException e) {
            return false;
        }
    }
}
