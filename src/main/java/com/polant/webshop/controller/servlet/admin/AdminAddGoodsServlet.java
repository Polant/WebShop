package com.polant.webshop.controller.servlet.admin;

import com.polant.webshop.data.JdbcStorage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Сервлет, обслуживающий добавление товара в магазин.
 */
@WebServlet("/admin/goods/add")
public class AdminAddGoodsServlet extends HttpServlet {

    private final JdbcStorage storage = JdbcStorage.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/view/admin_add_new_good.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        addGood(req, resp);
    }

    private void addGood(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        int price = Integer.valueOf(req.getParameter("price"));
        String category = req.getParameter("category");
        String color = req.getParameter("color");
        int providerId = 1;//Заглушка.
        String manufacturerName = req.getParameter("manufacturer_name");
        Date manufacturingDate = null;
        Date deliveryDate = null;
        try {
            manufacturingDate = DateFormat.getInstance().parse(req.getParameter("manufacturing_date"));
            deliveryDate = DateFormat.getInstance().parse(req.getParameter("delivery_date"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int countLeft = Integer.valueOf(req.getParameter("count_left"));

        storage.addGood(name, description, price, category, color, providerId, manufacturerName,
                manufacturingDate, deliveryDate, countLeft);

        resp.sendRedirect(String.format("%s%s", req.getContextPath(), "/"));
    }
}
