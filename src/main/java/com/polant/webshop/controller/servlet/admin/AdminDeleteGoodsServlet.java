package com.polant.webshop.controller.servlet.admin;

import com.polant.webshop.data.JdbcStorage;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Сервлет, отвечающий за удаление выбранного товара.
 */
@WebServlet("/admin/goods/delete")
public class AdminDeleteGoodsServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AdminDeleteGoodsServlet.class);

    private final JdbcStorage storage = JdbcStorage.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int deleteGoodId = Integer.valueOf(req.getParameter("id"));
        if (storage.deleteGood(deleteGoodId)){
            LOGGER.debug(String.format("Delete good id:%d SUCCESS", deleteGoodId));
        }else {
            LOGGER.debug(String.format("Delete good id:%d FAILED", deleteGoodId));
        }
        resp.sendRedirect(String.format("%s%s", req.getContextPath(), "/"));
    }
}
