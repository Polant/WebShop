package com.polant.webshop.data;

import com.polant.webshop.model.Good;
import com.polant.webshop.model.Order;
import com.polant.webshop.model.OrderItem;
import com.polant.webshop.model.User;
import com.polant.webshop.model.complex.ComplexOrderGoodsItem;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.*;

/**
 * Данный класс является классом-оберткой над базой данных. Содержит все необходимые методы для работы с БД.
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
     * @return matched User object.
     */
    public User checkLogin(String login, String password){
        try(Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE login=? AND password=?")) {

            statement.setString(1, login);
            statement.setString(2, password);

            ResultSet set = statement.executeQuery();
            if (set.next()){
                LOGGER.debug(String.format("Confirm login and password %s%s SUCCESS", login, password));
                return new User(
                        set.getInt("id"),
                        set.getString("login"),
                        set.getString("password"),
                        set.getString("email"),
                        set.getBoolean("isBanned"),
                        set.getBoolean("isAdmin")
                );
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        LOGGER.debug(String.format("Authorization %s%s FAILED", login, password));
        return null;
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

    public List<Good> filterGoods(String name, String[] categories, int priceFrom, int priceTo, String inStock, String[] colors,
                                  String orderByName, String orderByPrice, String orderByDate) {
        List<Good> result = new ArrayList<>();

        try(Connection connection = this.getConnection(); Statement statement = connection.createStatement()){

            String stock;
            if (inStock.equals("in_stock")){
                stock = "count_left>0";
            }else if (inStock.equals("not_in_stock")){
                stock = "count_left=0";
            }else {//В наличии и не в наличии.
                stock = "count_left>=0";
            }

            StringBuilder sql = new StringBuilder("SELECT * FROM goods WHERE LOWER(name) LIKE '%").append(name.toLowerCase()).append("%' ");
            if (categories != null) {
                //Если выбраны категории для фильтра.
                sql.append("AND category IN (");
                for (int i = 0; i < categories.length; i++) {
                    sql.append(String.format("'%s'", categories[i]));
                    if (i < categories.length - 1) {
                        sql.append(", ");
                    }
                }
                sql.append(")");
            }
            sql.append(" AND (price BETWEEN ").append(priceFrom).append(" AND ").append(priceTo).append(") AND ").append(stock);
            if (colors != null) {
                //Если выбраны цвета для фильтра.
                sql.append(" AND color IN(");
                for (int i = 0; i < colors.length; i++) {
                    sql.append(String.format("'%s'", colors[i]));
                    if (i < colors.length - 1) {
                        sql.append(", ");
                    }
                }
                sql.append(")");
            }
            sql.append(" ORDER BY name ").append(orderByName).append(", price ").append(orderByPrice).append(", manufacturing_date ").append(orderByDate);

            ResultSet set = statement.executeQuery(sql.toString());
            while (set.next()){
                Good good = new Good(
                        set.getInt("id"),
                        set.getString("name"),
                        set.getString("description"),
                        set.getDouble("price"),
                        set.getString("category"),
                        set.getString("color"),
                        set.getInt("provider_id"),
                        set.getString("manufacturer_name"),
                        set.getDate("manufacturing_date"),
                        set.getDate("delivery_date"),
                        set.getInt("count_left")
                );
                result.add(good);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public User registerUser(String login, String password, String email) {
        try (Connection connection = this.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO users(login, password, email) VALUES(?,?,?)")) {

            statement.setString(1, login);
            statement.setString(2, password);
            statement.setString(3, email);

            statement.execute();

            String sql = String.format("SELECT * FROM users WHERE login='%s'", login);
            ResultSet set = statement.executeQuery(sql);
            if (set.next()){
                User user = new User(
                        set.getInt("id"),
                        set.getString("login"),
                        set.getString("password"),
                        set.getString("email"),
                        set.getBoolean("isBanned"),
                        set.getBoolean("isAdmin")
                );
                LOGGER.debug(String.format("REGISTER USER %s", user));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User findUserById(int id){
        try (Connection connection = this.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id=?")) {

            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            if (set.next()){
                return new User(
                        set.getInt("id"),
                        set.getString("login"),
                        set.getString("password"),
                        set.getString("email"),
                        set.getBoolean("isBanned"),
                        set.getBoolean("isAdmin")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return true if update run successfully.
     */
    public boolean updateUserProfile(int userId, String passwordNew, String email) {
        try (Connection connection = this.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE users SET password=?, email=? WHERE id=?")) {

            statement.setString(1, passwordNew);
            statement.setString(2, email);
            statement.setInt(3, userId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addGood(String name, String description, double price, String category, String color, int providerId,
                        String manufacturerName, String manufacturingDate, String deliveryDate, int countLeft) {

        try (Connection connection = this.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO goods(name, description, price, category, color, provider_id, manufacturer_name, manufacturing_date, delivery_date, count_left) " +
                             "VALUES(?,?,?,?,?,?,?,?,?,?)")) {

            statement.setString(1, name);
            statement.setString(2, description);
            statement.setDouble(3, price);
            statement.setString(4, category);
            statement.setString(5, color);
            statement.setInt(6, providerId);
            statement.setString(7, manufacturerName);
            statement.setString(8, manufacturingDate);
            statement.setString(9, deliveryDate);
            statement.setInt(10, countLeft);

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean editGood(int id, String name, String description, double price, String category, String color, int providerId,
                            String manufacturerName, String manufacturingDate, String deliveryDate, int countLeft) {
        try (Connection connection = this.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE goods SET name=?, description=?, price=?, category=?, color=?, provider_id=?, " +
                             "manufacturer_name=?, manufacturing_date=?, delivery_date=?, count_left=? WHERE id=?")){

            statement.setString(1, name);
            statement.setString(2, description);
            statement.setDouble(3, price);
            statement.setString(4, category);
            statement.setString(5, color);
            statement.setInt(6, providerId);
            statement.setString(7, manufacturerName);
            statement.setString(8, manufacturingDate);
            statement.setString(9, deliveryDate);
            statement.setInt(10, countLeft);
            statement.setInt(11, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //----------------Удаление товара-----------------------//

    /**
     * Если с данным товаром не ассоциировано ни одного заказа, то можно удалить его из базы.
     * В противном случае просто присваиваю count_left = 0, чтобы каскадно не удалялись orderItems.
     */
    public boolean deleteGood(int id) {
        try (Connection connection = this.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(good_id) AS count_order_items FROM order_items WHERE good_id=?")) {

            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                if (set.getInt("count_order_items") > 0) {
                    return deleteGoodFromStock(id);
                } else {
                    return deleteGoodFromDatabase(id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean deleteGoodFromStock(int id){
        try(Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE goods SET count_left=0 WHERE id=?")) {

            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean deleteGoodFromDatabase(int id) {
        try(Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM goods WHERE id=?")) {

            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //------------------------------------//

    /**
     * @param isBanned - true if admin banned user with userId
     * @return if update run successfully
     */
    public boolean setUserBanned(int userId, boolean isBanned) {
        try(Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE users SET isBanned=? WHERE id=?")) {

            statement.setBoolean(1, isBanned);
            statement.setInt(2, userId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
