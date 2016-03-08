package com.polant.webshop.controller;

import com.polant.webshop.data.JdbcStorage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Antony on 08.03.2016.
 */
@WebServlet("/good/show")
public class GoodServlet extends HttpServlet {

    private final JdbcStorage storage = JdbcStorage.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("good", storage.findGoodById(Integer.valueOf(req.getParameter("id"))));
        req.getRequestDispatcher("/view/selected_good.jsp").forward(req, resp);
    }
}
