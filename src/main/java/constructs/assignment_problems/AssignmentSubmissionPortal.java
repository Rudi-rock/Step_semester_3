package constructs.assignment_problems;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class AssignmentSubmissionPortal {

    // ==========================================
    // Student Entity
    // ==========================================

    public static class Student {
        private final String studentId;
        private final String name;

        public Student(String studentId, String name) {
            if (studentId == null || studentId.trim().isEmpty()) {
                throw new IllegalArgumentException("Student ID cannot be empty.");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Student name cannot be empty.");
            }
            this.studentId = studentId;
            this.name = name;
        }

        public String getStudentId() {
            return studentId;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name + " (" + studentId + ")";
        }
    }

    // ==========================================
    // Assignment Hierarchy & Polymorphic Penalties
    // ==========================================

    public static abstract class Assignment {
        private final String assignmentId;
        private final String title;
        private final LocalDateTime dueDate;
        private final double maxMarks;

        public Assignment(String assignmentId, String title, LocalDateTime dueDate, double maxMarks) {
            if (assignmentId == null || assignmentId.trim().isEmpty()) {
                throw new IllegalArgumentException("Assignment ID cannot be empty.");
            }
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Title cannot be empty.");
            }
            if (dueDate == null) {
                throw new IllegalArgumentException("Due date cannot be null.");
            }
            if (maxMarks <= 0) {
                throw new IllegalArgumentException("Max marks must be greater than zero.");
            }
            this.assignmentId = assignmentId;
            this.title = title;
            this.dueDate = dueDate;
            this.maxMarks = maxMarks;
        }

        public String getAssignmentId() {
            return assignmentId;
        }

        public String getTitle() {
            return title;
        }

        public LocalDateTime getDueDate() {
            return dueDate;
        }

        public double getMaxMarks() {
            return maxMarks;
        }

        public abstract String getAssignmentType();

        // Polymorphic late penalty calculation based on assignment type
        public abstract double calculateLatePenalty(double rawScore, int lateDays);

        public int calculateLateDays(LocalDateTime submissionTime) {
            if (!submissionTime.isAfter(dueDate)) {
                return 0;
            }
            long hoursLate = ChronoUnit.HOURS.between(dueDate, submissionTime);
            return (int) Math.ceil((double) hoursLate / 24.0);
        }

        @Override
        public String toString() {
            return "[" + getAssignmentType() + "] " + title + " (Due: " + dueDate + ", Max: " + maxMarks + " marks)";
        }
    }

    public static class CodingAssignment extends Assignment {
        private static final double PENALTY_PER_DAY = 0.10; // 10% per late day

        public CodingAssignment(String assignmentId, String title, LocalDateTime dueDate, double maxMarks) {
            super(assignmentId, title, dueDate, maxMarks);
        }

        @Override
        public String getAssignmentType() {
            return "Coding";
        }

        @Override
        public double calculateLatePenalty(double rawScore, int lateDays) {
            if (lateDays <= 0) return 0.0;
            return rawScore * PENALTY_PER_DAY * lateDays;
        }
    }

    public static class WrittenAssignment extends Assignment {
        private static final double PENALTY_PER_DAY = 0.20; // 20% per late day

        public WrittenAssignment(String assignmentId, String title, LocalDateTime dueDate, double maxMarks) {
            super(assignmentId, title, dueDate, maxMarks);
        }

        @Override
        public String getAssignmentType() {
            return "Written";
        }

        @Override
        public double calculateLatePenalty(double rawScore, int lateDays) {
            if (lateDays <= 0) return 0.0;
            return rawScore * PENALTY_PER_DAY * lateDays;
        }
    }

    // ==========================================
    // Submission Lifecycle & Grading
    // ==========================================

    public enum SubmissionStatus {
        Submitted,
        Graded
    }

    public static class Submission {
        private final String submissionId;
        private final Student student;
        private final Assignment assignment;
        private LocalDateTime submissionTime;
        private String content;
        private SubmissionStatus status;
        private double rawMarks;
        private double penaltyApplied;
        private double finalMarks;

        public Submission(String submissionId, Student student, Assignment assignment, LocalDateTime submissionTime, String content) {
            if (submissionId == null || submissionId.trim().isEmpty()) {
                throw new IllegalArgumentException("Submission ID cannot be empty.");
            }
            if (student == null) {
                throw new IllegalArgumentException("Student cannot be null.");
            }
            if (assignment == null) {
                throw new IllegalArgumentException("Assignment cannot be null.");
            }
            if (submissionTime == null) {
                throw new IllegalArgumentException("Submission time cannot be null.");
            }
            if (content == null || content.trim().isEmpty()) {
                throw new IllegalArgumentException("Content cannot be empty.");
            }

            this.submissionId = submissionId;
            this.student = student;
            this.assignment = assignment;
            this.submissionTime = submissionTime;
            this.content = content;
            this.status = SubmissionStatus.Submitted;
        }

        public String getSubmissionId() {
            return submissionId;
        }

        public Student getStudent() {
            return student;
        }

        public Assignment getAssignment() {
            return assignment;
        }

        public LocalDateTime getSubmissionTime() {
            return submissionTime;
        }

        public SubmissionStatus getStatus() {
            return status;
        }

        public double getRawMarks() {
            return rawMarks;
        }

        public double getPenaltyApplied() {
            return penaltyApplied;
        }

        public double getFinalMarks() {
            return finalMarks;
        }

        public void resubmit(LocalDateTime newTime, String newContent) {
            if (status == SubmissionStatus.Graded) {
                throw new IllegalStateException("Cannot resubmit assignment " + assignment.getTitle() +
                        " for student " + student.getName() + " because it is already GRADED.");
            }
            this.submissionTime = newTime;
            this.content = newContent;
            System.out.println("[RESUBMISSION] Updated submission for " + student.getName() + " at " + newTime);
        }

        public void grade(double awardedRawScore) {
            if (status != SubmissionStatus.Submitted) {
                throw new IllegalStateException("Assignment cannot be graded because its status is: " + status);
            }
            if (awardedRawScore < 0 || awardedRawScore > assignment.getMaxMarks()) {
                throw new IllegalArgumentException("Awarded score must be between 0 and " + assignment.getMaxMarks());
            }

            int lateDays = assignment.calculateLateDays(submissionTime);
            double penalty = assignment.calculateLatePenalty(awardedRawScore, lateDays);
            if (penalty > awardedRawScore) {
                penalty = awardedRawScore;
            }

            this.rawMarks = awardedRawScore;
            this.penaltyApplied = penalty;
            this.finalMarks = awardedRawScore - penalty;
            this.status = SubmissionStatus.Graded;

            System.out.println("[GRADED] Submission " + submissionId + " (" + student.getName() +
                    ") | Type: " + assignment.getAssignmentType() + " | Late Days: " + lateDays +
                    " | Raw Marks: " + rawMarks + " | Penalty: -" + penaltyApplied + " | Final Marks: " + finalMarks);
        }

        @Override
        public String toString() {
            return "Submission #" + submissionId + " | " + student.getName() + " -> " + assignment.getTitle() +
                    " | Status: " + status + (status == SubmissionStatus.Graded ? " [Final Score: " + finalMarks + "/" + assignment.getMaxMarks() + "]" : "");
        }
    }

    // ==========================================
    // Demonstration
    // ==========================================

    public static void main(String[] args) {
        System.out.println("=== Assignment Submission Portal Demonstration ===");

        LocalDateTime baseDue = LocalDateTime.of(2026, 10, 5, 23, 59);

        // 1. Create Coding and Written Assignments
        Assignment codingAssignment = new CodingAssignment("ASG-CS101", "DSA Binary Search Tree", baseDue, 100.0);
        Assignment writtenAssignment = new WrittenAssignment("ASG-ENG201", "Technical Essay on Ethics", baseDue, 100.0);

        System.out.println("\nCreated Assignments:");
        System.out.println(" - " + codingAssignment);
        System.out.println(" - " + writtenAssignment);

        Student s1 = new Student("STU-1", "Kunal Kamra");
        Student s2 = new Student("STU-2", "Pooja Hegde");
        Student s3 = new Student("STU-3", "Tarun Roy");

        // 2. Student 1: Submits Coding Assignment ON TIME (Oct 5 at 20:00)
        System.out.println("\n--- Step 1: On-time Coding Submission ---");
        Submission sub1 = new Submission("SUB-01", s1, codingAssignment,
                LocalDateTime.of(2026, 10, 5, 20, 0), "public class BST { ... }");
        System.out.println("Status before grading: " + sub1.getStatus());
        sub1.grade(95.0);

        // 3. Student 2: Submits Coding Assignment 2 DAYS LATE (Oct 7 at 12:00)
        System.out.println("\n--- Step 2: Late Coding Submission (2 days late) ---");
        Submission sub2 = new Submission("SUB-02", s2, codingAssignment,
                LocalDateTime.of(2026, 10, 7, 12, 0), "class BSTSolution { ... }");
        sub2.grade(90.0);

        // 4. Student 3: Submits Written Assignment 2 DAYS LATE (Oct 7 at 12:00)
        System.out.println("\n--- Step 3: Late Written Submission (2 days late) ---");
        Submission sub3 = new Submission("SUB-03", s3, writtenAssignment,
                LocalDateTime.of(2026, 10, 7, 12, 0), "Ethics Essay Draft");
        sub3.grade(90.0);

        // 5. Attempting to resubmit after being graded (Forbidden)
        System.out.println("\n--- Step 4: Attempting to resubmit already GRADED assignment ---");
        try {
            sub1.resubmit(LocalDateTime.of(2026, 10, 8, 10, 0), "Updated BST Code");
        } catch (IllegalStateException e) {
            System.out.println("[BLOCKED] Caught expected exception: " + e.getMessage());
        }

        // 6. Resubmission allowed BEFORE grading
        System.out.println("\n--- Step 5: Resubmission allowed BEFORE grading ---");
        Submission sub4 = new Submission("SUB-04", s1, writtenAssignment,
                LocalDateTime.of(2026, 10, 4, 15, 0), "First Essay Draft");
        System.out.println("Created: " + sub4);
        sub4.resubmit(LocalDateTime.of(2026, 10, 5, 18, 0), "Polished Final Essay");
        sub4.grade(88.0);
    }
}
