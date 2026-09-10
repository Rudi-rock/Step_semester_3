package constructs.assigment_problems;

public class Payment {
    double amount;

    public Payment() {
        this(0.0);
    }

    public Payment(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public static double processPayment(Payment payment, double amount) {
        if (payment instanceof CardPayment) {
            double finalAmount = amount * 1.02;
            System.out.println("Card payment processed: Rs " + finalAmount);
            return finalAmount;
        } else {
            System.out.println("Standard payment processed: Rs " + amount);
            return amount;
        }
    }

    public static double processPayment(Payment payment) {
        return processPayment(payment, payment.amount);
    }

    public static void main(String[] args) {
        Payment[] payments = {
            new Payment(150.0),
            new CardPayment(100.0),
            new Payment(130.0),
            new CardPayment(70.0),
            new Payment(100.0)
        };

        double totalCollected = 0.0;
        for (Payment p : payments) {
            totalCollected += processPayment(p);
        }

        System.out.println("Total collected: Rs " + totalCollected);
    }
}

class CardPayment extends Payment {

    public CardPayment() {
        super(0.0);
    }

    public CardPayment(double amount) {
        super(amount);
    }
}
