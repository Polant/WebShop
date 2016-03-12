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
import java.util.regex.Pattern;

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
        registerUser(req, resp);
    }

    private void registerUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String login = req.getParameter("login");
        String[] password = req.getParameterValues("password");
        String email = req.getParameter("email");

        boolean validLogin = true;
        boolean validPassword = true;
        boolean validEmail = true;

        if (login.equals("")){
            req.setAttribute("EMPTY_LOGIN", true);
            validLogin = false;
        }

        if (password[0].equals("")){
            req.setAttribute("EMPTY_PASSWORD", true);
            validPassword = false;
        }
        else if (!password[0].equals(password[1])){//пароли должны совпадать.
            req.setAttribute("PASSWORDS_NOT_EQUALS", true);
            validPassword = false;
        }

        //Почта не может быть пустой и должна быть корректной.
        if (!Pattern.matches("^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$", email)) {
            req.setAttribute("EMAIL_NOT_VALID", true);
            validEmail = false;
        }

        if (validEmail && validPassword && validLogin) {
            User user = storage.registerUser(login, password[0], email);
            if (user != null) {
                //Удачная регистрация.
                openSession(req, resp, user);
                return;
            } else {
                //Если такой логин уже существует.
                req.setAttribute("LOGIN_ALREADY_EXISTS", true);
                validLogin = false;
            }
        }
        //Если пользователь ввел невалидные данные, то он не покидает страницу регистрации.
        backToRegistrationPage(req, resp, login, password[0], email, validLogin, validPassword, validEmail);
    }

    private void backToRegistrationPage(HttpServletRequest req, HttpServletResponse resp, String login, String password,
                                        String email, boolean validLogin, boolean validPassword, boolean validEmail) throws ServletException, IOException {

        if (validLogin) req.setAttribute("login", login);
        if (validPassword) req.setAttribute("password", password);
        if (validEmail) req.setAttribute("email", email);

        req.getRequestDispatcher("/view/registration.jsp").forward(req, resp);
    }


    private void openSession(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        HttpSession newSession = req.getSession(true);
        newSession.setAttribute("login", user.getLogin());
        newSession.setAttribute("user_id", user.getId());

        resp.sendRedirect(String.format("%s%s", req.getContextPath(), "/"));
    }
}
