public class WeeklyCheckInSettlement {

    static class GymMember {

        protected int monthlyFee;
        protected int sessionsAttended;

        private static int membersEnrolled = 0;

        public final String membershipNumber;

        private int feesPaid;

        public GymMember(int monthlyFee) {

            if (monthlyFee <= 0) {
                throw new IllegalArgumentException(
                        "Monthly fee must be positive"
                );
            }

            this.monthlyFee = monthlyFee;

            membersEnrolled++;

            membershipNumber =
                    "GYM-" + (2000 + membersEnrolled);

            feesPaid = 0;
        }

        public void payFee(int amount) {

            if (amount > 0) {
                feesPaid += amount;
            }
        }

        public void payFee(int amount, String mode) {

            // Mode is recorded conceptually,
            // then actual payment logic is reused.
            payFee(amount);
        }

        public int getFeesPaid() {
            return feesPaid;
        }

        public void attendSession() {
            sessionsAttended++;
        }

        public int getSessionsAttended() {
            return sessionsAttended;
        }

        public static boolean isValidReferralCode(String code) {

            if (code == null || code.length() != 4) {
                return false;
            }

            if (code.charAt(0) != 'G') {
                return false;
            }

            if (!Character.isDigit(code.charAt(1))) {
                return false;
            }

            if (!Character.isDigit(code.charAt(2))) {
                return false;
            }

            if (!Character.isUpperCase(code.charAt(3))) {
                return false;
            }

            return true;
        }

        public static int getMembersEnrolled() {
            return membersEnrolled;
        }
    }

    static class GroupClassMember extends GymMember {

        private String className;

        public GroupClassMember(
                int monthlyFee,
                String className) {

            super(monthlyFee);
            this.className = className;
        }
    }

    static String processWeeklyCheckIn(
            GymMember[] members) {

        int processed = 0;
        int nullSkipped = 0;
        int group = 0;
        int individual = 0;

        for (GymMember member : members) {

            if (member == null) {
                nullSkipped++;
                continue;
            }

            processed++;

            if (member instanceof GroupClassMember) {
                group++;
            } else {
                individual++;
            }
        }

        return processed + " processed | "
                + nullSkipped + " null skipped | "
                + group + " group | "
                + individual + " individual";
    }

    public static void main(String[] args) {

        GymMember m1 =
                new GymMember(1000);

        System.out.println(
                m1.membershipNumber
        );

        System.out.println(
                GymMember.getMembersEnrolled()
        );

        System.out.println(
                GymMember.isValidReferralCode("G45B")
        );

        System.out.println(
                GymMember.isValidReferralCode("G4B")
        );

        System.out.println(
                GymMember.isValidReferralCode("X45B")
        );

        m1.payFee(500);
        m1.payFee(500, "UPI");

        System.out.println(
                m1.getFeesPaid()
        );

        System.out.println(
                processWeeklyCheckIn(
                        new GymMember[]{
                                new GroupClassMember(
                                        1500,
                                        "Zumba"
                                ),
                                null,
                                new GymMember(1000)
                        }
                )
        );
    }
}