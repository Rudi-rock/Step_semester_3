package constructs.assignment_problems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CampusNoticeBroadcaster {

    // ==========================================
    // Abstraction: NotificationChannel (Loose Coupling)
    // ==========================================

    public interface NotificationChannel {
        String getChannelName();
        void sendNotification(Student student, Notice notice);
    }

    public static class EmailChannel implements NotificationChannel {
        @Override
        public String getChannelName() {
            return "Email";
        }

        @Override
        public void sendNotification(Student student, Notice notice) {
            System.out.println("  [EMAIL -> " + student.getEmail() + "] " +
                    "Subject: " + notice.getTitle() + " | Body: " + notice.getContent());
        }
    }

    public static class SmsChannel implements NotificationChannel {
        @Override
        public String getChannelName() {
            return "SMS";
        }

        @Override
        public void sendNotification(Student student, Notice notice) {
            System.out.println("  [SMS -> " + student.getPhone() + "] " +
                    "Alert: " + notice.getTitle());
        }
    }

    public static class AppChannel implements NotificationChannel {
        @Override
        public String getChannelName() {
            return "In-App Push";
        }

        @Override
        public void sendNotification(Student student, Notice notice) {
            System.out.println("  [APP PUSH -> " + student.getName() + " (Device Token)] " +
                    "Notification: " + notice.getTitle());
        }
    }

    // Extensibility Demonstration: Adding WhatsAppChannel without altering NoticeBoard
    public static class WhatsAppChannel implements NotificationChannel {
        @Override
        public String getChannelName() {
            return "WhatsApp";
        }

        @Override
        public void sendNotification(Student student, Notice notice) {
            System.out.println("  [WHATSAPP -> " + student.getPhone() + "] " +
                    "Direct Message: " + notice.getTitle() + " - " + notice.getContent());
        }
    }

    // ==========================================
    // Student Entity
    // ==========================================

    public static class Student {
        private final String studentId;
        private final String name;
        private final String department;
        private final String email;
        private final String phone;
        private final List<NotificationChannel> preferredChannels = new ArrayList<>();

        public Student(String studentId, String name, String department, String email, String phone) {
            if (studentId == null || studentId.trim().isEmpty()) {
                throw new IllegalArgumentException("Student ID cannot be empty.");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty.");
            }
            if (department == null || department.trim().isEmpty()) {
                throw new IllegalArgumentException("Department cannot be empty.");
            }
            this.studentId = studentId;
            this.name = name;
            this.department = department.toUpperCase();
            this.email = email;
            this.phone = phone;
        }

        public String getStudentId() {
            return studentId;
        }

        public String getName() {
            return name;
        }

        public String getDepartment() {
            return department;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        public void addPreferredChannel(NotificationChannel channel) {
            if (channel != null && !preferredChannels.contains(channel)) {
                preferredChannels.add(channel);
            }
        }

        public List<NotificationChannel> getPreferredChannels() {
            return Collections.unmodifiableList(preferredChannels);
        }

        @Override
        public String toString() {
            return name + " (" + studentId + " | Dept: " + department + ")";
        }
    }

    // ==========================================
    // Notice Entity
    // ==========================================

    public static class Notice {
        private final String noticeId;
        private final String title;
        private final String content;
        private final Set<String> targetDepartments = new HashSet<>();

        public Notice(String noticeId, String title, String content, Set<String> targetDepartments) {
            if (noticeId == null || noticeId.trim().isEmpty()) {
                throw new IllegalArgumentException("Notice ID cannot be empty.");
            }
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Title cannot be empty.");
            }
            if (content == null || content.trim().isEmpty()) {
                throw new IllegalArgumentException("Content cannot be empty.");
            }
            if (targetDepartments == null || targetDepartments.isEmpty()) {
                throw new IllegalArgumentException("At least one target department must be specified.");
            }

            this.noticeId = noticeId;
            this.title = title;
            this.content = content;
            for (String dept : targetDepartments) {
                this.targetDepartments.add(dept.toUpperCase());
            }
        }

        public String getNoticeId() {
            return noticeId;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public Set<String> getTargetDepartments() {
            return Collections.unmodifiableSet(targetDepartments);
        }

        @Override
        public String toString() {
            return "[" + noticeId + "] " + title + " -> Target Depts: " + targetDepartments;
        }
    }

    // ==========================================
    // NoticeBoard & Demonstration
    // ==========================================

    public static class NoticeBoard {
        private final List<Student> registeredStudents = new ArrayList<>();
        private final List<Notice> postedNotices = new ArrayList<>();

        public void registerStudent(Student student) {
            if (student != null) {
                registeredStudents.add(student);
            }
        }

        // NoticeBoard does not depend on any concrete NotificationChannel implementations (Loose Coupling)
        public void postNotice(Notice notice) {
            postedNotices.add(notice);
            System.out.println("\n>>> [BROADCASTING NOTICE] " + notice.getTitle() + " <<<");
            System.out.println("Targets: " + notice.getTargetDepartments());

            int recipientCount = 0;
            for (Student student : registeredStudents) {
                // Filter: Only students belonging to target departments receive the notice
                if (notice.getTargetDepartments().contains(student.getDepartment())) {
                    recipientCount++;
                    System.out.println("Delivering to " + student.getName() + " (" + student.getDepartment() + "):");
                    // Polymorphic dispatch across all student preferred channels
                    for (NotificationChannel channel : student.getPreferredChannels()) {
                        channel.sendNotification(student, notice);
                    }
                }
            }

            if (recipientCount == 0) {
                System.out.println("No matching students found for the target department(s).");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Campus Notice Broadcaster Demonstration ===");

        NoticeBoard noticeBoard = new NoticeBoard();

        // 1. Create Channels
        NotificationChannel email = new EmailChannel();
        NotificationChannel sms = new SmsChannel();
        NotificationChannel app = new AppChannel();
        NotificationChannel whatsApp = new WhatsAppChannel(); // Open-closed extensibility

        // 2. Setup Students with departments and channel preferences
        Student s1 = new Student("S101", "Aarav Sharma", "CSE", "aarav@campus.edu", "+91-9800000001");
        s1.addPreferredChannel(email);
        s1.addPreferredChannel(app);

        Student s2 = new Student("S102", "Diya Patel", "CSE", "diya@campus.edu", "+91-9800000002");
        s2.addPreferredChannel(email);
        s2.addPreferredChannel(sms);

        Student s3 = new Student("S103", "Rohan Verma", "ECE", "rohan@campus.edu", "+91-9800000003");
        s3.addPreferredChannel(app);
        s3.addPreferredChannel(whatsApp); // using newly added channel without touching NoticeBoard

        Student s4 = new Student("S104", "Manish Rao", "MECH", "manish@campus.edu", "+91-9800000004");
        s4.addPreferredChannel(sms);

        noticeBoard.registerStudent(s1);
        noticeBoard.registerStudent(s2);
        noticeBoard.registerStudent(s3);
        noticeBoard.registerStudent(s4);

        System.out.println("\nRegistered Students:");
        System.out.println(" - " + s1 + " Preferred: Email, App");
        System.out.println(" - " + s2 + " Preferred: Email, SMS");
        System.out.println(" - " + s3 + " Preferred: App, WhatsApp");
        System.out.println(" - " + s4 + " Preferred: SMS");

        // 3. Post Notice targeted to CSE only
        System.out.println("\n--- Step 1: Posting Notice for CSE only ---");
        Notice cseNotice = new Notice("N-01", "Hackathon Registration Open",
                "Sign up for Smart Campus Hackathon before Oct 15", Set.of("CSE"));
        noticeBoard.postNotice(cseNotice);

        // 4. Post Notice targeted to both CSE and ECE
        System.out.println("\n--- Step 2: Posting Notice for CSE and ECE ---");
        Notice techNotice = new Notice("N-02", "Robotics & AI Symposium",
                "Workshop on Embedded Systems in Auditorium A", Set.of("CSE", "ECE"));
        noticeBoard.postNotice(techNotice);

        // 5. Post Notice targeted to MECH only
        System.out.println("\n--- Step 3: Posting Notice for MECH only ---");
        Notice mechNotice = new Notice("N-03", "AutoCAD Certification Exam",
                "CAD lab 3 will host the certification on Monday", Set.of("MECH"));
        noticeBoard.postNotice(mechNotice);

        // 6. Post Notice targeted to CIVIL (No students registered in CIVIL)
        System.out.println("\n--- Step 4: Posting Notice for CIVIL (Untargeted Dept) ---");
        Notice civilNotice = new Notice("N-04", "Structural Design Seminar",
                "Seminar on green buildings", Set.of("CIVIL"));
        noticeBoard.postNotice(civilNotice);
    }
}
