package com.polant.webshop.controller.servlet.user.orders;

import com.polant.webshop.data.JdbcStorage;
import com.polant.webshop.model.Order;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Данный сервлет обрабатывает вывод заказов пользователя.
 */
@WebServlet("/user/orders")
public class ShowUserOrdersServlet extends HttpServlet {

    private static final String ORDERS_JSP = "/view/user_orders.jsp";
    private static final Logger LOGGER = Logger.getLogger(ShowUserOrdersServlet.class);

    private final JdbcStorage storage = JdbcStorage.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && !session.isNew()) {

            int userId = (int)session.getAttribute("user_id");
            LOGGER.debug(String.format("%d show his orders", userId));

            List<Order> orders = this.storage.getUserOrders(userId);
            req.setAttribute("orders", orders);
            req.getRequestDispatcher(ORDERS_JSP).forward(req, resp);
        }
        else{
            //TODO: если пользователь не авторизовался, то перенаправить его на какой-то error.jsp.
        }
    }
}
