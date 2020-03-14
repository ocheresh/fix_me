package com.Data;
import com.Message.SeparateMessage;
import lombok.NonNull;
import java.sql.*;

public class MessageData {
    public static Statement statmt;
    public static ResultSet resSet;
    public static Connection co;

    public static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            co = DriverManager.getConnection("jdbc:sqlite:Messages.db");
            statmt = co.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS messages " +
                    "(number varchar(255)," +
                    "broker varchar(255)," +
                    "instrument varchar(255)," +
                    "quantity varchar(255)," +
                    "date varchar(255)," +
                    "market varchar(255)," +
                    "price varchar(255)," +
                    "shareproduct varchar(255)," +
                    "checksum varchar(255))";
            statmt.executeUpdate(sql);
            resSet = statmt.executeQuery("SELECT * FROM messages");
            statmt.close();
        }
        catch (ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void addMessage(@NonNull String msg) {
        try {
            if (co == null) {
                Class.forName("org.sqlite.JDBC");
                co = DriverManager.getConnection("jdbc:sqlite:Messages.db");
            }
        }
        catch (Exception e) {}
        try (PreparedStatement statement = co.prepareStatement(
                "INSERT INTO messages(`number`, `broker`, `instrument`, `quantity`, `date`, `market`, `price`, `shareproduct`, `checksum`) " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            statement.setObject(1, SeparateMessage.getNumber(msg));
            statement.setObject(2, SeparateMessage.getBroker(msg));
            statement.setObject(3, SeparateMessage.getInstrument(msg));
            statement.setObject(4, SeparateMessage.getQuantuty(msg));
            statement.setObject(5, SeparateMessage.getDate(msg));
            statement.setObject(6, SeparateMessage.getMarket(msg));
            statement.setObject(7, SeparateMessage.getPrice(msg));
            statement.setObject(8, SeparateMessage.getShareproduct(msg));
            statement.setObject(9, SeparateMessage.getCheckSum(msg));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteMessage(String id) {
        try {
            if (co == null) {
                Class.forName("org.sqlite.JDBC");
                co = DriverManager.getConnection("jdbc:sqlite:Messages.db");
            }
        }
        catch (Exception e) {}
        try (PreparedStatement statement = co.prepareStatement(
                "DELETE FROM messages WHERE number = ?")) {
            statement.setObject(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteDataBase()
    {
        try {
            statmt = co.createStatement();
            resSet = statmt.executeQuery("SELECT * FROM messages");
            while(resSet.next())
                MessageData.deleteMessage(resSet.getString("number"));
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
