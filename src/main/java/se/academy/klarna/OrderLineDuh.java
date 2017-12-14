package se.academy.klarna;

public class OrderLineDuh {
    private String type;
    private String reference;
    private String name;
    private int quantity;
    private String quantityUnit;
    private double unitPrice;
    private double taxRate;
    private double totalAmount;
    private double totalTaxAmount;

    public OrderLineDuh(String type, String reference, String name, int quantity, String quantityUnit, double unitPrice, double taxRate, double totalAmount, double totalTaxAmount) {
        this.type = type;
        this.reference = reference;
        this.name = name;
        this.quantity = quantity;
        this.quantityUnit = quantityUnit;
        this.unitPrice = unitPrice;
        this.taxRate = taxRate;
        this.totalAmount = totalAmount;
        this.totalTaxAmount = totalTaxAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getQuantityUnit() {
        return quantityUnit;
    }

    public void setQuantityUnit(String quantityUnit) {
        this.quantityUnit = quantityUnit;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(double totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }
}
