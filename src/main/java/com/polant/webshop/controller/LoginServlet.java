package com.polant.webshop.controller;

import com.polant.webshop.data.JdbcStorage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Antony on 08.03.2016.
 */
@WebServlet("/user/login")
public class LoginServlet extends HttpServlet {

    private final JdbcStorage storage = JdbcStorage.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //TODO:реализовать хранение хешей, а не оригинальных паролей и логинов.
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        if (login != null && password != null){
            if (storage.checkLogin(login, password)){
                HttpSession newSession = req.getSession(true);
                newSession.setAttribute("login", login);
            }
        }
        else{
            //TODO: сделать обработку некорректного пароля и логина.
        }
        resp.sendRedirect(req.getParameter("lastURL"));
    }
}
