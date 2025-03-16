class Payment {
    int id;
    int bookingId;
    double amount;
    String paymentMethod;

    Payment(int id, int bookingId, double amount, String paymentMethod) {
        this.id = id;
        this.bookingId = bookingId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }
}