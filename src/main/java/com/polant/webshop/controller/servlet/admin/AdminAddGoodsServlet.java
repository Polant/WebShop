package com.polant.webshop.controller.servlet.admin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Сервлет, обслуживающий добавление товара в магазин.
 */
@WebServlet("/admin/goods/add")
public class AdminAddGoodsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/view/admin_add_new_good.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        addGood(req, resp);
    }

    private void addGood(HttpServletRequest req, HttpServletResponse resp) {
        //TODO: реализовать добавление.
    }
}
