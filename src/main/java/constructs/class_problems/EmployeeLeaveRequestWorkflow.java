package constructs.class_problems;

public class EmployeeLeaveRequestWorkflow {

    // ==========================================
    // Status Enum & Reviewer
    // ==========================================

    public enum LeaveStatus {
        PENDING,
        UNDER_REVIEW,
        APPROVED,
        REJECTED
    }

    public static class Reviewer {
        private final String reviewerId;
        private final String name;
        private final String designation;

        public Reviewer(String reviewerId, String name, String designation) {
            if (reviewerId == null || reviewerId.trim().isEmpty()) {
                throw new IllegalArgumentException("Reviewer ID cannot be empty.");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Reviewer name cannot be empty.");
            }
            this.reviewerId = reviewerId;
            this.name = name;
            this.designation = designation;
        }

        public String getReviewerId() {
            return reviewerId;
        }

        public String getName() {
            return name;
        }

        public String getDesignation() {
            return designation;
        }

        @Override
        public String toString() {
            return name + " (" + designation + ")";
        }
    }

    // ==========================================
    // Employee Hierarchy (Abstraction & Polymorphism)
    // ==========================================

    public static abstract class Employee {
        private final String employeeId;
        private final String name;
        private int leaveBalance;

        public Employee(String employeeId, String name, int leaveBalance) {
            if (employeeId == null || employeeId.trim().isEmpty()) {
                throw new IllegalArgumentException("Employee ID cannot be empty.");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Employee name cannot be empty.");
            }
            if (leaveBalance < 0) {
                throw new IllegalArgumentException("Leave balance cannot be negative.");
            }
            this.employeeId = employeeId;
            this.name = name;
            this.leaveBalance = leaveBalance;
        }

        public String getEmployeeId() {
            return employeeId;
        }

        public String getName() {
            return name;
        }

        public int getLeaveBalance() {
            return leaveBalance;
        }

        public void deductLeave(int days) {
            if (days > leaveBalance) {
                throw new IllegalArgumentException("Cannot deduct more days than available balance.");
            }
            this.leaveBalance -= days;
        }

        public abstract String getEmployeeType();

        public abstract boolean validateLeavePolicy(int days);

        @Override
        public String toString() {
            return "[" + getEmployeeType() + "] " + name + " (ID: " + employeeId + ", Balance: " + leaveBalance + " days)";
        }
    }

    public static class FullTimeEmployee extends Employee {
        private static final int MAX_CONSECUTIVE_DAYS = 15;

        public FullTimeEmployee(String employeeId, String name, int leaveBalance) {
            super(employeeId, name, leaveBalance);
        }

        @Override
        public String getEmployeeType() {
            return "Full-Time Employee";
        }

        @Override
        public boolean validateLeavePolicy(int days) {
            if (days <= 0) return false;
            if (days > MAX_CONSECUTIVE_DAYS) {
                System.out.println("[POLICY REJECTION] Full-time employees cannot take more than " +
                        MAX_CONSECUTIVE_DAYS + " consecutive days without HR approval.");
                return false;
            }
            if (days > getLeaveBalance()) {
                System.out.println("[POLICY REJECTION] Insufficient leave balance. Requested: " +
                        days + ", Available: " + getLeaveBalance());
                return false;
            }
            return true;
        }
    }

    public static class PartTimeEmployee extends Employee {
        private static final int MAX_CONSECUTIVE_DAYS = 5;

        public PartTimeEmployee(String employeeId, String name, int leaveBalance) {
            super(employeeId, name, leaveBalance);
        }

        @Override
        public String getEmployeeType() {
            return "Part-Time Employee";
        }

        @Override
        public boolean validateLeavePolicy(int days) {
            if (days <= 0) return false;
            if (days > MAX_CONSECUTIVE_DAYS) {
                System.out.println("[POLICY REJECTION] Part-time employees cannot take more than " +
                        MAX_CONSECUTIVE_DAYS + " consecutive days.");
                return false;
            }
            if (days > getLeaveBalance()) {
                System.out.println("[POLICY REJECTION] Insufficient leave balance for part-time employee.");
                return false;
            }
            return true;
        }
    }

    public static class Contractor extends Employee {
        private static final int MAX_UNPAID_DAYS = 3;

