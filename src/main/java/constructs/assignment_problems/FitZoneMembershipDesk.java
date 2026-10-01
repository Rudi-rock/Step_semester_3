package constructs.assignment_problems;

import java.time.LocalDate;

public class FitZoneMembershipDesk {

    // ==========================================
    // Member Entity
    // ==========================================

    public static class Member {
        private final String memberId;
        private final String name;
        private final String phone;

        public Member(String memberId, String name, String phone) {
            if (memberId == null || memberId.trim().isEmpty()) {
                throw new IllegalArgumentException("Member ID cannot be empty.");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Member name cannot be empty.");
            }
            if (phone == null || phone.trim().isEmpty()) {
                throw new IllegalArgumentException("Phone cannot be empty.");
            }
            this.memberId = memberId;
            this.name = name;
            this.phone = phone;
        }

        public String getMemberId() {
            return memberId;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        @Override
        public String toString() {
            return name + " (ID: " + memberId + ", Phone: " + phone + ")";
        }
    }

    // ==========================================
    // MembershipPlan Abstraction (Polymorphic Fees)
    // ==========================================

    public static abstract class MembershipPlan {
        private final String planName;
        private final int durationMonths;

        public MembershipPlan(String planName, int durationMonths) {
            if (planName == null || planName.trim().isEmpty()) {
                throw new IllegalArgumentException("Plan name cannot be empty.");
            }
            if (durationMonths <= 0) {
                throw new IllegalArgumentException("Duration must be positive.");
            }
            this.planName = planName;
            this.durationMonths = durationMonths;
        }

        public String getPlanName() {
            return planName;
        }

        public int getDurationMonths() {
            return durationMonths;
        }

        public abstract double calculateFee();

        @Override
        public String toString() {
            return planName + " [" + durationMonths + " month(s), Fee: INR " + calculateFee() + "]";
        }
    }

    public static class MonthlyPlan extends MembershipPlan {
        private static final double BASE_RATE_PER_MONTH = 1000.0;

        public MonthlyPlan() {
            super("Monthly Plan", 1);
        }

        @Override
        public double calculateFee() {
            return BASE_RATE_PER_MONTH * getDurationMonths();
        }
    }

    public static class QuarterlyPlan extends MembershipPlan {
        private static final double BASE_RATE_PER_MONTH = 1000.0;
        private static final double DISCOUNT_RATE = 0.10; // 10% discount

        public QuarterlyPlan() {
            super("Quarterly Plan", 3);
        }

        @Override
        public double calculateFee() {
            double gross = BASE_RATE_PER_MONTH * getDurationMonths();
            return gross - (gross * DISCOUNT_RATE); // INR 2700
        }
    }

    public static class AnnualPlan extends MembershipPlan {
        private static final double BASE_RATE_PER_MONTH = 1000.0;
        private static final double DISCOUNT_RATE = 0.25; // 25% discount

        public AnnualPlan() {
            super("Annual Plan", 12);
        }

        @Override
        public double calculateFee() {
            double gross = BASE_RATE_PER_MONTH * getDurationMonths();
            return gross - (gross * DISCOUNT_RATE); // INR 9000
        }
    }

    // ==========================================
    // Membership & State Management
    // ==========================================

    public enum MembershipStatus {
        Active,
        Frozen,
        Expired
    }

    public static class Membership {
        private final String membershipId;
        private final Member member;
        private final MembershipPlan plan;
        private final LocalDate startDate;
        private final LocalDate endDate;
        private final double membershipFee;
        private MembershipStatus status;

        public Membership(String membershipId, Member member, MembershipPlan plan, LocalDate startDate) {
            if (membershipId == null || membershipId.trim().isEmpty()) {
                throw new IllegalArgumentException("Membership ID cannot be empty.");
            }
            if (member == null) {
                throw new IllegalArgumentException("Member cannot be null.");
            }
            if (plan == null) {
                throw new IllegalArgumentException("Plan cannot be null.");
            }
            if (startDate == null) {
                throw new IllegalArgumentException("Start date cannot be null.");
            }

            this.membershipId = membershipId;
            this.member = member;
            this.plan = plan;
            this.startDate = startDate;
            this.endDate = startDate.plusMonths(plan.getDurationMonths());
            this.membershipFee = plan.calculateFee();
            this.status = MembershipStatus.Active;
        }

        public String getMembershipId() {
            return membershipId;
        }

        public Member getMember() {
            return member;
        }

