package string.class_problems;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BankAccountWithdrawalSystem {

    static class Account {
        String id;
        String type;
        long balance;

        Account(String type, String id, long balance) {
            this.type = type;
            this.id = id;
            this.balance = balance;
        }

        String withdraw(long amount) {
            if (amount <= 0)
                return id + " rejected: invalid amount";

            if (type.equals("Savings") && balance - amount < 1000)
                return id + " rejected: minimum balance 1000";

            if (type.equals("Current") && balance - amount < -5000)
                return id + " rejected: overdraft limit 5000";

            balance -= amount;
            return id + " balance " + balance;
        }
    }

    public static void main(String[] args) {
        Map<String, Account> accounts = new HashMap<>();
        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();

            if (line.isEmpty())
                continue;

            String[] parts = line.split("\\s+");

            if (parts[0].equalsIgnoreCase("Savings")
                    || parts[0].equalsIgnoreCase("Current")) {

                if (parts.length != 3)
                    continue;

                String type = parts[0].equalsIgnoreCase("Savings")
                        ? "Savings" : "Current";

                accounts.put(parts[1],
                        new Account(type, parts[1], Long.parseLong(parts[2])));

            } else if (parts[0].equalsIgnoreCase("WITHDRAW")
                    && parts.length == 3) {

                Account account = accounts.get(parts[1]);

                if (account == null)
                    System.out.println("Account not found");
                else
                    System.out.println(
                            account.withdraw(Long.parseLong(parts[2])));
            }
        }

        sc.close();
    }
}
