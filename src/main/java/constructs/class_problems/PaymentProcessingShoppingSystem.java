package constructs.class_problems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PaymentProcessingShoppingSystem {

    // ==========================================
    // Customer, Product & OrderItem
    // ==========================================

    public static class Customer {
        private final String customerId;
        private final String name;
        private final String email;

        public Customer(String customerId, String name, String email) {
            if (customerId == null || customerId.trim().isEmpty()) {
                throw new IllegalArgumentException("Customer ID cannot be empty.");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Customer name cannot be empty.");
            }
            if (email == null || !email.contains("@")) {
                throw new IllegalArgumentException("Invalid email format.");
            }
            this.customerId = customerId;
            this.name = name;
            this.email = email;
        }

        public String getCustomerId() {
            return customerId;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        @Override
        public String toString() {
            return name + " (" + email + ")";
        }
    }

    public static class Product {
        private final String productId;
        private final String name;
        private final double price;

        public Product(String productId, String name, double price) {
            if (productId == null || productId.trim().isEmpty()) {
                throw new IllegalArgumentException("Product ID cannot be empty.");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Product name cannot be empty.");
            }
            if (price < 0) {
                throw new IllegalArgumentException("Price cannot be negative.");
            }
            this.productId = productId;
            this.name = name;
            this.price = price;
        }

        public String getProductId() {
            return productId;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        @Override
        public String toString() {
            return name + " (INR " + price + ")";
        }
    }

    public static class OrderItem {
        private final Product product;
        private final int quantity;

        public OrderItem(Product product, int quantity) {
            if (product == null) {
                throw new IllegalArgumentException("Product cannot be null.");
            }
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero.");
            }
            this.product = product;
            this.quantity = quantity;
        }

        public Product getProduct() {
            return product;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getSubtotal() {
            return product.getPrice() * quantity;
        }

        @Override
        public String toString() {
            return product.getName() + " x " + quantity + " = INR " + getSubtotal();
        }
    }

    // ==========================================
    // Payment Abstraction & Polymorphic Providers
    // ==========================================

    public static class PaymentResult {
        private final boolean success;
        private final String transactionId;
        private final String message;

        public PaymentResult(boolean success, String transactionId, String message) {
            this.success = success;
            this.transactionId = transactionId;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return (success ? "[SUCCESS]" : "[FAILED]") + " Txn: " + transactionId + " - " + message;
        }
    }

    public interface PaymentMethod {
        String getMethodName();
        PaymentResult processPayment(double amount);
    }

    public static class CreditCardPayment implements PaymentMethod {
        private final String cardNumber;
        private final String cardHolderName;
        private double creditLimit;

        public CreditCardPayment(String cardNumber, String cardHolderName, double creditLimit) {
            if (cardNumber == null || cardNumber.replaceAll("\\s+", "").length() < 12) {
                throw new IllegalArgumentException("Invalid credit card number.");
            }
            this.cardNumber = cardNumber.replaceAll("\\s+", "");
            this.cardHolderName = cardHolderName;
            this.creditLimit = creditLimit;
        }

        @Override
        public String getMethodName() {
            return "Credit Card (Ending in " + cardNumber.substring(cardNumber.length() - 4) + ")";
        }

        @Override
        public PaymentResult processPayment(double amount) {
            if (amount > creditLimit) {
                return new PaymentResult(false, "TXN-FAIL-" + UUID.randomUUID().toString().substring(0, 8),
                        "Credit limit exceeded. Required: INR " + amount + ", Available: INR " + creditLimit);
            }
            creditLimit -= amount;
            return new PaymentResult(true, "TXN-CC-" + UUID.randomUUID().toString().substring(0, 8),
                    "Charged INR " + amount + " to Credit Card of " + cardHolderName + ". Remaining Limit: INR " + creditLimit);
        }
    }

    public static class PayPalPayment implements PaymentMethod {
        private final String payPalEmail;
        private double balance;

        public PayPalPayment(String payPalEmail, double initialBalance) {
            if (payPalEmail == null || !payPalEmail.contains("@")) {
                throw new IllegalArgumentException("Invalid PayPal email.");
            }
            this.payPalEmail = payPalEmail;
            this.balance = initialBalance;
        }

        @Override
        public String getMethodName() {
            return "PayPal (" + payPalEmail + ")";
        }

        @Override
        public PaymentResult processPayment(double amount) {
            if (amount > balance) {
                return new PaymentResult(false, "TXN-FAIL-" + UUID.randomUUID().toString().substring(0, 8),
                        "Insufficient PayPal balance. Required: INR " + amount + ", Balance: INR " + balance);
            }
            balance -= amount;
            return new PaymentResult(true, "TXN-PP-" + UUID.randomUUID().toString().substring(0, 8),
                    "Paid INR " + amount + " via PayPal account " + payPalEmail + ". Remaining: INR " + balance);
        }
    }

    public static class BankTransferPayment implements PaymentMethod {
        private final String accountNumber;
        private final String bankName;
        private double availableBalance;

        public BankTransferPayment(String accountNumber, String bankName, double availableBalance) {
            this.accountNumber = accountNumber;
            this.bankName = bankName;
            this.availableBalance = availableBalance;
        }

        @Override
        public String getMethodName() {
            return "Bank Transfer (" + bankName + ")";
        }

        @Override
        public PaymentResult processPayment(double amount) {
            if (amount > availableBalance) {
                return new PaymentResult(false, "TXN-FAIL-" + UUID.randomUUID().toString().substring(0, 8),
                        "Insufficient bank balance in " + bankName + ". Required: INR " + amount + ", Available: INR " + availableBalance);
            }
            availableBalance -= amount;
            return new PaymentResult(true, "TXN-NEFT-" + UUID.randomUUID().toString().substring(0, 8),
                    "Transferred INR " + amount + " from " + bankName + " A/C " + accountNumber);
        }
    }

    // ==========================================
    // Order & State Management
    // ==========================================

    public enum OrderStatus {
        PENDING,
        PAID,
        CANCELLED
    }

    public static class Order {
        private final String orderId;
        private final Customer customer;
        private final List<OrderItem> items = new ArrayList<>();
        private OrderStatus status;
        private PaymentResult lastPaymentResult;

        public Order(String orderId, Customer customer) {
            if (orderId == null || orderId.trim().isEmpty()) {
                throw new IllegalArgumentException("Order ID cannot be empty.");
            }
            if (customer == null) {
                throw new IllegalArgumentException("Customer cannot be null.");
            }
            this.orderId = orderId;
            this.customer = customer;
            this.status = OrderStatus.PENDING;
        }

        public String getOrderId() {
            return orderId;
        }

        public Customer getCustomer() {
            return customer;
        }

        public OrderStatus getStatus() {
            return status;
        }

        public PaymentResult getLastPaymentResult() {
            return lastPaymentResult;
        }

        public List<OrderItem> getItems() {
            return Collections.unmodifiableList(items);
        }

        public void addItem(Product product, int quantity) {
            if (status == OrderStatus.PAID) {
                throw new IllegalStateException("Cannot add items to an already PAID order.");
            }
            items.add(new OrderItem(product, quantity));
        }

        public double calculateTotal() {
            double total = 0.0;
            for (OrderItem item : items) {
                total += item.getSubtotal();
            }
            return total;
        }

        public boolean processPayment(PaymentMethod paymentMethod) {
            if (items.isEmpty()) {
                System.out.println("[REJECTED] Order " + orderId + " must contain at least one item before payment.");
                return false;
            }

            if (status == OrderStatus.PAID) {
                System.out.println("[WARNING] Order " + orderId + " is already paid. Duplicate payment rejected.");
                return false;
            }

            double totalAmount = calculateTotal();
            System.out.println("\n[PROCESSING] Initiating payment of INR " + totalAmount +
                    " for Order #" + orderId + " via " + paymentMethod.getMethodName());

            PaymentResult result = paymentMethod.processPayment(totalAmount);
            this.lastPaymentResult = result;

            if (result.isSuccess()) {
                this.status = OrderStatus.PAID;
                System.out.println("[SUCCESS] Payment approved! Order #" + orderId + " is now marked as PAID.");
                System.out.println("Details: " + result);
                return true;
            } else {
                this.status = OrderStatus.PENDING;
                System.out.println("[FAILED] Payment declined! Order #" + orderId + " remains PENDING.");
                System.out.println("Details: " + result);
                return false;
            }
        }

        @Override
        public String toString() {
            return "Order #" + orderId + " | Customer: " + customer.getName() +
                    " | Items: " + items.size() + " | Total: INR " + calculateTotal() +
                    " | Status: " + status;
        }
    }

    // ==========================================
    // Demonstration
    // ==========================================

    public static void main(String[] args) {
        System.out.println("=== Payment Processing Shopping System Demonstration ===");

        // 1. Setup Customer and Products
        Customer customer = new Customer("C-101", "Sanjay Singhania", "sanjay@example.com");
        Product laptop = new Product("P-1", "MacBook Pro 14\"", 160000.0);
        Product mouse = new Product("P-2", "Wireless Ergonomic Mouse", 3500.0);
        Product keyboard = new Product("P-3", "Mechanical Keyboard", 6500.0);

        System.out.println("Customer: " + customer);

        // 2. Create Order
        Order order = new Order("ORD-9001", customer);
        System.out.println("\nCreated: " + order);

        // 3. Attempt payment on empty order (Must be rejected)
        System.out.println("\n--- Step 1: Attempting payment on empty order ---");
        PaymentMethod testCard = new CreditCardPayment("4111222233334444", "Sanjay Singhania", 200000.0);
        order.processPayment(testCard);

        // 4. Add items to order
        System.out.println("\n--- Step 2: Adding items to order ---");
        order.addItem(mouse, 2);    // 2 x 3500 = 7000
        order.addItem(keyboard, 1); // 1 x 6500 = 6500
        System.out.println("Items in order:");
        for (OrderItem item : order.getItems()) {
            System.out.println(" - " + item);
        }
        System.out.println("Total Amount: INR " + order.calculateTotal()); // 13500

        // 5. Attempt payment with insufficient PayPal balance (Should fail and remain PENDING)
        System.out.println("\n--- Step 3: Payment with insufficient PayPal balance ---");
        PaymentMethod lowBalancePayPal = new PayPalPayment("sanjay@paypal.com", 5000.0);
        boolean payPalSuccess = order.processPayment(lowBalancePayPal);
        System.out.println("Payment Success: " + payPalSuccess + " | Order Status: " + order.getStatus());

        // 6. Retry payment using Credit Card with sufficient limit (Should succeed -> PAID)
        System.out.println("\n--- Step 4: Retry payment using Credit Card ---");
        PaymentMethod validCreditCard = new CreditCardPayment("4532789012345678", "Sanjay Singhania", 50000.0);
        boolean ccSuccess = order.processPayment(validCreditCard);
        System.out.println("Payment Success: " + ccSuccess + " | Order Status: " + order.getStatus());

        // 7. Attempt to add item or pay again on PAID order (Blocked)
        System.out.println("\n--- Step 5: Attempting to add item to already PAID order ---");
        try {
            order.addItem(laptop, 1);
        } catch (IllegalStateException e) {
            System.out.println("[BLOCKED] " + e.getMessage());
        }

        System.out.println("\n--- Step 6: Attempting to pay again on PAID order ---");
        order.processPayment(validCreditCard);

        // 8. Second order with Bank Transfer Payment
        System.out.println("\n--- Step 7: Second Order with Bank Transfer ---");
        Order order2 = new Order("ORD-9002", customer);
        order2.addItem(laptop, 1); // 160000
        PaymentMethod bankTransfer = new BankTransferPayment("987654321012", "HDFC Bank", 250000.0);
        order2.processPayment(bankTransfer);
        System.out.println("Final state of Order 2: " + order2);
    }
}
