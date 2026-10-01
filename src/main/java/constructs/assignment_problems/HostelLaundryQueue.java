package constructs.assignment_problems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HostelLaundryQueue {

    // ==========================================
    // Abstraction: WashType Interface (Open-Closed Principle)
    // ==========================================

    public interface WashType {
        String getTypeName();
        int getDurationMinutes();
        double getCost();
    }

    public static class QuickWash implements WashType {
        @Override
        public String getTypeName() {
            return "Quick Wash";
        }

        @Override
        public int getDurationMinutes() {
            return 30;
        }

        @Override
        public double getCost() {
            return 20.0;
        }

        @Override
        public String toString() {
            return getTypeName() + " (" + getDurationMinutes() + " mins, INR " + getCost() + ")";
        }
    }

    public static class NormalWash implements WashType {
        @Override
        public String getTypeName() {
            return "Normal Wash";
        }

        @Override
        public int getDurationMinutes() {
            return 45;
        }

        @Override
        public double getCost() {
            return 30.0;
        }

        @Override
        public String toString() {
            return getTypeName() + " (" + getDurationMinutes() + " mins, INR " + getCost() + ")";
        }
    }

    public static class HeavyWash implements WashType {
        @Override
        public String getTypeName() {
            return "Heavy Wash";
        }

        @Override
        public int getDurationMinutes() {
            return 60;
        }

        @Override
        public double getCost() {
            return 45.0;
        }

        @Override
        public String toString() {
            return getTypeName() + " (" + getDurationMinutes() + " mins, INR " + getCost() + ")";
        }
    }

    // Newly added wash type without modifying any core booking logic!
    public static class SanitizeDelicateWash implements WashType {
        @Override
        public String getTypeName() {
            return "Sanitize & Delicate Wash";
        }

        @Override
        public int getDurationMinutes() {
            return 75;
        }

        @Override
        public double getCost() {
            return 65.0;
        }

        @Override
        public String toString() {
            return getTypeName() + " (" + getDurationMinutes() + " mins, INR " + getCost() + ")";
        }
    }

    // ==========================================
    // Student Entity
    // ==========================================

    public static class Student {
        private final String studentId;
        private final String name;
        private final String roomNumber;

        public Student(String studentId, String name, String roomNumber) {
            if (studentId == null || studentId.trim().isEmpty()) {
                throw new IllegalArgumentException("Student ID cannot be empty.");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Student name cannot be empty.");
            }
            this.studentId = studentId;
            this.name = name;
            this.roomNumber = roomNumber;
        }

        public String getStudentId() {
            return studentId;
        }

        public String getName() {
            return name;
        }

        public String getRoomNumber() {
            return roomNumber;
        }

        @Override
        public String toString() {
            return name + " (" + studentId + ", Room: " + roomNumber + ")";
        }
    }

    // ==========================================
    // WashingMachine & WashCycle
    // ==========================================

    public static class WashingMachine {
        private final String machineId;
        private boolean isBusy;
        private WashCycle currentCycle;

        public WashingMachine(String machineId) {
            if (machineId == null || machineId.trim().isEmpty()) {
                throw new IllegalArgumentException("Machine ID cannot be empty.");
            }
            this.machineId = machineId;
            this.isBusy = false;
            this.currentCycle = null;
        }

        public String getMachineId() {
            return machineId;
        }

        public boolean isBusy() {
            return isBusy;
        }

        public WashCycle getCurrentCycle() {
            return currentCycle;
        }

        public void assignCycle(WashCycle cycle) {
            if (this.isBusy) {
                throw new IllegalStateException("Machine " + machineId + " is already BUSY with another cycle!");
            }
            this.isBusy = true;
            this.currentCycle = cycle;
        }

        public void release() {
            if (!this.isBusy) {
                throw new IllegalStateException("Machine " + machineId + " is not currently running any wash.");
            }
            this.isBusy = false;
            this.currentCycle = null;
        }

        @Override
        public String toString() {
            return "Machine " + machineId + " [Status: " + (isBusy ? "BUSY" : "AVAILABLE") + "]";
        }
    }

    public static class WashCycle {
        private final String cycleId;
        private final Student student;
        private final WashingMachine machine;
        private final WashType washType;
        private boolean isCompleted;

        public WashCycle(String cycleId, Student student, WashingMachine machine, WashType washType) {
            if (cycleId == null || cycleId.trim().isEmpty()) {
                throw new IllegalArgumentException("Cycle ID cannot be empty.");
            }
            if (student == null) {
                throw new IllegalArgumentException("Student cannot be null.");
            }
            if (machine == null) {
                throw new IllegalArgumentException("Machine cannot be null.");
            }
            if (washType == null) {
                throw new IllegalArgumentException("Wash type cannot be null.");
            }

            this.cycleId = cycleId;
            this.student = student;
            this.machine = machine;
            this.washType = washType;
            this.isCompleted = false;

            this.machine.assignCycle(this);
        }

        public String getCycleId() {
            return cycleId;
        }

        public Student getStudent() {
            return student;
        }

        public WashingMachine getMachine() {
            return machine;
        }

        public WashType getWashType() {
            return washType;
        }

        public boolean isCompleted() {
            return isCompleted;
        }

        public void completeWash() {
            if (isCompleted) {
                throw new IllegalStateException("Cycle " + cycleId + " is already completed.");
            }
            this.machine.release();
            this.isCompleted = true;
            System.out.println("[CYCLE COMPLETED] " + washType.getTypeName() + " on " + machine.getMachineId() +
                    " finished for student " + student.getName() + ". Machine is now FREE.");
        }

        @Override
        public String toString() {
            return "Cycle #" + cycleId + " | " + student.getName() + " on " + machine.getMachineId() +
                    " | Type: " + washType.getTypeName() + " (" + washType.getDurationMinutes() + " mins, INR " +
                    washType.getCost() + ") | Status: " + (isCompleted ? "COMPLETED" : "RUNNING");
        }
    }

    // ==========================================
    // System State & Operations
    // ==========================================

    private final Map<String, WashingMachine> machines = new HashMap<>();
    private final List<WashCycle> activeCycles = new ArrayList<>();

    public void addMachine(WashingMachine machine) {
        machines.put(machine.getMachineId(), machine);
    }

    public WashCycle startWash(String cycleId, Student student, String machineId, WashType washType) {
        WashingMachine machine = machines.get(machineId);
        if (machine == null) {
            System.out.println("[ERROR] Machine " + machineId + " not found.");
            return null;
        }

        if (machine.isBusy()) {
            System.out.println("[BLOCKED] " + student.getName() + " cannot use Machine " + machineId +
                    " because it is currently BUSY with " + machine.getCurrentCycle().getStudent().getName() + "'s wash.");
            return null;
        }

        WashCycle cycle = new WashCycle(cycleId, student, machine, washType);
        activeCycles.add(cycle);
        System.out.println("[WASH STARTED] " + student.getName() + " started " + washType.getTypeName() +
                " on Machine " + machineId + ". Cost: INR " + washType.getCost());
        return cycle;
    }

    public static void main(String[] args) {
        System.out.println("=== Hostel Laundry Queue Demonstration ===");

        HostelLaundryQueue queueSystem = new HostelLaundryQueue();

        // 1. Setup Washing Machines
        WashingMachine wm1 = new WashingMachine("WM-01");
        WashingMachine wm2 = new WashingMachine("WM-02");
        queueSystem.addMachine(wm1);
        queueSystem.addMachine(wm2);

        System.out.println("\nInitial Machine States:");
        System.out.println(" - " + wm1);
        System.out.println(" - " + wm2);

        // 2. Students
        Student s1 = new Student("STU-101", "Kabir Sharma", "H3-204");
        Student s2 = new Student("STU-102", "Rhea Sengupta", "H3-305");
        Student s3 = new Student("STU-103", "Dev Anand", "H3-108");

        // 3. Wash Types
        WashType quick = new QuickWash();
        WashType normal = new NormalWash();
        WashType heavy = new HeavyWash();
        WashType sanitize = new SanitizeDelicateWash();

        // 4. Student 1 starts Quick Wash on WM-01
        System.out.println("\n--- Step 1: Kabir starts Quick Wash on WM-01 ---");
        WashCycle c1 = queueSystem.startWash("CYC-01", s1, "WM-01", quick);

        // 5. Student 2 tries to use WM-01 while it is busy (Must be blocked)
        System.out.println("\n--- Step 2: Rhea attempts to start Normal Wash on busy WM-01 ---");
        WashCycle c2Blocked = queueSystem.startWash("CYC-02", s2, "WM-01", normal);

        // 6. Student 2 uses WM-02 instead (Available)
        System.out.println("\n--- Step 3: Rhea uses WM-02 for Heavy Wash ---");
        WashCycle c2 = queueSystem.startWash("CYC-03", s2, "WM-02", heavy);

        System.out.println("\nMachine Status During Washes:");
        System.out.println(" - " + wm1);
        System.out.println(" - " + wm2);

        // 7. Complete Wash on WM-01 (Machine freed)
        System.out.println("\n--- Step 4: Completing Kabir's wash on WM-01 ---");
        c1.completeWash();

        System.out.println("\nMachine Status After WM-01 Completion:");
        System.out.println(" - " + wm1);

        // 8. Student 3 now starts newly introduced SanitizeDelicateWash on WM-01
        System.out.println("\n--- Step 5: Dev starts newly introduced SanitizeDelicateWash on WM-01 ---");
        WashCycle c3 = queueSystem.startWash("CYC-04", s3, "WM-01", sanitize);
        System.out.println("Active Cycle: " + c3);
    }
}
