package com.polant.webshop.data;

import com.polant.webshop.model.Good;
import com.polant.webshop.model.Order;
import com.polant.webshop.model.OrderItem;
import com.polant.webshop.model.complex.ComplexOrderGoodsItem;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antony on 06.03.2016.
 */
public class JdbcStorage {

    private static final JdbcStorage INSTANCE = new JdbcStorage();

    private JdbcProperties prop = JdbcProperties.getInstance();

    private static final Logger LOGGER = Logger.getLogger(JdbcStorage.class);

    public static final String TABLE_GOODS = "goods";
    public static final String TABLE_ORDER_ITEMS = "order_items";
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_PROVIDERS = "providers";
    public static final String TABLE_USERS = "users";

    public static final String ORDER_REGISTERED = "зарегистрирован";
    public static final String ORDER_CANCELLED = "не подтвержден";

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
                LOGGER.debug(String.format("Authorization %s%s SUCCESS", login, password));
                return result.getInt("id");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        LOGGER.debug(String.format("Authorization %s%s FAILED", login, password));
        return -1;
    }


    /**
     * @return id of order, which has not payed yet; otherwise return -1;
     */
    public int checkNotPayedOrder(int userId){
        try(Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM orders WHERE user_id=? AND status=?")){

            statement.setInt(1, userId);
            statement.setString(2, ORDER_CANCELLED);

            ResultSet set = statement.executeQuery();
            if (set.next()){
                return set.getInt("id");
            }
        } catch (SQLException e) {
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
    public ComplexOrderGoodsItem createNewOrder(Good newGood, int quantity, int userId) {
        try (Connection connection = this.getConnection(); Statement statement = connection.createStatement()) {

            //Создаю сам заказ.
            statement.execute(String.format("INSERT INTO orders(user_id, order_date) VALUES(%d, CURDATE())", userId));

            //Получаю Id только что добавленного в базу заказа.
            ResultSet ordersSet = statement.executeQuery(String.format("SELECT MAX(id) as last_order_id FROM orders WHERE user_id=%d", userId));
            if (ordersSet.next()) {
                int orderId = ordersSet.getInt("last_order_id");

                LOGGER.debug(String.format("User %d create an order №%d with :%s", userId, orderId, newGood));

                //Добавляю товар в заказ.
                statement.execute(String.format("INSERT INTO order_items(order_id, good_id, quantity) VALUES(%d, %d, %d)",
                        orderId, newGood.getId(), quantity));

                //Уменьшаю количество товара на складе.
                decreaseGoodCountLeft(newGood, quantity);

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

    public void addGoodToOrder(Good newGood, int currentOrderId, Integer quantity, int userId) {
        try(Connection connection = this.getConnection(); Statement statement = connection.createStatement()) {

            //Добавляю товар в существующий заказ.
            statement.execute(String.format("INSERT INTO order_items(order_id, good_id, quantity) VALUES(%d, %d, %d)",
                    currentOrderId, newGood.getId(),quantity));

            //Уменьшаю количество товара на складе.
            decreaseGoodCountLeft(newGood, quantity);

            LOGGER.debug(String.format("User %d add to order №%d (quantity=%d): %s", userId, currentOrderId, quantity, newGood));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void decreaseGoodCountLeft(Good good, int countDecrease){
        try(Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE goods SET count_left=count_left-? WHERE id=?")) {

            statement.setInt(1, countDecrease);
            statement.setInt(2, good.getId());
            statement.execute();

            good.setCountLeft(good.getCountLeft() - countDecrease);

            LOGGER.debug(String.format("%s count decreased : (-%d)", good, countDecrease));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * @return Возвращаю все товары для выбранного заказа.
     */
    public List<ComplexOrderGoodsItem> getAllOrderInfo(int orderId) {
        try(Connection connection = this.getConnection(); Statement statement = connection.createStatement()) {

            ResultSet orderItemsSet = statement.executeQuery(String.format("SELECT * FROM order_items WHERE order_id=%d", orderId));
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


    /**
     * Оплата платежа пользователем. Просто меняю статус платежа == ORDER_REGISTERED (см. константу).
     * Поставить статус 'оплачено' может только администратор.
     *
     * @param isPay true - если идет оплата, false - если идет отмена оплаты.
     */
    public int payForOrder(int orderId, boolean isPay) {
        try(Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE orders SET status=? WHERE id=?")) {

            statement.setString(1, isPay ? ORDER_REGISTERED : ORDER_CANCELLED);
            statement.setInt(2, orderId);

            LOGGER.debug(String.format("Order №%d payment %s", orderId, isPay ? ORDER_REGISTERED : ORDER_CANCELLED));

            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * @return count of order items of selected order (by order id).
     */
    public int getCountItemsInOrder(int orderId) {
        try(Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(id) as count_items FROM order_items WHERE order_id=?")) {

            statement.setInt(1, orderId);

            ResultSet set = statement.executeQuery();
            if (set.next()){
                return set.getInt("count_items");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean deleteOrderItem(int deleteOrderItemId) {
        try(Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM order_items WHERE id=?")) {

            //Добавляю количество товара на складе.
            statement.execute(String.format(
                    "UPDATE goods SET count_left=count_left+(SELECT quantity FROM order_items WHERE id=%d) WHERE id=(SELECT good_id FROM order_items WHERE id=%d)",
                    deleteOrderItemId, deleteOrderItemId));

            statement.setInt(1, deleteOrderItemId);
            return statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteOrder(int deleteOrderId) {
        try(Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM orders WHERE id=?")) {

            //Но сначала добавлю количество товаров на складе, которые удалил пользователь.
            increaseCountLeftGoodsAfterDeletingOrder(deleteOrderId);

            statement.setInt(1, deleteOrderId);
            return statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void increaseCountLeftGoodsAfterDeletingOrder(int orderId){
        try(Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE goods SET count_left=count_left+? WHERE id=?")) {

//            statement.execute("DROP TABLE IF EXISTS temp_goods;");
//            statement.execute("CREATE TABLE temp_goods (id INT PRIMARY KEY, count_left INT NOT NULL);");
//            statement.execute("INSERT INTO temp_goods(id, count_left) SELECT id, count_left FROM goods;");
//
//            statement.execute(String.format("UPDATE goods SET count_left=count_left+(SELECT SUM(order_items.quantity) " +
//                    "FROM order_items INNER JOIN temp_goods ON order_items.good_id = temp_goods.id " +
//                    "WHERE goods.id=temp_goods.id AND order_items.order_id=%d)", orderId));
//
//            statement.execute("DROP TABLE temp_goods");

            List<ComplexOrderGoodsItem> items = getAllOrderInfo(orderId);
            for (ComplexOrderGoodsItem i : items){
                statement.setInt(1, i.getOrderItem().getQuantity());
                statement.setInt(2, i.getOrderGood().getId());
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Order> getUserOrders(int userId) {
        List<Order> result = new ArrayList<>();

        try(Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM orders WHERE user_id=?")) {

            statement.setInt(1, userId);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                Order order = new Order(set.getInt("id"),
                        set.getString("status"),
                        set.getInt("user_id"),
                        set.getDate("order_date"));

                result.add(order);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
