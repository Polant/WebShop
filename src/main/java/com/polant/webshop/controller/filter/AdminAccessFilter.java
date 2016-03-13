package com.polant.webshop.controller.filter;

import com.polant.webshop.controller.filter.base.BaseFilter;
import com.polant.webshop.data.JdbcStorage;
import com.polant.webshop.model.User;
import org.apache.log4j.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Фильтр, который не позволяет незарегистрированному и не являющемуся админом пользователю получить доступ
 * к разделам администратора сайта.
 */
@WebFilter("/admin/*")
public class AdminAccessFilter extends BaseFilter {

    private static final Logger LOGGER = Logger.getLogger(AdminAccessFilter.class);

    private final JdbcStorage storage = JdbcStorage.getInstance();

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                try {
                    int userId = (int) session.getAttribute("user_id");
                    User user = storage.findUserById(userId);
                    if (!user.getIsAdmin()) {
                        LOGGER.debug(String.format("%s which is not an admin try to access for admin pages", user));
                        response.sendRedirect(String.format("%s%s", request.getContextPath(), "/"));
                        return;
                    }
                }catch (NullPointerException e){
                    LOGGER.debug("User wasn't authorize");
                    response.sendRedirect(String.format("%s%s", request.getContextPath(), "/"));
                    return;
                }
                filterChain.doFilter(request, response);
            }
            else {
                LOGGER.debug("User wasn't authorize; session not open");
                response.sendRedirect(String.format("%s%s", request.getContextPath(), "/"));
            }
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
    }
}
