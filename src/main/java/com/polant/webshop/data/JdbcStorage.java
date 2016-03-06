package com.polant.webshop.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Antony on 06.03.2016.
 */
public class JdbcStorage {

    public static void main(String[] args) {
        Connection connection;
        try {
            JdbcProperties properties = JdbcProperties.getInstance();
//            DriverManager.registerDriver((Driver) Class.forName(properties.getProperty("jdbc.driver_class")).newInstance());

            connection = DriverManager.getConnection(properties.getProperty("jdbc.url"),
                    properties.getProperty("jdbc.username"),
                    properties.getProperty("jdbc.password"));


            if (!connection.isClosed()){
                System.out.println("connection open");
            }
            connection.close();
            if (connection.isClosed()){
                System.out.println("connection close");
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
