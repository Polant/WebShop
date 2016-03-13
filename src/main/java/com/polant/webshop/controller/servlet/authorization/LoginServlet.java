package com.polant.webshop.controller.servlet.authorization;

import com.polant.webshop.data.JdbcStorage;
import com.polant.webshop.model.User;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Сервлет, отвечающий за авторизацию пользователей.
 */
@WebServlet("/user/login")
public class LoginServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class);

    private final JdbcStorage storage = JdbcStorage.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //TODO:реализовать хранение хешей, а не оригинальных паролей и логинов.

        //Выход из системы.
        if (req.getParameter("log_out") != null) {
            logout(req, resp);
            return;
        }
        //Вход в систему.
        login(req, resp);
    }

    private void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        session.invalidate();
        //После выхода всегда направляю пользователя на главную страницу.
        resp.sendRedirect(String.format("%s%s", req.getContextPath(), "/"));
    }

    private void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        if (login != null && password != null) {
            User user;
            if ((user = storage.checkLogin(login, password)) != null && !user.getIsBanned()) {

                HttpSession newSession = req.getSession(true);
                newSession.setAttribute("login", login);
                newSession.setAttribute("user_id", user.getId());

                if (user.getIsAdmin()) {
                    newSession.setAttribute("IS_ADMIN", true);
//                    resp.sendRedirect(String.format("%s%s", req.getContextPath(), "/"));
//                    return;
                }
            }
            if (user != null && user.getIsBanned()) {
                LOGGER.debug(String.format("BANNED %s tried to log in", user));
            }
        } else {
            LOGGER.debug(String.format("AUTHORIZATION BY login:%s ; password: %s FAILED", login, password));
        }
        resp.sendRedirect(req.getParameter("lastURL"));
    }
}
