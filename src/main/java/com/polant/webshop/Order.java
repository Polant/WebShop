package com.polant.webshop;

import java.util.Date;

/**
 * Created by Antony on 05.03.2016.
 */
public class Order {

    private int id;
    private String status;
    private int clientId;
    private Date orderDate;

    public Order(int id, String status, int clientId, Date orderDate) {
        this.id = id;
        this.status = status;
        this.clientId = clientId;
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

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
}
