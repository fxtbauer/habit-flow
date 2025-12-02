package br.gov.sp.fatec.pg.oo.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.sp.fatec.pg.oo.database.SQLConnection;
import br.gov.sp.fatec.pg.oo.model.User;

public class UserRepository {

    public void createUser(User user) {
        // SQL de INSERT
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        //  Garante o fechamento automático da conexão e do statement
        try (Connection conn = SQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
        //Mapeamento dos atributos do objeto User para os placeholders SQL.        
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findByUsername(String username) {
        // SQL para buscar um único usuário pelo nome de usuário (que deve ser UNIQUE)
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = SQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();

        try (Connection conn = SQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Itera sobre todos os registros retornados   
            while (rs.next()) {
                // Cria um novo objeto User para cada linha e o adiciona à lista
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                ));
            }

            return users;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = SQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
