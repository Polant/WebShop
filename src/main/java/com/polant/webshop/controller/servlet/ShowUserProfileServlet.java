package com.polant.webshop.controller.servlet;

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
 * Данный сервлет обеспечивает работу с личный кабинетом пользователя.
 */
@WebServlet("/user/profile")
public class ShowUserProfileServlet extends HttpServlet {

    private final JdbcStorage storage = JdbcStorage.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && !session.isNew()) {
            int userId = (int)session.getAttribute("user_id");
            User user = storage.findUserById(userId);

            req.setAttribute("user", user);
            req.getRequestDispatcher("/view/user_profile_show.jsp").forward(req, resp);
        }
    }
}
