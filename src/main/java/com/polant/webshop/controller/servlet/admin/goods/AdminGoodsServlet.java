package com.polant.webshop.controller.servlet.admin.goods;

import com.polant.webshop.data.JdbcStorage;
import com.polant.webshop.model.Good;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервлет, обслуживающий добавление товара в магазин.
 */
@WebServlet(urlPatterns = {"/admin/goods/add", "/admin/goods/edit"})
public class AdminGoodsServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AdminGoodsServlet.class);

    private final JdbcStorage storage = JdbcStorage.getInstance();

    private final List<String> colors = new ArrayList<>();
    private final List<String> categories = new ArrayList<>();

    private static final String IS_EDIT = "IS_EDIT";

    {
        colors.add("Красный");
        colors.add("Зеленый");
        colors.add("Синий");
        colors.add("Черный");
        colors.add("Белый");

        categories.add("Смартфон");
        categories.add("Планшет");
        categories.add("ПК");
        categories.add("Ноутбук");
        categories.add("Телевизор");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //Проверяю назвачение запроса.
        if (isCalledForEditGoods(req)) {
            int goodId = Integer.valueOf(req.getParameter("good_id"));
            Good editGood = storage.findGoodById(goodId);

            req.setAttribute("editGood", editGood);
            req.setAttribute(IS_EDIT, true);
        }else {
            req.setAttribute(IS_EDIT, false);
        }

        req.setAttribute("colorsList", colors);
        req.setAttribute("categoriesList", categories);

        req.getRequestDispatcher("/view/admin_goods_form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String name = req.getParameter("name");
        String description = req.getParameter("description");
        double price = Double.valueOf(req.getParameter("price"));
        String category = req.getParameter("category");
        String color = req.getParameter("color");
        int providerId = 1;//Заглушка.
        String manufacturerName = req.getParameter("manufacturer_name");
        String manufacturingDate = req.getParameter("manufacturing_date");
        String deliveryDate = req.getParameter("delivery_date");
        int countLeft = Integer.valueOf(req.getParameter("count_left"));

        if (isCalledForEditGoods(req)) {
            int id = Integer.valueOf(req.getParameter("id"));
            if (storage.editGood(id, name, description, price, category, color, providerId,
                    manufacturerName, manufacturingDate, deliveryDate, countLeft)) {
                LOGGER.debug(String.format("Edit good id:%d SUCCESS", id));
            } else {
                LOGGER.debug(String.format("Edit good id:%d FAILED", id));
            }
        } else {
            storage.addGood(name, description, price, category, color, providerId, manufacturerName,
                    manufacturingDate, deliveryDate, countLeft);
        }
        resp.sendRedirect(String.format("%s%s", req.getContextPath(), "/"));
    }

    private boolean isCalledForEditGoods(HttpServletRequest req){
        return req.getRequestURI().lastIndexOf("edit") > 0;
    }
}
