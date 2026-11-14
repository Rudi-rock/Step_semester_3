import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class BankAccountWithdrawalSystem {
    private static abstract class Account {
        private final String id;
        protected long balance;

        Account(String id, long balance) {
            this.id = id;
            this.balance = balance;
        }

        String getId() {
            return id;
        }

        abstract boolean canWithdraw(long amount);

        String rejectionMessage() {
            return "";
        }
    }

    private static final class SavingsAccount extends Account {
        SavingsAccount(String id, long balance) {
            super(id, balance);
        }

        @Override
        boolean canWithdraw(long amount) {
            return balance - amount >= 1000;
        }

        @Override
        String rejectionMessage() {
            return "minimum balance 1000";
        }
    }

    private static final class CurrentAccount extends Account {
        CurrentAccount(String id, long balance) {
            super(id, balance);
        }

        @Override
        boolean canWithdraw(long amount) {
            return balance - amount >= -5000;
        }

        @Override
        String rejectionMessage() {
            return "overdraft limit 5000";
        }
    }

    public static void main(String[] args) throws IOException {
        Map<String, Account> accounts = new HashMap<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length == 0 || parts[0].isEmpty()) {
                continue;
            }
            if (parts[0].equals("Savings") || parts[0].equals("Current")) {
                Account account = parts[0].equals("Savings")
                        ? new SavingsAccount(parts[1], Long.parseLong(parts[2]))
                        : new CurrentAccount(parts[1], Long.parseLong(parts[2]));
                accounts.put(account.getId(), account);
            } else if (parts[0].equals("WITHDRAW")) {
                Account account = accounts.get(parts[1]);
                if (account == null) {
                    System.out.println("Account not found");
                } else {
                    long amount = Long.parseLong(parts[2]);
                    if (account.canWithdraw(amount)) {
                        account.balance -= amount;
                        System.out.println(account.getId() + " balance " + account.balance);
                    } else {
                        System.out.println(account.getId() + " rejected: " + account.rejectionMessage());
                    }
                }
            }
        }
    }
}
