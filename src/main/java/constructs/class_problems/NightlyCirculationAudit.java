package constructs.class_problems;

class MembershipNumbersRenewalCodesNightlyCirculationAudit extends NightlyCirculationAudit {
}

public class NightlyCirculationAudit {

    public static boolean isValidRenewalCode(String code) {
        return LibraryMember.isValidRenewalCode(code);
    }

    public static String processNightlyAudit(LibraryMember[] members) {
        return LibraryMember.processNightlyAudit(members);
    }

    public static void main(String[] args) {
        LibraryMember.resetEnrolledCount();

        LibraryMember m1 = new LibraryMember(3);
        System.out.println("Member number: " + m1.memberNumber);
        System.out.println("Members enrolled: " + LibraryMember.getMembersEnrolled());

        System.out.println("isValidRenewalCode(\"R12A\"): " + isValidRenewalCode("R12A"));
        System.out.println("isValidRenewalCode(\"R1A\"): " + isValidRenewalCode("R1A"));
        System.out.println("isValidRenewalCode(\"X12A\"): " + isValidRenewalCode("X12A"));

        m1.borrowBook();
        m1.borrowBook("Fiction");
        System.out.println("Books borrowed: " + m1.getBooksBorrowed());

        LibraryMember[] auditList = {
            new FacultyMember(5, "Physics"),
            null,
            new LibraryMember(3)
        };

        String auditResult = processNightlyAudit(auditList);
        System.out.println("Audit result: " + auditResult);
    }
}
