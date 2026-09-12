package constructs.class_problems;

class TheWeeklyCirculationReport extends WeeklyCirculationReport {
}

public class WeeklyCirculationReport {

    public static String batchPrint(LibraryMember[] members) {
        if (members == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (LibraryMember m : members) {
            if (m == null) {
                continue;
            }
            // Polymorphic call to displayInfo() without using instanceof to decide output
            sb.append(m.displayInfo());

            // Safely guarded downcast only for genuine StudentMember instances to access course
            if (m instanceof StudentMember) {
                StudentMember sm = (StudentMember) m;
                sb.append(" [Course via downcast: ").append(sm.getCourse()).append("]");
            }
            sb.append(" | ");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        LibraryMember[] members = {
            new LibraryMember("LB5", 3),
            new StudentMember("STU6", 3, "ECE")
        };

        String report = batchPrint(members);
        System.out.println(report);

        // Demonstrating safe vs unsafe downcasting
        LibraryMember plain = new LibraryMember("LB6", 3);
        try {
            StudentMember bad = (StudentMember) plain;
            System.out.println("Downcast succeeded unexpectedly: " + bad);
        } catch (ClassCastException e) {
            System.out.println("Safeguard confirmed: plain LibraryMember cannot be downcast to StudentMember (" + e.getClass().getSimpleName() + ")");
        }
    }
}
