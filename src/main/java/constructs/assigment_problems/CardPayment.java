package constructs.assigment_problems;

public class CardPayment extends Payment {

    public CardPayment() {
        super(0.0);
    }

    public CardPayment(double amount) {
        super(amount);
    }

    public static void main(String[] args) {
        Payment.main(args);
    }
}
