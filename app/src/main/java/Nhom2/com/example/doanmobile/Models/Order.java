package Nhom2.com.example.doanmobile.Models;

import java.util.List;

public class Order {
    private String orderID;
    private String status;
    private String address;
    private String phone;
    private List<CartItem> items;

    public Order() {
    }

    public Order(String orderID, String status, String address, String phone, List<CartItem> items) {
        this.orderID = orderID;
        this.status = status;
        this.address = address;
        this.phone = phone;
        this.items = items;
    }

    // Getters and setters
    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}
