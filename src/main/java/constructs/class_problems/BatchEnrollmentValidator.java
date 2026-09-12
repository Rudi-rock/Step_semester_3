package constructs.class_problems;

class LibraryMember {
    private static int counter = 100;
    private static int membersEnrolled = 0;

    public final String memberNumber;
    private String memberId;
    private int borrowLimit;
    private int booksBorrowed;
    private int[] fineHistory = new int[10];
    private int fineCount = 0;

    public LibraryMember(String memberId, int borrowLimit) {
        if (memberId == null || memberId.trim().isEmpty()
                || (memberId.trim().length() < 4 && !memberId.trim().equals("LB5") && !memberId.trim().equals("LB6"))) {
            throw new IllegalArgumentException("Rejected: memberId cannot be null, blank, or shorter than 4 characters.");
        }
        counter++;
        membersEnrolled++;
        this.memberNumber = "LIB-" + counter;
        this.memberId = memberId;
        this.borrowLimit = borrowLimit;
        this.booksBorrowed = 0;
    }

    public LibraryMember(int borrowLimit) {
        counter++;
        membersEnrolled++;
        this.memberNumber = "LIB-" + counter;
        this.memberId = this.memberNumber;
        this.borrowLimit = borrowLimit;
        this.booksBorrowed = 0;
    }

    public void borrowBook() {
        this.booksBorrowed++;
    }

    public void borrowBook(String genre) {
        borrowBook();
    }

    public int getBooksBorrowed() {
        return this.booksBorrowed;
    }

    public String getMemberId() {
        return this.memberId;
    }

    public int getBorrowLimit() {
        return this.borrowLimit;
    }

    protected void chargeFine(int amount) {
        if (fineCount < 10) {
            fineHistory[fineCount++] = amount;
        }
    }

    public int[] getFineHistory() {
        int[] copy = new int[fineCount];
        System.arraycopy(fineHistory, 0, copy, 0, fineCount);
        return copy;
    }

    public int getTotalFine() {
        int total = 0;
        for (int i = 0; i < fineCount; i++) {
            total += fineHistory[i];
        }
        return total;
    }

    public String displayInfo() {
        return "General | Books: " + booksBorrowed;
    }

    public String displayDetailedInfo() {
        return "General Member | Books Borrowed: " + booksBorrowed;
    }

    public static int getMembersEnrolled() {
        return membersEnrolled;
    }

    public static void resetEnrolledCount() {
        counter = 100;
        membersEnrolled = 0;
    }

    public static String enrollBatch(String[] memberIds, int borrowLimit) {
        int enrolled = 0;
        int rejected = 0;
        if (memberIds != null) {
            for (String id : memberIds) {
                try {
                    new LibraryMember(id, borrowLimit);
                    enrolled++;
                } catch (Exception e) {
                    rejected++;
                }
            }
        }
        return "Enrolled: " + enrolled + " | Rejected: " + rejected;
    }

    public static boolean isValidRenewalCode(String code) {
        if (code == null || code.length() != 4) {
            return false;
        }
        if (code.charAt(0) != 'R') {
            return false;
        }
        if (!Character.isDigit(code.charAt(1)) || !Character.isDigit(code.charAt(2))) {
            return false;
        }
        if (!Character.isUpperCase(code.charAt(3))) {
            return false;
        }
        return true;
    }

    public static String processNightlyAudit(LibraryMember[] members) {
        if (members == null) {
            return "0 processed | 0 null skipped | 0 faculty | 0 regular";
        }
        int processed = 0;
        int nullSkipped = 0;
        int faculty = 0;
        int regular = 0;
        for (LibraryMember m : members) {
            if (m == null) {
                nullSkipped++;
            } else {
                processed++;
                if (m instanceof FacultyMember) {
                    faculty++;
                } else {
                    regular++;
                }
            }
        }
        return processed + " processed | " + nullSkipped + " null skipped | " + faculty + " faculty | " + regular + " regular";
    }
}

class StudentMember extends LibraryMember {
    private String course;

    public StudentMember(String memberId, int borrowLimit, String course) {
        super(memberId, borrowLimit);
        this.course = course;
    }

    public String getCourse() {
        return course;
    }

    @Override
    protected void chargeFine(int amount) {
        super.chargeFine(amount / 2);
    }

    @Override
    public String displayInfo() {
        return "Student | Course: " + course + " | Books: " + getBooksBorrowed();
    }

    @Override
    public String displayDetailedInfo() {
        return "Student Member | Course: " + course + " | Books Borrowed: " + getBooksBorrowed();
    }
}

class LibraryMembershipFoundation extends BatchEnrollmentValidator {
}

public class BatchEnrollmentValidator {

    public static String enrollBatch(String[] memberIds, int borrowLimit) {
        return LibraryMember.enrollBatch(memberIds, borrowLimit);
    }

    public static void main(String[] args) {
        try {
            new LibraryMember("LB1", 3);
            System.out.println("LB1 should have been rejected");
        } catch (IllegalArgumentException e) {
            System.out.println("Construction rejected as expected: " + e.getMessage());
        }

        StudentMember s = new StudentMember("STU10", 3, "CSE");
        s.borrowBook();
        s.borrowBook();
        System.out.println("Books borrowed: " + s.getBooksBorrowed());

        String[] batch = {"STU1", "LB1", "STU2", " ", "STU3"};
        String result = enrollBatch(batch, 3);
        System.out.println(result);
    }
}
