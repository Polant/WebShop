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
 * Фильтр для заказов.
 */
@WebFilter(urlPatterns = {"/user/orders", "/user/orders/*"})
public class OrdersFilter extends BaseFilter {

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
                    if (user.getIsAdmin()) {
                        //Админ не имеет доступа к редактированию заказов и их просмотру,
                        //а может только менять статус заказа с 'зарегистрирован' на 'оплачен' или 'отменен'.
                        LOGGER.debug(String.format("%s admin try to get access to user orders", user));
                        response.sendRedirect(String.format("%s%s", request.getContextPath(), "/"));
                        return;
                    }
                }catch (NullPointerException e){
                    LOGGER.debug("User which wasn't authorize try get access to user orders");
                    response.sendRedirect(String.format("%s%s", request.getContextPath(), "/view/user_orders_error.jsp"));
                    return;
                }
                filterChain.doFilter(request, response);
            }
            else {
                LOGGER.debug("User which wasn't authorize try get access to user orders; session not open");
                response.sendRedirect(String.format("%s%s", request.getContextPath(), "/view/user_orders_error.jsp"));
            }
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
    }
}
