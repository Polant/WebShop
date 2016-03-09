package com.polant.webshop.data;

import com.polant.webshop.model.Good;
import com.polant.webshop.model.Order;
import com.polant.webshop.model.OrderItem;
import com.polant.webshop.model.complex.ComplexOrderGoodsItem;

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

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(prop.getProperty("jdbc.url"),
                    prop.getProperty("jdbc.username"),
                    prop.getProperty("jdbc.password"));
    }

    /**
     * @return id of matched user
     */
    public int checkLogin(String login, String password){
        try(Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE login=? AND password=?")) {

            statement.setString(1, login);
            statement.setString(2, password);

            ResultSet result = statement.executeQuery();
            if (result.next()){
                return result.getInt("id");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Good findGoodById(int id){
        try(Connection connection = this.getConnection();
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

        try(Connection connection = this.getConnection(); Statement statement = connection.createStatement()) {

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

    public Order findOrderById(int id){
        try(Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM orders WHERE id=?")) {

            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();
            if (result.next()){
                return new Order(
                        result.getInt("id"),
                        result.getString("status"),
                        result.getInt("user_id"),
                        result.getDate("order_date")
                );
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return OrderItem, which attached to new order.
     */
    public ComplexOrderGoodsItem createNewOrder(Good newGood, int userId, int quantity) {
        try (Connection connection = this.getConnection(); Statement statement = connection.createStatement()) {

            //Создаю сам заказ.
            statement.execute(String.format("INSERT INTO orders(user_id, order_date) VALUES(%d, CURDATE())", userId));

            //Получаю Id только что добавленного в базу заказа.
            ResultSet ordersSet = statement.executeQuery(String.format("SELECT MAX(id) as last_order_id FROM orders WHERE user_id=%d", userId));
            if (ordersSet.next()) {
                int orderId = ordersSet.getInt("last_order_id");

                //Добавляю товар в заказ.
                statement.execute(String.format("INSERT INTO order_items(order_id, good_id, quantity) VALUES(%d, %d, %d)",
                        orderId, newGood.getId(), quantity));

                //Получаю объект только что добавленного в базу товара заказа.
                ResultSet orderItemsSet = statement.executeQuery(
                        String.format("SELECT * FROM order_items WHERE id=(SELECT MAX(id) FROM order_items WHERE order_id=%d)", orderId));

                if (orderItemsSet.next()) {
                    OrderItem orderItem = new OrderItem(orderItemsSet.getInt("id"), orderItemsSet.getInt("order_id"),
                            orderItemsSet.getInt("good_id"), orderItemsSet.getInt("quantity")
                    );

                    //Создаю комплексную сущность специально для работы с basket.jsp.
                    return new ComplexOrderGoodsItem(newGood, orderItem);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ComplexOrderGoodsItem> addGoodToOrder(int currentOrderId, Good newGood, Integer quantity) {
        try(Connection connection = this.getConnection(); Statement statement = connection.createStatement()) {

            //Добавляю товар в существующий заказ.
            statement.execute(String.format("INSERT INTO order_items(order_id, good_id, quantity) VALUES(%d, %d, %d)",
                    currentOrderId, newGood.getId(),quantity));

            //Получаю все товары по данному заказу.
            ResultSet orderItemsSet = statement.executeQuery(String.format("SELECT * FROM order_items WHERE order_id=%d", currentOrderId));
            List<ComplexOrderGoodsItem> resultList = new ArrayList<>();

            while (orderItemsSet.next()){
                Good good = findGoodById(orderItemsSet.getInt("good_id"));
                OrderItem orderItem = new OrderItem(orderItemsSet.getInt("id"),
                        orderItemsSet.getInt("order_id"),
                        orderItemsSet.getInt("good_id"),
                        orderItemsSet.getInt("quantity"));

                //добавляю комплексную сущность специально для работы с basket.jsp.
                resultList.add(new ComplexOrderGoodsItem(good, orderItem));
            }
            return resultList;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
