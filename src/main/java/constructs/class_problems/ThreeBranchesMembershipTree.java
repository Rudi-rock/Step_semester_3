package constructs.class_problems;

class HonorsStudentMember extends StudentMember {
    private int bonusLimit;

    public HonorsStudentMember(String memberId, int borrowLimit, String course, int bonusLimit) {
        super(memberId, borrowLimit, course);
        this.bonusLimit = bonusLimit;
    }

    public int getBonusLimit() {
        return this.bonusLimit;
    }

    @Override
    public String displayInfo() {
        return "Honors Student | Course: " + getCourse() + " | Bonus Limit: " + bonusLimit + " | Books: " + getBooksBorrowed();
    }

    @Override
    public String displayDetailedInfo() {
        return "Honors Student Member | Course: " + getCourse() + " | Bonus Limit: " + bonusLimit + " | Books Borrowed: " + getBooksBorrowed();
    }
}

class FacultyMember extends LibraryMember {
    private String department;

    public FacultyMember(String memberId, int borrowLimit, String department) {
        super(memberId, borrowLimit);
        this.department = department;
    }

    public FacultyMember(int borrowLimit, String department) {
        super(borrowLimit);
        this.department = department;
    }

    public String getDepartment() {
        return this.department;
    }

    @Override
    public String displayInfo() {
        return "Faculty | Department: " + department + " | Books: " + getBooksBorrowed();
    }

    @Override
    public String displayDetailedInfo() {
        return "Faculty Member | Department: " + department + " | Books Borrowed: " + getBooksBorrowed();
    }
}

class MembershipTreeBranches extends ThreeBranchesMembershipTree {
}

public class ThreeBranchesMembershipTree {

    public static String classifyGeneration(LibraryMember member) {
        if (member instanceof HonorsStudentMember) {
            return "Multilevel descendant (3 generations deep)";
        } else if (member instanceof FacultyMember) {
            return "Hierarchical sibling (independent branch)";
        } else if (member instanceof StudentMember) {
            return "Direct descendant (2 generations deep)";
        } else if (member instanceof LibraryMember) {
            return "Root generation";
        }
        return "Unknown";
    }

    public static int getTotalBooksBorrowed(LibraryMember[] members) {
        if (members == null) {
            return 0;
        }
        int total = 0;
        for (LibraryMember m : members) {
            if (m != null) {
                total += m.getBooksBorrowed();
            }
        }
        return total;
    }

    public static void main(String[] args) {
        LibraryMember general = new LibraryMember("GEN01", 2);
        StudentMember student = new StudentMember("STU01", 3, "CSE");
        HonorsStudentMember honors = new HonorsStudentMember("HON01", 4, "ECE", 2);
        FacultyMember faculty = new FacultyMember("FAC01", 5, "Physics");

        System.out.println(general.displayDetailedInfo());
        System.out.println(student.displayDetailedInfo());
        System.out.println(honors.displayDetailedInfo());
        System.out.println(faculty.displayDetailedInfo());

        System.out.println(classifyGeneration(honors));
        System.out.println(classifyGeneration(faculty));

        student.borrowBook();
        student.borrowBook(); // 2

        honors.borrowBook(); // 1

        faculty.borrowBook();
        faculty.borrowBook();
        faculty.borrowBook(); // 3

        LibraryMember[] members = {student, honors, faculty};
        System.out.println("Total books borrowed: " + getTotalBooksBorrowed(members));
    }
}
