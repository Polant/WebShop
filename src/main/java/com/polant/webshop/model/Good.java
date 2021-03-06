package com.polant.webshop.model;

import java.util.Date;

/**
 * Сущность - 'Товар'
 */
public class Good {

    private int id;
    private String name;
    private String description;

    private double price;

    private String category;
    private String color;

    private int providerId;
    private String manufacturerName;

    private Date manufacturedDate;
    private Date deliveryDate;

    private int countLeft;

    public Good(int id, String name, String description, double price, String category, String color,
                int providerId, String manufacturerName, Date manufacturedDate, Date deliveryDate, int countLeft) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.color = color;
        this.providerId = providerId;
        this.manufacturerName = manufacturerName;
        this.manufacturedDate = manufacturedDate;
        this.deliveryDate = deliveryDate;
        this.countLeft = countLeft;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public Date getManufacturedDate() {
        return manufacturedDate;
    }

    public void setManufacturedDate(Date manufacturedDate) {
        this.manufacturedDate = manufacturedDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public int getCountLeft() {
        return countLeft;
    }

    public void setCountLeft(int countLeft) {
        this.countLeft = countLeft;
    }

    @Override
    public String toString() {
        return "Good{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", color='" + color + '\'' +
                ", providerId=" + providerId +
                ", manufacturerName='" + manufacturerName + '\'' +
                ", manufacturedDate=" + manufacturedDate +
                ", deliveryDate=" + deliveryDate +
                ", countLeft=" + countLeft +
                '}';
    }
}
