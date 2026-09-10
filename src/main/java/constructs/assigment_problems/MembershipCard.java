package constructs.assigment_problems;

public class MembershipCard {
    static String libraryName;
    static String libraryBranch;
    String studentName;

    static {
        libraryName = "Central Library";
        libraryBranch = "Campus South";
        System.out.println("Library info loaded");
    }

    public MembershipCard(String studentName) {
        this.studentName = studentName;
        System.out.println("Membership card created: " + studentName + " | Library: " + libraryName);
    }

    public static void main(String[] args) {
        String[] students = {"Ravi", "Meera", "Karthik", "Divya", "Anitha"};
        for (String student : students) {
            new MembershipCard(student);
        }
    }
}
