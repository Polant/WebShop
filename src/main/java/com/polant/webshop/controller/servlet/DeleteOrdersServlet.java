package com.polant.webshop.controller.servlet;

import com.polant.webshop.data.JdbcStorage;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Сервлет, отвечающий за удаление заказа и соответствующих ему товаров.
 */
@WebServlet("/user/basket/delete_order_item")
public class DeleteOrdersServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(DeleteOrdersServlet.class);

    private final JdbcStorage storage = JdbcStorage.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean deleteOrderWithAllItems = Boolean.valueOf(req.getParameter("delete_all_items"));
        int deleteOrderId = Integer.valueOf(req.getParameter("order_id"));

        //Если пользователь удаляет последний товар в заказе, то удаляю весь заказ. Или пользователь сам решил удалить весь заказ.
        if (!deleteOrderWithAllItems && storage.getCountItemsInOrder(deleteOrderId) > 1){
            int deleteOrderItemId = Integer.valueOf(req.getParameter("item_id"));
            LOGGER.debug(String.format("DELETE order item №%d : %s", deleteOrderItemId, storage.deleteOrderItem(deleteOrderItemId)));
        }else {
            LOGGER.debug(String.format("DELETE order №%d : %s", deleteOrderId, storage.deleteOrder(deleteOrderId)));
        }

        //true - если запрос послан из списка заказов пользователя, false - из корзины пользователя.
        boolean callFromUserOrders = Boolean.valueOf(req.getParameter("redirect_to_user_orders"));

        resp.sendRedirect(String.format("%s%s", req.getContextPath(), callFromUserOrders ? "/user/orders" : "/user/basket"));
    }
}
