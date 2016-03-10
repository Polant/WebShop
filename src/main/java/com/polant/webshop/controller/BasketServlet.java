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

    private static final String ORDER_ATTRIBUTE_JSP = "order";              //Заказ.
    private static final String ORDER_GOODS_ATTRIBUTE_JSP = "orderGoods";   //Товары заказа.
    private static final String IS_PAYED = "IS_PAYED";                      //Идет запрос на оплату заказа.

    private static final String JSP_PAGE = "/view/basket.jsp";
    private static final String EMPTY_BASKET_JSP = "/view/basket_empty.jsp";


    /**
     * Данный метод обрабатывает только запрос на просмотр содержимого корзины.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && !session.isNew()) {

            int lastOrderId = this.storage.checkNotPayedOrder((int)session.getAttribute("user_id"));
            //Если в корзине есть товары.
            if (lastOrderId > 0){
                req.setAttribute(ORDER_ATTRIBUTE_JSP, storage.findOrderById(lastOrderId));
                req.setAttribute(ORDER_GOODS_ATTRIBUTE_JSP, storage.getAllOrderInfo(lastOrderId));
                req.setAttribute(IS_PAYED, false);

                req.getRequestDispatcher(JSP_PAGE).forward(req, resp);
            }
            else {//корзина пуста.
                req.getRequestDispatcher(EMPTY_BASKET_JSP).forward(req, resp);
            }
        }
        else{
            //TODO: если пользователь не авторизовался, то перенаправить его на какой-то error.jsp.
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && !session.isNew()) {

            List<ComplexOrderGoodsItem> goodsItems;//Все товары, которые прикреплены к заказу.
            int orderId;

            //Если идет оплата платежа.
            if (Boolean.valueOf(req.getParameter("pay_for_order"))) {
                orderId = Integer.valueOf(req.getParameter("order_id"));
                storage.payForOrder(orderId, true);
                req.setAttribute(IS_PAYED, true);
                goodsItems = storage.getAllOrderInfo(orderId);
            }
            else if (Boolean.valueOf(req.getParameter("cancel_pay_for_order"))) {//идет отмена оплаты.
                orderId = Integer.valueOf(req.getParameter("order_id"));
                storage.payForOrder(orderId, false);
                req.setAttribute(IS_PAYED, false);
                goodsItems = storage.getAllOrderInfo(orderId);
            }
            else {//Если идет добавка товара в корзину.
                Good newGood = storage.findGoodById(Integer.valueOf(req.getParameter("good_id")));
                goodsItems = addToBasket(req, session, newGood);

                orderId = goodsItems.get(0).getOrderItem().getOrderId();
                req.setAttribute(IS_PAYED, false);
            }
            req.setAttribute(ORDER_ATTRIBUTE_JSP, storage.findOrderById(orderId));
            req.setAttribute(ORDER_GOODS_ATTRIBUTE_JSP, goodsItems);

            req.getRequestDispatcher(JSP_PAGE).forward(req, resp);
        }
    }


    /**
     * @return все товары (включая только что добавленный) текущего заказа.
     */
    private List<ComplexOrderGoodsItem> addToBasket(HttpServletRequest req, HttpSession session, Good newGood) {

        List<ComplexOrderGoodsItem> goodsList;
        int userId = (int) session.getAttribute("user_id");
        int quantity = Integer.valueOf(req.getParameter("quantity"));

        int lastOrderId = this.storage.checkNotPayedOrder(userId);

        //Если создаю новый заказ (в том случае, когда нет неоплаченных заказов).
        if (lastOrderId < 0) {
            ComplexOrderGoodsItem item = storage.createNewOrder(newGood, quantity, userId);
            goodsList = new ArrayList<>(1);
            goodsList.add(item);
        } else {
            //Если добавляю новый товар к существующему заказу.
            storage.addGoodToOrder(newGood, lastOrderId, quantity, userId);
            goodsList = storage.getAllOrderInfo(lastOrderId);
        }
        return goodsList;
        //TODO: сделать обработку случая, когда на складе недостаточно товаров.
    }

}
