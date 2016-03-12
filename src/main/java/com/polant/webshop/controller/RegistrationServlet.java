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
            //пароли должны совпадать.
            passwordsEqualsError(req, resp);
            return;
        }
        User user = storage.registerUser(login, password[0], email);
        if (user != null){
            //Удачная регистрация.
            openSession(req, resp, user);
        }else {
            //Если регистрация прошла неудачно (такой логин уже существует).
            loginAlreadyExists(req, resp);
        }
    }

    private void passwordsEqualsError(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("PASSWORDS_NOT_EQUALS", true);
        req.getRequestDispatcher("/view/registration.jsp").forward(req, resp);
    }

    private void openSession(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        HttpSession newSession = req.getSession(true);
        newSession.setAttribute("login", user.getLogin());
        newSession.setAttribute("user_id", user.getId());

        resp.sendRedirect(String.format("%s%s", req.getContextPath(), "/"));
    }

    private void loginAlreadyExists(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("LOGIN_ALREADY_EXISTS", true);
        req.getRequestDispatcher("/view/registration.jsp").forward(req, resp);
    }
}