        public Contractor(String employeeId, String name) {
            super(employeeId, name, 0);
        }

        @Override
        public String getEmployeeType() {
            return "Contractor";
        }

        @Override
        public boolean validateLeavePolicy(int days) {
            if (days <= 0) return false;
            if (days > MAX_UNPAID_DAYS) {
                System.out.println("[POLICY REJECTION] Contractors are allowed a maximum of " +
                        MAX_UNPAID_DAYS + " unpaid emergency leave days per engagement.");
                return false;
            }
            return true;
        }
    }

    // ==========================================
    // Controlled State Transition LeaveRequest
    // ==========================================

    public static class LeaveRequest {
        private final String requestId;
        private final Employee employee;
        private final int days;
        private final String reason;
        private LeaveStatus status;
        private Reviewer reviewedBy;
        private String reviewerComments;

        public LeaveRequest(String requestId, Employee employee, int days, String reason) {
            if (requestId == null || requestId.trim().isEmpty()) {
                throw new IllegalArgumentException("Request ID cannot be empty.");
            }
            if (employee == null) {
                throw new IllegalArgumentException("Employee cannot be null.");
            }
            if (days <= 0) {
                throw new IllegalArgumentException("Leave days must be greater than zero.");
            }
            if (reason == null || reason.trim().isEmpty()) {
                throw new IllegalArgumentException("Reason cannot be empty.");
            }

            this.requestId = requestId;
            this.employee = employee;
            this.days = days;
            this.reason = reason;
            this.status = LeaveStatus.PENDING;
        }

        public String getRequestId() {
            return requestId;
        }

        public Employee getEmployee() {
            return employee;
        }

        public int getDays() {
            return days;
        }

        public String getReason() {
            return reason;
        }

        public LeaveStatus getStatus() {
            return status;
        }

        public Reviewer getReviewedBy() {
            return reviewedBy;
        }

        public String getReviewerComments() {
            return reviewerComments;
        }

        // Controlled State Transition: PENDING -> UNDER_REVIEW
        public boolean review(Reviewer reviewer, String comments) {
            if (status == LeaveStatus.APPROVED || status == LeaveStatus.REJECTED) {
                System.out.println("[ERROR] Cannot review request " + requestId +
                        " because it is already in final state: " + status);
                return false;
            }
            if (reviewer == null) {
                System.out.println("[ERROR] Reviewer cannot be null.");
                return false;
            }

            this.status = LeaveStatus.UNDER_REVIEW;
            this.reviewedBy = reviewer;
            this.reviewerComments = comments;
            System.out.println("[STATE CHANGE] Request " + requestId + " is now UNDER_REVIEW by " + reviewer.getName());
            return true;
        }

        // Controlled State Transition: UNDER_REVIEW -> APPROVED
        public boolean approve(Reviewer reviewer) {
            if (status == LeaveStatus.PENDING) {
                System.out.println("[ERROR] Request " + requestId +
                        " cannot be approved directly! It MUST be reviewed first.");
                return false;
            }
            if (status == LeaveStatus.APPROVED || status == LeaveStatus.REJECTED) {
                System.out.println("[ERROR] Cannot approve request " + requestId +
                        " because it is already finalized (" + status + ").");
                return false;
            }

            if (!employee.validateLeavePolicy(days)) {
                System.out.println("[REJECTED ON APPROVAL] Leave policy check failed during approval.");
                this.status = LeaveStatus.REJECTED;
                this.reviewedBy = reviewer;
                this.reviewerComments = "Auto-rejected due to policy failure";
                return false;
            }

            this.status = LeaveStatus.APPROVED;
            this.reviewedBy = reviewer;
            if (employee.getLeaveBalance() >= days) {
                employee.deductLeave(days);
            }
            System.out.println("[STATE CHANGE] Request " + requestId + " has been APPROVED by " + reviewer.getName());
            return true;
        }

