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
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection conn = SQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = SQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapUser(rs);
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findByToken(String token) {
        String sql = "SELECT * FROM users WHERE token = ?";

        try (Connection conn = SQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, token);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapUser(rs);
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveToken(int userId, String token) {
        String sql = "UPDATE users SET token = ? WHERE id = ?";

        try (Connection conn = SQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, token);
            stmt.setInt(2, userId);

            stmt.executeUpdate();

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

            while (rs.next()) users.add(mapUser(rs));
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

public void promoteToAdmin(int id) {
    String sql = "UPDATE users SET role = 'ADMIN' WHERE id = ?";

    try (Connection conn = SQLConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, id);
        stmt.executeUpdate();

    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}


    private User mapUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("role")
        );
    }
}
