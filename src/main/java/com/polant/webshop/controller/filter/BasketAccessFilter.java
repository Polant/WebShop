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
 * Фильтр, который не позволяет получить доступ к корзине неавторизованому пользователю или админу.
 */
@WebFilter("/user/basket")
public class BasketAccessFilter extends BaseFilter{

    private static final Logger LOGGER = Logger.getLogger(BasketAccessFilter.class);

    private final JdbcStorage storage = JdbcStorage.getInstance();

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                try {
                    int userId = (int) session.getAttribute("user_id");
                    User user = storage.findUserById(userId);
                    if (user.getIsAdmin()) {//Админ не имеет доступа к корзине.
                        LOGGER.debug(String.format("%s admin try to get access to user basket", user));
                        response.sendRedirect(String.format("%s%s", request.getContextPath(), "/"));
                        return;
                    }
                }catch (NullPointerException e){
                    LOGGER.debug("User which wasn't authorize try get access to basket");
                    response.sendRedirect(String.format("%s%s", request.getContextPath(), "/view/basket_error.jsp"));
                    return;
                }
                filterChain.doFilter(request, response);
            }
            else {
                LOGGER.debug("User which wasn't authorize try get access to basket; session not open");
                response.sendRedirect(String.format("%s%s", request.getContextPath(), "/view/basket_error.jsp"));
            }
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
    }
}
