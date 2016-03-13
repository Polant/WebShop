package com.polant.webshop.controller.servlet.admin.orders;

import com.polant.webshop.data.JdbcStorage;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Сервлет проводит обработку изменения статуса заказа.
 */
@WebServlet(urlPatterns = {"/admin/orders/set_payed", "/admin/orders/set_revoked"})
public class AdminManageOrdersServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AdminManageOrdersServlet.class);

    private final JdbcStorage storage = JdbcStorage.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int orderId = Integer.valueOf(req.getParameter("id"));
        if (isCallToSetOrderPayed(req)) {
            if (storage.setOrderStatus(orderId, JdbcStorage.ORDER_PAYED)){
                LOGGER.debug(String.format("ORDER id:%d - SET STATUS %s", orderId, JdbcStorage.ORDER_PAYED));
            }
        } else {
            if (storage.setOrderStatus(orderId, JdbcStorage.ORDER_REVOKED)){
                LOGGER.debug(String.format("ORDER id:%d - SET STATUS %s", orderId, JdbcStorage.ORDER_REVOKED));
            }
        }
        //resp.sentRedirect();
    }

    /**
     * @return true, если запрос послан для подтверждения оплаты. false - в противном случае.
     */
    private boolean isCallToSetOrderPayed(HttpServletRequest req){
        return req.getRequestURI().lastIndexOf("set_payed") > 0;
    }
}
