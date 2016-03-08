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

    private JdbcProperties prop = JdbcProperties.getInstance();

    public static final String TABLE_GOODS = "goods";
    public static final String TABLE_ORDER_ITEMS = "order_items";
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_PROVIDERS = "providers";
    public static final String TABLE_USERS = "users";

    private JdbcStorage(){
        try {
            Class.forName(prop.getProperty("jdbc.driver_class"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static JdbcStorage getInstance(){
        return INSTANCE;
    }

    public boolean checkLogin(String login, String password){
        //TODO: реализовать проверку логина и пароля.
        return true;
    }

    public Good findGoodById(int id){
        try(Connection connection = DriverManager.getConnection(prop.getProperty("jdbc.url"), prop.getProperty("jdbc.username"),
                prop.getProperty("jdbc.password"));
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM goods WHERE id=?")) {

            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();
            if (result.next()){
                return new Good(
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
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Good> getGoods(){
        List<Good> goods = new ArrayList<>();

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
