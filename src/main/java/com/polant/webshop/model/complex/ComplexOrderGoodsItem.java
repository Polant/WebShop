package com.polant.webshop.model.complex;

import com.polant.webshop.model.Good;
import com.polant.webshop.model.OrderItem;

/**
 * Составная сущность, которая сопоставляет товар с элементом выбранного заказа по Id заказа.
 */
public class ComplexOrderGoodsItem {

    private Good orderGood;
    private OrderItem orderItem;

    public ComplexOrderGoodsItem(Good orderGood, OrderItem orderItem) {
        this.orderGood = orderGood;
        this.orderItem = orderItem;
    }

    public Good getOrderGood() {
        return orderGood;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }
}
