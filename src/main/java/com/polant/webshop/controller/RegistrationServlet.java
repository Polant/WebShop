package com.polant.webshop.controller;

import com.polant.webshop.data.JdbcStorage;
import com.polant.webshop.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Данный сервлет обрабатывает регистрацию пользователя на сайте.
 */
@WebServlet("/user/registration")
public class RegistrationServlet extends HttpServlet {

    private final JdbcStorage storage = JdbcStorage.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/view/registration.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String[] password = req.getParameterValues("password");
        String email = req.getParameter("email");

        if (!password[0].equals(password[1])){
            //ошибка регистрации.
            return;
        }
        User user = storage.registerUser(login, password[0], email);
    }


}