        public MembershipPlan getPlan() {
            return plan;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public double getMembershipFee() {
            return membershipFee;
        }

        public MembershipStatus getStatus() {
            return status;
        }

        public boolean checkIn() {
            if (status == MembershipStatus.Active) {
                System.out.println("[CHECK-IN SUCCESS] " + member.getName() + " checked in successfully.");
                return true;
            } else if (status == MembershipStatus.Frozen) {
                System.out.println("[CHECK-IN BLOCKED] " + member.getName() +
                        "'s membership is currently FROZEN. Please unfreeze before check-in.");
                return false;
            } else {
                System.out.println("[CHECK-IN BLOCKED] " + member.getName() +
                        "'s membership is EXPIRED. Please renew to check in.");
                return false;
            }
        }

        public boolean freeze() {
            if (status == MembershipStatus.Expired) {
                System.out.println("[ACTION BLOCKED] Cannot freeze membership " + membershipId + " because it is EXPIRED.");
                return false;
            }
            if (status == MembershipStatus.Frozen) {
                System.out.println("[WARNING] Membership " + membershipId + " is already frozen.");
                return false;
            }
            this.status = MembershipStatus.Frozen;
            System.out.println("[STATUS CHANGE] Membership " + membershipId + " for " + member.getName() + " is now FROZEN.");
            return true;
        }

        public boolean unfreeze() {
            if (status == MembershipStatus.Expired) {
                System.out.println("[ACTION BLOCKED] Cannot unfreeze membership " + membershipId + " because it is EXPIRED.");
                return false;
            }
            if (status == MembershipStatus.Active) {
                System.out.println("[WARNING] Membership " + membershipId + " is already active.");
                return false;
            }
            this.status = MembershipStatus.Active;
            System.out.println("[STATUS CHANGE] Membership " + membershipId + " for " + member.getName() + " is now ACTIVE.");
            return true;
        }

        public void expire() {
            this.status = MembershipStatus.Expired;
            System.out.println("[STATUS CHANGE] Membership " + membershipId + " for " + member.getName() + " has EXPIRED.");
        }

        @Override
        public String toString() {
            return "Membership #" + membershipId + " | " + member.getName() + " | " + plan.getPlanName() +
                    " | Valid: " + startDate + " to " + endDate + " | Fee: INR " + membershipFee + " | Status: " + status;
        }
    }

    // ==========================================
    // FitZone Membership Desk Demonstration
    // ==========================================

    public static void main(String[] args) {
        System.out.println("=== FitZone Membership Desk Demonstration ===");

        MembershipPlan monthly = new MonthlyPlan();
        MembershipPlan quarterly = new QuarterlyPlan();
        MembershipPlan annual = new AnnualPlan();

        System.out.println("\nAvailable Membership Plans (Polymorphic Pricing):");
        System.out.println(" - " + monthly);
        System.out.println(" - " + quarterly);
        System.out.println(" - " + annual);

        Member member1 = new Member("MEM-101", "Rohit Sharma", "9820098200");
        Membership membership1 = new Membership("MBR-001", member1, quarterly, LocalDate.of(2026, 10, 1));
        System.out.println("\nCreated Membership: " + membership1);

        System.out.println("\n--- Step 1: Member checks in while ACTIVE ---");
        membership1.checkIn();

        System.out.println("\n--- Step 2: Member requests to freeze membership ---");
        membership1.freeze();

        System.out.println("\n--- Step 3: Member attempts check-in while FROZEN ---");
        membership1.checkIn();

        System.out.println("\n--- Step 4: Member unfreezes membership ---");
        membership1.unfreeze();

        System.out.println("\n--- Step 5: Member checks in after unfreezing ---");
        membership1.checkIn();

        System.out.println("\n--- Step 6: Membership expires ---");
        membership1.expire();

        System.out.println("\n--- Step 7: Attempting operations on EXPIRED membership ---");
        System.out.print("Check-in attempt: ");
        membership1.checkIn();

        System.out.print("Freeze attempt: ");
        membership1.freeze();

        System.out.print("Unfreeze attempt: ");
        membership1.unfreeze();

        System.out.println("\n--- Step 8: Annual Plan Registration ---");
        Member member2 = new Member("MEM-102", "Smriti Mandhana", "9811198111");
        Membership membership2 = new Membership("MBR-002", member2, annual, LocalDate.of(2026, 10, 1));
        System.out.println(membership2);
        membership2.checkIn();
    }
}
