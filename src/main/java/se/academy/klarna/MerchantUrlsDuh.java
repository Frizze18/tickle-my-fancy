package se.academy.klarna;

public class MerchantUrlsDuh {
    private String terms;
    private String checkout;
    private String confirmation;
    private String push;

    public MerchantUrlsDuh() {
        terms = "";
        checkout = "";
        confirmation = "";
        push = "";
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public String getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }

    public String getPush() {
        return push;
    }

    public void setPush(String push) {
        this.push = push;
    }
}
