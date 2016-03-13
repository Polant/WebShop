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
 * Сервлет, отвечающий за управление пользователями (блокировка / разблокировка).
 */
@WebServlet(urlPatterns = {"/admin/users/lock", "/admin/users/unlock"})
public class AdminManageUsersServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AdminManageUsersServlet.class);

    private final JdbcStorage storage = JdbcStorage.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = Integer.valueOf(req.getParameter("id"));

        if (isCallToUnlockUser(req)) {
            if (storage.setUserBanned(userId, false)){
                LOGGER.debug(String.format("UNLOCK User(id:%d) SUCCESS", userId));
            }else {
                LOGGER.debug(String.format("UNLOCK User(id:%d) FAILED", userId));
            }
        } else {
            if (storage.setUserBanned(userId, true)){
                LOGGER.debug(String.format("LOCK User(id:%d) SUCCESS", userId));
            }else {
                LOGGER.debug(String.format("LOCK User(id:%d) FAILED", userId));
            }
        }
        resp.sendRedirect(String.format("%s%s", req.getContextPath(), "/admin/users"));
    }

    /**
     * @return true, если запрос послан для разблокировки пользователя. false - в противном случае.
     */
    private boolean isCallToUnlockUser(HttpServletRequest req){
        return req.getRequestURI().lastIndexOf("unlock") > 0;
    }
}
