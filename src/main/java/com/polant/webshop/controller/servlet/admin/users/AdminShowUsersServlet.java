package com.polant.webshop.controller.servlet.admin.users;

import com.polant.webshop.data.JdbcStorage;
import com.polant.webshop.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Сервлет взаимодействует в .jsp, в котором выводится список всех зарегистрированных польщователей сайта.
 */
@WebServlet("/admin/users")
public class AdminShowUsersServlet extends HttpServlet {

    private final JdbcStorage storage = JdbcStorage.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> users = storage.getUsers();
        req.setAttribute("users", users);
        req.getRequestDispatcher("/view/admin_users.jsp").forward(req, resp);
    }
}
