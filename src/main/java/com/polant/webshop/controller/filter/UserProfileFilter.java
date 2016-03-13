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
 * Фильтр для страниц, связанных с профилем пользователя.
 */
@WebFilter(urlPatterns = {"/user/profile", "/user/profile/*"})
public class UserProfileFilter extends BaseFilter {

    private static final Logger LOGGER = Logger.getLogger(BasketAccessFilter.class);

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                if (session.getAttribute("user_id") == null) {
                    LOGGER.debug("User which wasn't authorize try get access to user profile");
                    response.sendRedirect(String.format("%s%s", request.getContextPath(), "/view/user_profile_error.jsp"));
                    return;
                }
                filterChain.doFilter(request, response);
            }
            else {
                LOGGER.debug("User which wasn't authorize try get access to user orders; session not open");
                response.sendRedirect(String.format("%s%s", request.getContextPath(), "/view/user_profile_error.jsp"));
            }
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
    }
}
