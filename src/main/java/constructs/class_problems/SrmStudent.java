package constructs.class_problems;

public class SrmStudent {
    static String collegeName;
    static String academicYear;

    static {
        collegeName = "SRMIST";
        academicYear = "2025-26";
        System.out.println("College info loaded");
    }

    public SrmStudent(String name) {
        System.out.println("Student record created: " + name);
    }

    public static void main(String[] args) {
        String[] names = {"Ravi", "Meera", "Karthik", "Divya", "Anitha"};
        for (String name : names) {
            new SrmStudent(name);
        }
    }
}
