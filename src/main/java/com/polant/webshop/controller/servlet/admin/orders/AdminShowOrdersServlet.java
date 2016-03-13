package com.polant.webshop.controller.servlet.admin.orders;

import com.polant.webshop.data.JdbcStorage;
import com.polant.webshop.model.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 *  Сервлет взаимодействует в .jsp, в котором выводится список всех зарегистрированных заказов.
 */
@WebServlet("/admin/orders")
public class AdminShowOrdersServlet extends HttpServlet {

    private final JdbcStorage storage = JdbcStorage.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Order> orders = storage.getRegisteredOrdersOrders();
        req.setAttribute("orders", orders);
        req.setAttribute("JdbcStorage", storage);
        req.getRequestDispatcher("/view/admin_orders.jsp").forward(req, resp);
    }
}
