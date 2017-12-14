package se.academy.domain;

public class Order {
    private int orderID;
    private int customerID;
    private double cost;
    private int quantity;
    private String klarna_order_id;

    public Order(int orderID, int customerID, double cost, int quantity, String klarna_order_id) {
        this.orderID = orderID;
        this.customerID = customerID;
        this.cost = cost;
        this.quantity = quantity;
        this.klarna_order_id = klarna_order_id;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getKlarna_order_id() {
        return klarna_order_id;
    }

    public void setKlarna_order_id(String klarna_order_id) {
        this.klarna_order_id = klarna_order_id;
    }
}
