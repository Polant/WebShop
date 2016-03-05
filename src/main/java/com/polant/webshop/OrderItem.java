package com.polant.webshop;

/**
 * Created by Antony on 05.03.2016.
 */
public class OrderItem {

    private int id;
    private int orderId;
    private int goodId;
    private int quantity;

    public OrderItem(int id, int orderId, int goodId, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.goodId = goodId;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getGoodId() {
        return goodId;
    }

    public void setGoodId(int goodId) {
        this.goodId = goodId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
