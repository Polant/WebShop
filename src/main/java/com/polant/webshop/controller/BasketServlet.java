package com.polant.webshop.controller;

import com.polant.webshop.data.JdbcStorage;
import com.polant.webshop.model.Good;
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
        addToBasket(req);
        req.getRequestDispatcher("/view/basket.jsp").forward(req, resp);
    }

    private void addToBasket(HttpServletRequest req){
        Good newGood = storage.findGoodById(Integer.valueOf(req.getParameter("good_id")));

        HttpSession session = req.getSession(false);
        if (session != null && !session.isNew()) {

            List<OrderGood> goodsList;

            int userId = (int) session.getAttribute("user_id");
            int quantity = Integer.valueOf(req.getParameter("quantity"));
            
            //Если создаю новый заказ.
            if (session.getAttribute("current_order_id") == null) {
                OrderItem orderItem = storage.createNewOrder(newGood, userId, quantity);

                session.setAttribute("current_order_id", orderItem.getOrderId());

                goodsList = new ArrayList<>();
                goodsList.add(new OrderGood(newGood, orderItem));
            }
            else {
                //Если добавляю новый товар к существующему заказу.
                goodsList = storage.addGoodToOrder((int) session.getAttribute("current_order_id"), newGood, quantity);
            }
            //TODO: сделать обработку случая, когда на складе недостаточно товаров.
            req.setAttribute("orderGoods", goodsList);
        }
    }

}
