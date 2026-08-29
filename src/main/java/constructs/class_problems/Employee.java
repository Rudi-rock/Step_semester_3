package constructs.class_problems;

public class Employee {
    String id;
    double salary;

    public Employee(String id, double salary) {
        this.id = id;
        this.salary = salary;
    }

    public void raiseSalary(double salary) {
        this.salary += salary;
    }

    public void display() {
        System.out.println(id + " | Final Salary: Rs " + salary);
    }

    public static void main(String[] args) {
        Employee[] emps = {
            new Employee("E-101", 40000),
            new Employee("E-102", 55000),
            new Employee("E-103", 62000),
            new Employee("E-104", 48000)
        };

        for (Employee e : emps) {
            e.raiseSalary(5000);
            e.display();
        }
    }
}
