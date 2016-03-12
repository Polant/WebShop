package com.polant.webshop.controller.servlet.admin;

import com.polant.webshop.data.JdbcStorage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Сервлет, обслуживающий добавление товара в магазин.
 */
@WebServlet("/admin/goods/add")
public class AdminAddGoodsServlet extends HttpServlet {

    private final JdbcStorage storage = JdbcStorage.getInstance();

    private final String[] colors = {"Красный", "Зеленый", "Синий","Черный", "Белый"};
    private final String[] categories = {"Смартфон", "Планшет", "ПК","Ноутбук", "Телевизор"};

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String requestColor = req.getParameter("color");
        String requestCategory = req.getParameter("category");

        if (requestColor == null) requestColor = colors[0];
        if (requestCategory == null) requestCategory = categories[0];

        List<String> colorsList = new ArrayList<>();
        List<String> categoriesList = new ArrayList<>();

        Collections.addAll(colorsList, colors);
        Collections.addAll(categoriesList, categories);

        req.setAttribute("colorsList", colorsList);
        req.setAttribute("selectedColor", requestColor);

        req.setAttribute("categoriesList", categoriesList);
        req.setAttribute("selectedCategory", requestCategory);
        req.getRequestDispatcher("/view/admin_add_new_good.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        addGood(req, resp);
    }

    private void addGood(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            req.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String name = req.getParameter("name");
        String description = req.getParameter("description");
        int price = Integer.valueOf(req.getParameter("price"));
        String category = req.getParameter("category");
        String color = req.getParameter("color");
        int providerId = 1;//Заглушка.
        String manufacturerName = req.getParameter("manufacturer_name");
        String manufacturingDate = req.getParameter("manufacturing_date");
        String deliveryDate = req.getParameter("delivery_date");
        int countLeft = Integer.valueOf(req.getParameter("count_left"));

        storage.addGood(name, description, price, category, color, providerId, manufacturerName,
                manufacturingDate, deliveryDate, countLeft);

        resp.sendRedirect(String.format("%s%s", req.getContextPath(), "/"));
    }
}
