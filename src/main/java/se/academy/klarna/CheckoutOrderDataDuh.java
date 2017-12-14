package se.academy.klarna;

import java.util.List;

public class CheckoutOrderDataDuh {
    private String purchaseCountry;
    private String purchaseCurrency;
    private String locale;
    private double orderAmount;
    private double orderTaxAmount;
    private List<OrderLineDuh> lines;
    private MerchantUrlsDuh urls;

    public CheckoutOrderDataDuh(double orderAmount, double orderTaxAmount, List<OrderLineDuh> lines) {
        purchaseCountry = "se";
        purchaseCurrency = "sek";
        locale = "sv-se";
        this.orderAmount = orderAmount;
        this.orderTaxAmount = orderTaxAmount;
        this.lines = lines;
        urls = new MerchantUrlsDuh();
    }

    public String getPurchaseCountry() {
        return purchaseCountry;
    }

    public void setPurchaseCountry(String purchaseCountry) {
        this.purchaseCountry = purchaseCountry;
    }

    public String getPurchaseCurrency() {
        return purchaseCurrency;
    }

    public void setPurchaseCurrency(String purchaseCurrency) {
        this.purchaseCurrency = purchaseCurrency;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public double getOrderTaxAmount() {
        return orderTaxAmount;
    }

    public void setOrderTaxAmount(double orderTaxAmount) {
        this.orderTaxAmount = orderTaxAmount;
    }

    public List<OrderLineDuh> getLines() {
        return lines;
    }

    public void setLines(List<OrderLineDuh> lines) {
        this.lines = lines;
    }

    public MerchantUrlsDuh getUrls() {
        return urls;
    }

    public void setUrls(MerchantUrlsDuh urls) {
        this.urls = urls;
    }
}
