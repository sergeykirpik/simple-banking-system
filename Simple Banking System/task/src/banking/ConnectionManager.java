package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {

    private final String fileName;

    public ConnectionManager(String fileName) {
        this.fileName = fileName;
    }

    public Connection getConnection() throws SQLException {
        String url = "jdbc:sqlite:" + fileName;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        return dataSource.getConnection();
    }
}
