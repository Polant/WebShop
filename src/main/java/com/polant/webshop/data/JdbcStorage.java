package com.polant.webshop.data;

import com.polant.webshop.model.User;

import java.sql.*;

/**
 * Created by Antony on 06.03.2016.
 */
public class JdbcStorage {

    public static void main(String[] args) {
        JdbcProperties prop = JdbcProperties.getInstance();

        try(Connection connection = DriverManager.getConnection(prop.getProperty("jdbc.url"), prop.getProperty("jdbc.username"),
                prop.getProperty("jdbc.password")); Statement statement = connection.createStatement()) {

            ResultSet result = statement.executeQuery("SELECT * FROM users;");
            while (result.next()){
                User c = new User(
                        result.getInt("id"),
                        result.getString("login"),
                        result.getString("password"),
                        result.getString("email"),
                        result.getBoolean("isBanned"),
                        result.getBoolean("isAdmin"));
                System.out.println(c);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
