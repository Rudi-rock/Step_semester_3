package constructs.class_problems;

public class StudentFee {
    String regNo;
    double total;

    public StudentFee(String regNo, double total) {
        this.regNo = regNo;
        this.total = total;
    }

    final double calculateLateFee(int daysLate) {
        return total * 0.01 * daysLate;
    }

    final void printSummary(int daysLate) {
        if (daysLate <= 0) {
            System.out.println(regNo + " - On time, no late fee");
        } else {
            System.out.println(regNo + " | Total Fee: Rs " + total + " | Late Fee: Rs " + calculateLateFee(daysLate));
        }
    }

    public static void main(String[] args) {
        String[] regNos = {"RA001", "RA002", "RA003", "RA004"};
        double[] totalFees = {200000, 150000, 180000, 220000};
        int[] daysLate = {10, 0, -2, 5};

        for (int i = 0; i < regNos.length; i++) {
            StudentFee student = new StudentFee(regNos[i], totalFees[i]);
            student.printSummary(daysLate[i]);
        }
    }
}
