package br.gov.sp.fatec.pg.oo.database;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
     public static void initialize() {
        try (Connection conn = SQLConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // TABELA DE USUÁRIOS
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    role TEXT NOT NULL
                );
            """);

            // TABELA DE HÁBITOS
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS habits (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    completed INTEGER NOT NULL DEFAULT 0,
                    FOREIGN KEY (user_id) REFERENCES users(id)
                );
            """);

            System.out.println("Tabelas CRIADAS com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

