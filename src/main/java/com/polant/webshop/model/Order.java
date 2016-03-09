package com.polant.webshop.model;

import java.util.Date;

/**
 * Created by Antony on 05.03.2016.
 */
public class Order {

    private int id;
    private String status;
    private int userId;
    private Date orderDate;

    public Order(int id, String status, int userId, Date orderDate) {
        this.id = id;
        this.status = status;
        this.userId = userId;
        this.orderDate = orderDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", userId=" + userId +
                ", orderDate=" + orderDate +
                '}';
    }
}
