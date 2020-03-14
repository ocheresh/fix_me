package com.Data;

import lombok.NonNull;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public class BrokerData {
    public static Statement statmt;
    public static ResultSet resSet;
    public static Connection co;

    public static void connect()
    {
        try {
            Class.forName("org.sqlite.JDBC");
            co = DriverManager.getConnection("jdbc:sqlite:Brokers.db");
            statmt = co.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS brokers " +
                    "(name varchar(255)," +
                    "apple varchar(255)," +
                    "orange varchar(255)," +
                    "lemon varchar(255)," +
                    "pineapple varchar(255)," +
                    "money varchar(255))";
            statmt.executeUpdate(sql);
            resSet = statmt.executeQuery("SELECT * FROM brokers");
            statmt.close();
        }
        catch (ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void addBroker(@NonNull String id) {
        try {
            if (co == null) {
                Class.forName("org.sqlite.JDBC");
                co = DriverManager.getConnection("jdbc:sqlite:Brokers.db");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        try (PreparedStatement statement = co.prepareStatement(
                "INSERT INTO brokers(`name`, `apple`, `orange`, `lemon`, `pineapple`, `money`) " +
                        "VALUES(?, ?, ?, ?, ?, ?)")) {
            statement.setObject(1, id);
            statement.setObject(2, String.valueOf((int)(Math.random() * 1000)));
            statement.setObject(3, String.valueOf((int)(Math.random() * 1000)));
            statement.setObject(4, String.valueOf((int)(Math.random() * 1000)));
            statement.setObject(5, String.valueOf((int)(Math.random() * 1000)));
            statement.setObject(6, "100000");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addBroker(@NonNull BrokerClass brokerClass) {
        try (PreparedStatement statement = co.prepareStatement(
                "INSERT INTO brokers(`name`, `apple`, `orange`, `lemon`, `pineapple`, `money`) " +
                        "VALUES(?, ?, ?, ?, ?, ?)")) {
            statement.setObject(1, brokerClass.getName());
            statement.setObject(2, brokerClass.getApple());
            statement.setObject(3, brokerClass.getOrange());
            statement.setObject(4, brokerClass.getLemon());
            statement.setObject(5, brokerClass.getPineapple());
            statement.setObject(6, brokerClass.getMoney());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getRandBroker() {
        int rand = (int)(Math.random() * 10 * BrokerData.size()) % BrokerData.size();
        int t = 0;
        String retMsg = "";
        try {
            Class.forName("org.sqlite.JDBC");
            co = DriverManager.getConnection("jdbc:sqlite:Brokers.db");
            statmt = co.createStatement();
            resSet = statmt.executeQuery("SELECT * FROM brokers");
            while(resSet.next()) {
                if (t == rand)
                    retMsg = resSet.getString("name");
                t++;
            }
        }
        catch (Exception e) {
            System.out.println("Error Data Broker getRandBroker: " + e.getMessage());
        }
        return (retMsg);
    }

    public static int size()
    {
        int t = 0;
        try {
            statmt = co.createStatement();
            resSet = statmt.executeQuery("SELECT * FROM brokers");
            while(resSet.next())
                t++;
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return (t);
    }

    public static void deleteBroker(String id) {
        try {
            if (co == null) {
                Class.forName("org.sqlite.JDBC");
                co = DriverManager.getConnection("jdbc:sqlite:Brokers.db");
            }
        }
        catch (Exception e) {}
        try (PreparedStatement statement = co.prepareStatement(
                "DELETE FROM brokers WHERE name = ?")) {
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
            resSet = statmt.executeQuery("SELECT * FROM brokers");
            while(resSet.next())
                BrokerData.deleteBroker(resSet.getString("name"));
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static BrokerClass getBroker(String name)
    {
        Map temp = new HashMap<String, BrokerClass>();
        try {
            if (co == null) {
                Class.forName("org.sqlite.JDBC");
                co = DriverManager.getConnection("jdbc:sqlite:Brokers.db");
            }
            statmt = co.createStatement();
            resSet = statmt.executeQuery("SELECT * FROM brokers");
            while(resSet.next()) {
                String tempname = resSet.getString("name");
                BrokerClass brokerClass = new BrokerClass();
                brokerClass.setName(tempname);
                brokerClass.setApple(resSet.getString("apple"));
                brokerClass.setOrange(resSet.getString("orange"));
                brokerClass.setLemon(resSet.getString("lemon"));
                brokerClass.setPineapple(resSet.getString("pineapple"));;
                brokerClass.setMoney(resSet.getString("money"));
                temp.put(tempname, brokerClass);
            }
        }
        catch (Exception e) {
            System.out.println("Error Broker class: " + e.getMessage());
        }
        return ((BrokerClass)(temp.get(name)));
    }

    public static void closeDBbroker()
    {
        try {
            co.close();
            statmt.close();
            resSet.close();
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
