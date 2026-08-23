package oop.assigment_problems;

public class EmployeeStatic {
    String empName;
    double salary;
    static String companyName = "Bright Horizon Technologies";
    static int employeeCount = 0;

    public EmployeeStatic(String empName, double salary) {
        this.empName = empName;
        this.salary = salary;
        employeeCount++;
    }

    static void printCompanyInfo() {
        System.out.println(companyName);
        System.out.println("Employees on record: " + employeeCount);
    }

    public static void main(String[] args) {
        EmployeeStatic e1 = new EmployeeStatic("A", 50000);
        EmployeeStatic e2 = new EmployeeStatic("B", 60000);
        EmployeeStatic e3 = new EmployeeStatic("C", 70000);

        EmployeeStatic.printCompanyInfo();
    }
}
