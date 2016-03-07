package com.polant.webshop.data;

import com.polant.webshop.model.Good;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antony on 06.03.2016.
 */
public class JdbcStorage {

    private static final JdbcStorage INSTANCE = new JdbcStorage();

    private JdbcStorage(){
    }

    public static JdbcStorage getInstance(){
        return INSTANCE;
    }

    public List<Good> getGoods(){
        JdbcProperties prop = JdbcProperties.getInstance();

        List<Good> goods = new ArrayList<>();

        try {
            Class.forName(prop.getProperty("jdbc.driver_class"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try(Connection connection = DriverManager.getConnection(prop.getProperty("jdbc.url"), prop.getProperty("jdbc.username"),
                prop.getProperty("jdbc.password")); Statement statement = connection.createStatement()) {

            ResultSet result = statement.executeQuery("SELECT * FROM goods;");
            while (result.next()){
                Good good = new Good(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("description"),
                        result.getDouble("price"),
                        result.getString("category"),
                        result.getString("color"),
                        result.getInt("provider_id"),
                        result.getString("manufacturer_name"),
                        result.getDate("manufacturing_date"),
                        result.getDate("delivery_date"),
                        result.getInt("count_left")
                );
                goods.add(good);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return goods;
    }
}
