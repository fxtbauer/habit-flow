package br.gov.sp.fatec.pg.oo.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.sp.fatec.pg.oo.database.SQLConnection;
import br.gov.sp.fatec.pg.oo.model.Habit;

public class HabitRepository{
        public void createHabit(Habit habit) {
        // SQL para inserir um novo hábito    
        String sql = "INSERT INTO habits (user_id, name, completed) VALUES (?, ?, ?)";
        //  Garante que Connection e PreparedStatement sejam fechados automaticamente    
        try (Connection conn = SQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, habit.getUserId());
            stmt.setString(2, habit.getName());
            stmt.setInt(3, habit.isCompleted() ? 1 : 0);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Habit> getHabitsByUser(int userId){
        String sql = "SELECT * FROM habits WHERE user_id = ?";
        List<Habit> habits = new ArrayList<>();
        try (Connection conn = SQLConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setInt(1, userId);

                ResultSet rs = stmt.executeQuery();
               while (rs.next()) {
                Habit habit = new Habit(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("completed") == 1
                );

                habit.setUserId(rs.getInt("user_id")); 
                habits.add(habit);
            }
                return habits;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateHabit(Habit habit){
        String sql = "UPDATE habits SET name = ?, completed = ? WHERE id = ?"; 
        try (Connection conn = SQLConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setString(1, habit.getName());
                stmt.setInt(2, habit.isCompleted() ? 1 : 0);
                stmt.setInt(3, habit.getId());

                stmt.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    public void deleteHabit(int id){
        String sql = "DELETE FROM habits WHERE id = ?"; 
        try (Connection conn = SQLConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)){
                stmt.setInt(1, id);
                stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
