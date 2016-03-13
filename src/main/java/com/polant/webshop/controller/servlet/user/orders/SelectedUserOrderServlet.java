package com.polant.webshop.controller.servlet.user.orders;

import com.polant.webshop.data.JdbcStorage;
import com.polant.webshop.model.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Данный сервлет обрабатывает вывод выбранного заказа из всех заказов пользователя.
 */
@WebServlet("/user/orders/selected_order")
public class SelectedUserOrderServlet extends HttpServlet {

    private static final String ORDER_ATTRIBUTE_JSP = "order";              //Заказ.
    private static final String ORDER_GOODS_ATTRIBUTE_JSP = "orderGoods";   //Товары заказа.
    private static final String IS_PAYED = "IS_PAYED";                      //Оплачен ли данный заказ.
    private static final String JSP_PAGE = "/view/selected_order.jsp";

    private final JdbcStorage storage = JdbcStorage.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int orderId = Integer.valueOf(req.getParameter("order_id"));
        Order order = storage.findOrderById(orderId);

        req.setAttribute(ORDER_ATTRIBUTE_JSP, order);
        req.setAttribute(ORDER_GOODS_ATTRIBUTE_JSP, storage.getAllOrderInfo(orderId));
        req.setAttribute(IS_PAYED, !order.getStatus().equals(JdbcStorage.ORDER_CANCELLED));

        req.getRequestDispatcher(JSP_PAGE).forward(req, resp);
    }
}