        // Controlled State Transition: UNDER_REVIEW -> REJECTED
        public boolean reject(Reviewer reviewer, String reason) {
            if (status == LeaveStatus.PENDING) {
                System.out.println("[ERROR] Request " + requestId +
                        " cannot be rejected directly! It MUST be reviewed first.");
                return false;
            }
            if (status == LeaveStatus.APPROVED || status == LeaveStatus.REJECTED) {
                System.out.println("[ERROR] Cannot reject request " + requestId +
                        " because it is already finalized (" + status + ").");
                return false;
            }

            this.status = LeaveStatus.REJECTED;
            this.reviewedBy = reviewer;
            this.reviewerComments = reason;
            System.out.println("[STATE CHANGE] Request " + requestId + " has been REJECTED by " + reviewer.getName() +
                    ". Reason: " + reason);
            return true;
        }

        // Controlled State Transition: Attempting to reset back to PENDING is strictly forbidden
        public void resetToPending() {
            if (status == LeaveStatus.APPROVED || status == LeaveStatus.REJECTED) {
                throw new IllegalStateException("Approved or Rejected requests cannot return to Pending!");
            }
            this.status = LeaveStatus.PENDING;
        }

        @Override
        public String toString() {
            return "LeaveRequest [" + requestId + "] Employee: " + employee.getName() +
                    ", Days: " + days + ", Status: " + status +
                    (reviewedBy != null ? ", Reviewer: " + reviewedBy.getName() : "");
        }
    }

    // ==========================================
    // Demonstration
    // ==========================================

    public static void main(String[] args) {
        System.out.println("=== Employee Leave Request Workflow Demonstration ===");

        // 1. Create different types of employees
        Employee fullTime = new FullTimeEmployee("EMP-001", "Aditi Rao", 20);
        Employee partTime = new PartTimeEmployee("EMP-002", "Bhavik Shah", 5);
        Employee contractor = new Contractor("EMP-003", "Chetan Verma");

        Reviewer manager = new Reviewer("REV-101", "Sunil Joshi", "Engineering Manager");

        System.out.println("\nRegistered Employees:");
        System.out.println(" - " + fullTime);
        System.out.println(" - " + partTime);
        System.out.println(" - " + contractor);

        // 2. Full-time employee submits valid leave request
        System.out.println("\n--- Step 1: Full-time Employee submits 5 days leave ---");
        LeaveRequest req1 = new LeaveRequest("LR-001", fullTime, 5, "Family vacation");
        System.out.println("Created: " + req1);

        // 3. Attempt to approve directly without review (Should be blocked)
        System.out.println("\n--- Step 2: Attempting to approve directly from PENDING (Forbidden) ---");
        boolean approvedDirectly = req1.approve(manager);
        System.out.println("Direct approval result: " + approvedDirectly);

        // 4. Properly review the request
        System.out.println("\n--- Step 3: Manager reviews request LR-001 ---");
        req1.review(manager, "Project deliverables are on schedule, leave can be granted.");
        System.out.println("Current status: " + req1.getStatus());

        // 5. Approve the reviewed request
        System.out.println("\n--- Step 4: Manager approves request LR-001 ---");
        req1.approve(manager);
        System.out.println("Final status: " + req1.getStatus());
        System.out.println("Updated Employee: " + fullTime);

        // 6. Attempt to reset an approved request back to PENDING (Must fail)
        System.out.println("\n--- Step 5: Attempting to reset APPROVED request back to PENDING ---");
        try {
            req1.resetToPending();
        } catch (IllegalStateException e) {
            System.out.println("[BLOCKED] Exception caught as expected: " + e.getMessage());
        }

        // 7. Part-time employee submits excessive leave (Policy violation)
        System.out.println("\n--- Step 6: Part-Time Employee applies for 8 days (Policy Limit: 5) ---");
        LeaveRequest req2 = new LeaveRequest("LR-002", partTime, 8, "Attending conference");
        req2.review(manager, "Reviewing 8 days request against policy.");
        req2.approve(manager); // Will fail policy check and reject
        System.out.println("Request LR-002 status: " + req2.getStatus());

        // 8. Contractor applies for valid emergency leave
        System.out.println("\n--- Step 7: Contractor applies for 2 days leave ---");
        LeaveRequest req3 = new LeaveRequest("LR-003", contractor, 2, "Medical appointment");
        req3.review(manager, "Stand-in contractor assigned for two days.");
        req3.approve(manager);
        System.out.println("Request LR-003 status: " + req3.getStatus());
    }
}
