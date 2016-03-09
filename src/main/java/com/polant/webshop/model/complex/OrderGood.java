package com.polant.webshop.model.complex;

import com.polant.webshop.model.Good;
import com.polant.webshop.model.OrderItem;

/**
 * Составная сущность, которая сопоставляет товар с элементом выбранного заказа по Id заказа.
 */
public class OrderGood {

    private Good orderGood;
    private OrderItem orderItem;

    public OrderGood(Good orderGood, OrderItem orderItem) {
        this.orderGood = orderGood;
        this.orderItem = orderItem;
    }

    public Good getOrderGood() {
        return orderGood;
    }

    public void setOrderGood(Good orderGood) {
        this.orderGood = orderGood;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }
}
