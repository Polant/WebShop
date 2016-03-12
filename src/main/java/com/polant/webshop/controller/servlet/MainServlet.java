package com.polant.webshop.controller.servlet;

import com.polant.webshop.data.JdbcStorage;
import com.polant.webshop.model.Good;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Данный сервлет обслуживает главную страницу интернет-магазина.
 */
@WebServlet("/")
public class MainServlet extends HttpServlet {

    private final JdbcStorage storage = JdbcStorage.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("goods", storage.getGoods());
        req.getRequestDispatcher("/view/main.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Good> goods = parseSearchRequest(req);
        req.setAttribute("goods", goods);
        req.getRequestDispatcher("/view/main.jsp").forward(req, resp);
    }

    private List<Good> parseSearchRequest(HttpServletRequest request) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String name = request.getParameter("name_goods");

        String[] categories = request.getParameterValues("category");
        int priceFrom = Integer.valueOf(request.getParameter("price_from"));
        int priceTo = Integer.valueOf(request.getParameter("price_to"));
        String inStock = request.getParameter("is_in_stock");
        String[] colors = request.getParameterValues("color");

        //orderBy... have value 'asc' or 'desc'.
        String orderByName = request.getParameter("sort_order_by_name");
        String orderByPrice = request.getParameter("sort_order_by_price");
        String orderByDate = request.getParameter("sort_order_by_date");

        if (name == null) name = "";

        return storage.filterGoods(name, categories, priceFrom, priceTo, inStock, colors, orderByName, orderByPrice, orderByDate);
    }
}
