import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PayrollRegisterSystem {
    private abstract static class Staff {
        private final String name;

        Staff(String name) {
            this.name = name;
        }

        abstract long pay();

        abstract String type();

        String name() {
            return name;
        }
    }

    private static final class FullTime extends Staff {
        private final long salary;

        FullTime(String name, long salary) {
            super(name);
            this.salary = salary;
        }

        @Override
        long pay() {
            return salary;
        }

        @Override
        String type() {
            return "FullTime";
        }
    }

    private static final class PartTime extends Staff {
        private final long hours;
        private final long rate;

        PartTime(String name, long hours, long rate) {
            super(name);
            this.hours = hours;
            this.rate = rate;
        }

        @Override
        long pay() {
            return hours * rate;
        }

        @Override
        String type() {
            return "PartTime";
        }
    }

    private static final class Intern extends Staff {
        private final long stipend;

        Intern(String name, long stipend) {
            super(name);
            this.stipend = stipend;
        }

        @Override
        long pay() {
            return stipend;
        }

        @Override
        String type() {
            return "Intern";
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Staff[] payslips = new Staff[1000];
        int count = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length == 0 || parts[0].isEmpty()) {
                continue;
            }
            Staff staff = switch (parts[0]) {
                case "FullTime" -> new FullTime(parts[1], Long.parseLong(parts[2]));
                case "PartTime" -> new PartTime(parts[1], Long.parseLong(parts[2]), Long.parseLong(parts[3]));
                case "Intern" -> new Intern(parts[1], Long.parseLong(parts[2]));
                default -> throw new IllegalArgumentException("Unknown staff type: " + parts[0]);
            };
            payslips[count++] = staff;
        }

        long total = 0;
        Staff topEarner = payslips[0];
        for (int i = 0; i < count; i++) {
            Staff staff = payslips[i];
            System.out.println("Payslip[name=" + staff.name() + ", type=" + staff.type() + ", pay=" + staff.pay() + "]");
            total += staff.pay();
            if (staff.pay() > topEarner.pay()) {
                topEarner = staff;
            }
        }
        if (count > 0) {
            System.out.println("Total " + total);
            System.out.println("top earner " + topEarner.name());
        }
    }
}
