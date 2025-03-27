package org.unibl.etf.database.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DATABASE_NAME = "bp_gamingshop";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "toor";
    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/" + DATABASE_NAME;

    private Connection connection;
    private static DatabaseConnection instance;

    private DatabaseConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch(ClassNotFoundException ex) {
            System.out.println("Error: unable to load driver class!");
            System.exit(1);
        }

        try {
            connection = DriverManager.getConnection(CONNECTION_URL, USERNAME, PASSWORD);

            if(connection != null){
                System.out.println("Database connection established.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
