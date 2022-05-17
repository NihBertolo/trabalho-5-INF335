package configuration.impl;

import com.mongodb.client.MongoClient;
import configuration.DatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionImpl implements DatabaseConnection {

    @Override
    public Connection conectarMysql() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://", "root", "18648454");
    }

    @Override
    public MongoClient conectarMongoDB() {
        return null;
    }
}
