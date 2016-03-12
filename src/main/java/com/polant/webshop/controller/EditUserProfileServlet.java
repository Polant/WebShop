package com.polant.webshop.controller;

import com.polant.webshop.data.JdbcStorage;
import com.polant.webshop.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Сервлет, отвечающий за редактирование профиля пользователя.
 */
@WebServlet("/user/profile/change")
public class EditUserProfileServlet extends HttpServlet {

    private final JdbcStorage storage = JdbcStorage.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && !session.isNew()) {
            int userId = (int)session.getAttribute("user_id");
            User user = storage.findUserById(userId);

            req.setAttribute("email", user.getEmail());
            req.setAttribute("password", user.getPassword());
            req.getRequestDispatcher("/view/user_profile_edit.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        changeUserProfile(req, resp);
    }

    private void changeUserProfile(HttpServletRequest req, HttpServletResponse resp) {

    }
}
