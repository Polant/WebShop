package com.polant.webshop.controller.servlet;

import com.polant.webshop.data.JdbcStorage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Сервлет, отвечающий за отображение информации о выбранном товаре.
 */
@WebServlet("/good/show")
public class GoodServlet extends HttpServlet {

    private final JdbcStorage storage = JdbcStorage.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("good", storage.findGoodById(Integer.valueOf(req.getParameter("good_id"))));
        req.getRequestDispatcher("/view/selected_good.jsp").forward(req, resp);
    }
}
