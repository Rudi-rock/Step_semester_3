import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class StudentClubRegistrationManagement {
    private static final class Member {
        private final String rollNumber;
        private final String name;

        Member(String rollNumber, String name) {
            this.rollNumber = rollNumber;
            this.name = name;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof Member member && rollNumber.equals(member.rollNumber);
        }

        @Override
        public int hashCode() {
            return Objects.hash(rollNumber);
        }
    }

    public static void main(String[] args) throws IOException {
        Set<Member> members = new HashSet<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        boolean countPrinted = false;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length == 0 || parts[0].isEmpty()) {
                continue;
            }
            Member member = new Member(parts[1], parts[2]);
            if (parts[0].equals("ADD")) {
                System.out.println(members.add(member) ? "Added" : "duplicate rejected");
            } else if (parts[0].equals("CONTAINS")) {
                if (!countPrinted) {
                    System.out.println("member count " + members.size());
                    countPrinted = true;
                }
                System.out.println("contains: " + members.contains(member));
            }
        }
        if (!countPrinted) {
            System.out.println("member count " + members.size());
        }
    }
}
