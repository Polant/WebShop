package com.polant.webshop.controller.servlet.admin.users;

import com.polant.webshop.data.JdbcStorage;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Сервлет отвечает за открытие / закрытие доступа к разделам администратора для пользователя.
 */
@WebServlet(urlPatterns = {"/admin/users/open_admin_access", "/admin/users/close_admin_access"})
public class OpenAdminAccessServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AdminManageUsersServlet.class);

    private final JdbcStorage storage = JdbcStorage.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = Integer.valueOf(req.getParameter("id"));

        if (isCallToOpenAdminAccess(req)) {
            if (storage.setUserAdminAccessStatus(userId, true)){
                LOGGER.debug(String.format("OPEN ADMIN ACCESS for User(id:%d) SUCCESS", userId));
            }else {
                LOGGER.debug(String.format("OPEN ADMIN ACCESS  LOCK User(id:%d) FAILED", userId));
            }
        } else {
            if (storage.setUserAdminAccessStatus(userId, false)){
                LOGGER.debug(String.format("CLOSE ADMIN ACCESS  User(id:%d) SUCCESS", userId));
            }else {
                LOGGER.debug(String.format("CLOSE ADMIN ACCESS User(id:%d) FAILED", userId));
            }
        }
        resp.sendRedirect(String.format("%s%s", req.getContextPath(), "/admin/users"));
    }

    /**
     * @return true, если запрос послан для открытия доступа администратора. false - в противном случае.
     */
    private boolean isCallToOpenAdminAccess(HttpServletRequest req){
        return req.getRequestURI().lastIndexOf("open_admin_access") > 0;
    }
}
