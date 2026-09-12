package constructs.class_problems;

import java.util.Arrays;

class TheStudentDiscountFineLedger extends StudentDiscountFineLedger {
}

public class StudentDiscountFineLedger {

    public static void main(String[] args) {
        StudentMember s = new StudentMember("STU5", 3, "CSE");
        s.chargeFine(100);
        System.out.println("Total fine after charging 100 with student 50% discount: " + s.getTotalFine());

        int[] history = s.getFineHistory();
        System.out.println("Fine history: " + Arrays.toString(history));

        // Attempting to mutate returned history array
        history[0] = 999;

        // Verify defensive copy preserved internal state
        int[] safeHistory = s.getFineHistory();
        System.out.println("Fine history after outside modification attempt: " + Arrays.toString(safeHistory));
        System.out.println("Tamper resistance verified: " + (safeHistory[0] == 50));
    }
}
