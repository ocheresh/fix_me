package com.Data;

import lombok.NonNull;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MarketData {
    public static Statement statmt;
    public static ResultSet resSet;
    public static Connection co;

    public static void connect()
    {
        try {
            Class.forName("org.sqlite.JDBC");
            co = DriverManager.getConnection("jdbc:sqlite:Markets.db");
            statmt = co.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS markets " +
                    "(name varchar(255)," +
                    "apple varchar(255)," +
                    "orange varchar(255)," +
                    "lemon varchar(255)," +
                    "pineapple varchar(255))";
            statmt.executeUpdate(sql);
            resSet = statmt.executeQuery("SELECT * FROM markets");
            statmt.close();
        }
        catch (ClassNotFoundException e) {
            System.out.println("Error connect MarketData: " + e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Error connect MarketData: " + e.getMessage());
        }
    }

    public static void addMarket(@NonNull String id) {
        try {
            if (co == null) {
                Class.forName("org.sqlite.JDBC");
                co = DriverManager.getConnection("jdbc:sqlite:Markets.db");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        try (PreparedStatement statement = co.prepareStatement(
                "INSERT INTO markets(`name`, `apple`, `orange`, `lemon`, `pineapple`) " +
                        "VALUES(?, ?, ?, ?, ?)")) {
            statement.setObject(1, id);
            statement.setObject(2, String.valueOf((int)(Math.random() * 10000)));
            statement.setObject(3, String.valueOf((int)(Math.random() * 10000)));
            statement.setObject(4, String.valueOf((int)(Math.random() * 10000)));
            statement.setObject(5, String.valueOf((int)(Math.random() * 10000)));
            try {
                statement.executeUpdate();
            }
            finally {
                if (statement != null)
                    statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addMarket(@NonNull String id, String info[]) {
        try (PreparedStatement statement = co.prepareStatement(
                "INSERT INTO markets(`name`, `apple`, `orange`, `lemon`, `pineapple`) " +
                        "VALUES(?, ?, ?, ?, ?)")) {
            statement.setObject(1, id);
            statement.setObject(2, info[0]);
            statement.setObject(3, info[1]);
            statement.setObject(4, info[2]);
            statement.setObject(5, info[3]);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getRandMarket() {
        int rand = (int)(Math.random() * 10 * MarketData.size()) % MarketData.size();
        int t = 0;
        String retMsg = "";
        try {
            Class.forName("org.sqlite.JDBC");
            co = DriverManager.getConnection("jdbc:sqlite:Markets.db");
            statmt = co.createStatement();
            resSet = statmt.executeQuery("SELECT * FROM markets");
            while(resSet.next()) {
                if (t == rand)
                    retMsg = resSet.getString("name");
                t++;
            }
        }
        catch (Exception e) {
            System.out.println("Error Data Market getRandMarket: " + e.getMessage());
        }
        return (retMsg);
    }

    public static int size() {
        int t = 0;
        try {
            Class.forName("org.sqlite.JDBC");
            co = DriverManager.getConnection("jdbc:sqlite:Markets.db");
            statmt = co.createStatement();
            resSet = statmt.executeQuery("SELECT * FROM markets");
            while(resSet.next())
                t++;
        }
        catch (Exception e) {
            System.out.println("Error Data Market size: " + e.getMessage());
        }
        return (t);
    }

    public static void deleteMarket(String id) {
        try (PreparedStatement statement = co.prepareStatement(
                "DELETE FROM markets WHERE name = ?")) {
            statement.setObject(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteDataBase() {
        try {
            statmt = co.createStatement();
            resSet = statmt.executeQuery("SELECT * FROM markets");
            while (resSet.next())
                MarketData.deleteMarket(resSet.getString("name"));
        } catch (Exception e) {
            System.out.println("Error delete database: " + e.getMessage());
        }
    }

    public static MarketClass getMarket(String name)
    {
        Map temp = new HashMap<String, MarketClass>();
        try {
            if (co == null) {
                Class.forName("org.sqlite.JDBC");
                co = DriverManager.getConnection("jdbc:sqlite:Markets.db");
            }
            statmt = co.createStatement();
            resSet = statmt.executeQuery("SELECT * FROM markets");

            while(resSet.next()) {
                String tempname = resSet.getString("name");
                MarketClass marketClass = new MarketClass();
                marketClass.setName(tempname);
                marketClass.setApple(resSet.getString("apple"));
                marketClass.setOrange(resSet.getString("orange"));
                marketClass.setLemon(resSet.getString("lemon"));
                marketClass.setPineapple(resSet.getString("pineapple"));;
                temp.put(tempname, marketClass);
            }
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return ((MarketClass)(temp.get(name)));
    }

    public static String getRandomProduct()
    {
        int t = (int)(Math.random()*10) % 4;
        if (t == 0)
            return "apple";
        else if (t == 1)
            return "orange";
        else if (t == 2)
            return "lemon";
        return "pineapple";
    }

    public static void closeDBmarket()
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
