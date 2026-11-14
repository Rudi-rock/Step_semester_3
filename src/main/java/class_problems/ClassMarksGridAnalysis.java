import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ClassMarksGridAnalysis {
    private static final class Student {
        private final String name;
        private final int[] marks;

        Student(String name, int[] marks) {
            this.name = name;
            this.marks = marks;
        }

        int total() {
            return marks[0] + marks[1] + marks[2];
        }
    }

    public static void main(String[] args) throws IOException {
        List<Student> students = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] parts = line.split("\\s+", 2);
            String[] markParts = parts[1].replace("[", "").replace("]", "").split(",");
            int[] marks = new int[3];
            for (int i = 0; i < 3; i++) {
                marks[i] = Integer.parseInt(markParts[i].trim());
            }
            students.add(new Student(parts[0], marks));
        }
        if (students.isEmpty()) {
            return;
        }

        StringBuilder totals = new StringBuilder("Totals ");
        int[] subjectTotals = new int[3];
        Student topper = students.get(0);
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            totals.append(student.name).append(' ').append(student.total());
            if (i < students.size() - 1) {
                totals.append(", ");
            }
            for (int subject = 0; subject < 3; subject++) {
                subjectTotals[subject] += student.marks[subject];
            }
            if (student.total() > topper.total()) {
                topper = student;
            }
        }
        System.out.println(totals);
        System.out.printf(Locale.ROOT, "averages %.2f, %.2f, %.2f%n",
                subjectTotals[0] / (double) students.size(),
                subjectTotals[1] / (double) students.size(),
                subjectTotals[2] / (double) students.size());
        System.out.println("topper " + topper.name + " (" + topper.total() + ")");
    }
}
