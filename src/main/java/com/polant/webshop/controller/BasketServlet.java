package com.polant.webshop.controller;

import com.polant.webshop.data.JdbcStorage;
import com.polant.webshop.model.Good;
import com.polant.webshop.model.complex.ComplexOrderGoodsItem;

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

    private static final String ORDER_ATTRIBUTE_JSP = "order";
    private static final String ORDER_GOODS_ATTRIBUTE_JSP = "orderGoods";
    private static final String JSP_PAGE = "/view/basket.jsp";

    private static final String IS_PAYED = "IS_PAYED";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && !session.isNew()) {

            //Если идет оплата платежа.
            if (Boolean.valueOf(req.getParameter("pay_for_order"))) {
                int orderId = Integer.valueOf(req.getParameter("order_id"));
                storage.payForOrder(orderId, true);

                req.setAttribute(ORDER_ATTRIBUTE_JSP, storage.findOrderById(orderId));
                req.setAttribute(ORDER_GOODS_ATTRIBUTE_JSP, storage.getAllOrderInfo(orderId));
                req.setAttribute(IS_PAYED, true);

                req.getRequestDispatcher(JSP_PAGE).forward(req, resp);
            }
            else if (Boolean.valueOf(req.getParameter("cancel_pay_for_order"))) {//идет отмена оплаты.
                int orderId = Integer.valueOf(req.getParameter("order_id"));
                storage.payForOrder(orderId, false);

                req.setAttribute(ORDER_ATTRIBUTE_JSP, storage.findOrderById(orderId));
                req.setAttribute(ORDER_GOODS_ATTRIBUTE_JSP, storage.getAllOrderInfo(orderId));
                req.setAttribute(IS_PAYED, false);

                req.getRequestDispatcher(JSP_PAGE).forward(req, resp);
            }
            else {//Если идет добавка товара в корзину.
                Good newGood = storage.findGoodById(Integer.valueOf(req.getParameter("good_id")));
                addToBasket(req, session, newGood);

                req.setAttribute(IS_PAYED, false);
                req.getRequestDispatcher(JSP_PAGE).forward(req, resp);
            }
        }
    }

    private void addToBasket(HttpServletRequest req, HttpSession session, Good newGood) {

        List<ComplexOrderGoodsItem> goodsList;
        int userId = (int) session.getAttribute("user_id");
        int quantity = Integer.valueOf(req.getParameter("quantity"));

        //Если создаю новый заказ.
        if (session.getAttribute("current_order_id") == null) {
            ComplexOrderGoodsItem item = storage.createNewOrder(newGood, userId, quantity);

            session.setAttribute("current_order_id", item.getOrderItem().getOrderId());

            goodsList = new ArrayList<>();
            goodsList.add(item);
        } else {
            //Если добавляю новый товар к существующему заказу.
            goodsList = storage.addGoodToOrder((int) session.getAttribute("current_order_id"), newGood, quantity);
        }
        req.setAttribute(ORDER_ATTRIBUTE_JSP, storage.findOrderById(goodsList.get(0).getOrderItem().getOrderId()));
        req.setAttribute(ORDER_GOODS_ATTRIBUTE_JSP, goodsList);

        //TODO: сделать обработку случая, когда на складе недостаточно товаров.
    }

}
