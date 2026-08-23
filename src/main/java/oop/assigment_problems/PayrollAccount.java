package oop.assigment_problems;

public class PayrollAccount {
    private double basicSalary;
    private double bonus = 0;

    public PayrollAccount(double basicSalary) {
        if (basicSalary < 0) {
            System.out.println("Warning: Negative salary, setting to 0");
            this.basicSalary = 0;
        } else {
            this.basicSalary = basicSalary;
        }
    }

    public void creditBonus(double amount) {
        if (amount <= 0) {
            System.out.println("Bonus rejected: amount must be positive");
        } else {
            bonus += amount;
            System.out.println("Bonus credited: Rs " + amount);
        }
    }

    public void deductTax(double percent) {
        if (percent < 0 || percent > 100) {
            System.out.println("Tax deduction rejected: invalid percent");
        } else {
            basicSalary = basicSalary - (basicSalary * percent / 100);
            System.out.println("Tax deducted: " + percent + "%");
        }
    }

    public double getNetSalary() {
        return basicSalary + bonus;
    }

    public static void main(String[] args) {
        PayrollAccount p = new PayrollAccount(50000);
        p.creditBonus(5000);
        p.deductTax(10);
        System.out.println("Net salary: Rs " + p.getNetSalary());
    }
}
