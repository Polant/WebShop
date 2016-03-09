package com.polant.webshop.controller;

import com.polant.webshop.data.JdbcStorage;
import com.polant.webshop.model.Good;
import com.polant.webshop.model.Order;
import com.polant.webshop.model.OrderItem;
import com.polant.webshop.model.complex.OrderGood;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Данный сервлет обслуживает страницу корзины пользователя.
 */
@WebServlet("/user/basket")
public class BasketServlet extends HttpServlet {

    private final JdbcStorage storage = JdbcStorage.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Good newGood = storage.findGoodById(Integer.valueOf(req.getParameter("good_id")));

        HttpSession session = req.getSession(false);
        if (session != null && !session.isNew()) {
            OrderItem orderItem = storage.createOrder(
                    newGood, (int)session.getAttribute("user_id"), Integer.valueOf(req.getParameter("quantity")));

            session.setAttribute("current_order_id", orderItem.getOrderId());

            List<OrderGood> goodsList = new ArrayList<>(1);
            goodsList.add(new OrderGood(newGood, orderItem));
            req.setAttribute("orderGoods", goodsList);
        }
        req.getRequestDispatcher("/view/basket.jsp").forward(req, resp);
    }

}
