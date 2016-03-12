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
import java.util.regex.Pattern;

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
            req.setAttribute("password_old", user.getPassword());
            req.setAttribute("password_new", user.getPassword());
            req.getRequestDispatcher("/view/user_profile_edit.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        changeUserProfile(req, resp);
    }

    private void changeUserProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        int userId = (int) session.getAttribute("user_id");
        User user = storage.findUserById(userId);

        String passwordOld = req.getParameter("password_old");
        String[] passwordNews = req.getParameterValues("password_new");
        String email = req.getParameter("email");

        boolean validOldPassword = true;
        boolean validNewPassword = true;
        boolean validEmail = true;

        if (!(storage.checkLogin(user.getLogin(), passwordOld) > 0)){
            req.setAttribute("NOT_VALID_OLD_PASSWORD", true);
            validOldPassword = false;
        }
        if (passwordNews[0].equals("")){
            req.setAttribute("EMPTY_NEW_PASSWORD", true);
            validNewPassword = false;
        }
        else if (!passwordNews[0].equals(passwordNews[1])){//пароли должны совпадать.
            req.setAttribute("NEW_PASSWORDS_NOT_EQUALS", true);
            validNewPassword = false;
        }

        if (!Pattern.matches("^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$", email)) {
            req.setAttribute("EMAIL_NOT_VALID", true);
            validEmail = false;
        }

        if (validEmail && validOldPassword && validNewPassword) {
            boolean successEdit = storage.updateUserProfile(userId, passwordNews[0], email);
            if (successEdit) {
                resp.sendRedirect(String.format("%s%s", req.getContextPath(), "/user/profile"));
                return;
            }
        }
        //Если пользователь ввел невалидные данные, то он не покидает страницу.
        backToEditProfilePage(req, resp, passwordOld, passwordNews[0], email, validOldPassword, validNewPassword, validEmail);
    }

    private void backToEditProfilePage(HttpServletRequest req, HttpServletResponse resp,
                                       String passwordOld, String passwordNew, String email,
                                       boolean validOldPassword, boolean validNewPassword, boolean validEmail) throws ServletException, IOException {

        if (validOldPassword) req.setAttribute("password_old", passwordOld);
        if (validNewPassword) req.setAttribute("password_new", passwordNew);
        if (validEmail) req.setAttribute("email", email);

        req.getRequestDispatcher("/view/user_profile_edit.jsp").forward(req, resp);
    }
}
