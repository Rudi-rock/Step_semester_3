package constructs.class_problems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OnlineExaminationSystem {

    // ==========================================
    // Student & Answer
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

    public static class Answer {
        private final int questionId;
        private final String responseText;

        public Answer(int questionId, String responseText) {
            this.questionId = questionId;
            this.responseText = responseText != null ? responseText.trim() : "";
        }

        public int getQuestionId() {
            return questionId;
        }

        public String getResponseText() {
            return responseText;
        }

        @Override
        public String toString() {
            return "Answer[Q" + questionId + ": '" + responseText + "']";
        }
    }

    // ==========================================
    // Question Hierarchy (Polymorphic Evaluation)
    // ==========================================

    public static abstract class Question {
        private final int questionId;
        private final String questionText;
        private final double maxMarks;

        public Question(int questionId, String questionText, double maxMarks) {
            if (questionText == null || questionText.trim().isEmpty()) {
                throw new IllegalArgumentException("Question text cannot be empty.");
            }
            if (maxMarks <= 0) {
                throw new IllegalArgumentException("Marks must be positive.");
            }
            this.questionId = questionId;
            this.questionText = questionText;
            this.maxMarks = maxMarks;
        }

        public int getQuestionId() {
            return questionId;
        }

        public String getQuestionText() {
            return questionText;
        }

        public double getMaxMarks() {
            return maxMarks;
        }

        public abstract String getQuestionType();

        // Polymorphic evaluation based on concrete question type
        public abstract double evaluate(Answer answer);

        @Override
        public String toString() {
            return "Q" + questionId + " [" + getQuestionType() + " | " + maxMarks + " marks]: " + questionText;
        }
    }

    public static class MCQQuestion extends Question {
        private final List<String> options;
        private final int correctOptionIndex; // 0-based

        public MCQQuestion(int questionId, String questionText, double maxMarks, List<String> options, int correctOptionIndex) {
            super(questionId, questionText, maxMarks);
            if (options == null || options.size() < 2) {
                throw new IllegalArgumentException("MCQ must have at least two options.");
            }
            if (correctOptionIndex < 0 || correctOptionIndex >= options.size()) {
                throw new IllegalArgumentException("Correct option index is out of bounds.");
            }
            this.options = new ArrayList<>(options);
            this.correctOptionIndex = correctOptionIndex;
        }

        public List<String> getOptions() {
            return Collections.unmodifiableList(options);
        }

        @Override
        public String getQuestionType() {
            return "MCQ";
        }

        @Override
        public double evaluate(Answer answer) {
            if (answer == null || answer.getResponseText().isEmpty()) {
                return 0.0;
            }
            try {
                int selectedIndex = Integer.parseInt(answer.getResponseText());
                return (selectedIndex == correctOptionIndex) ? getMaxMarks() : 0.0;
            } catch (NumberFormatException e) {
                String selected = answer.getResponseText();
                String correct = options.get(correctOptionIndex);
                return (selected.equalsIgnoreCase(correct)) ? getMaxMarks() : 0.0;
            }
        }
    }

    public static class TrueFalseQuestion extends Question {
        private final boolean correctAnswer;

        public TrueFalseQuestion(int questionId, String questionText, double maxMarks, boolean correctAnswer) {
            super(questionId, questionText, maxMarks);
            this.correctAnswer = correctAnswer;
        }

        @Override
        public String getQuestionType() {
            return "TrueFalse";
        }

        @Override
        public double evaluate(Answer answer) {
            if (answer == null || answer.getResponseText().isEmpty()) {
                return 0.0;
            }
            String resp = answer.getResponseText().trim().toLowerCase();
            boolean parsed = resp.equals("true") || resp.equals("t") || resp.equals("yes");
            return (parsed == correctAnswer) ? getMaxMarks() : 0.0;
        }
    }

    public static class ShortAnswerQuestion extends Question {
        private final String expectedKeyword;

        public ShortAnswerQuestion(int questionId, String questionText, double maxMarks, String expectedKeyword) {
            super(questionId, questionText, maxMarks);
            if (expectedKeyword == null || expectedKeyword.trim().isEmpty()) {
                throw new IllegalArgumentException("Expected keyword cannot be empty.");
            }
            this.expectedKeyword = expectedKeyword.trim().toLowerCase();
        }

        @Override
        public String getQuestionType() {
            return "ShortAnswer";
        }

        @Override
        public double evaluate(Answer answer) {
            if (answer == null || answer.getResponseText().isEmpty()) {
                return 0.0;
            }
            String studentText = answer.getResponseText().trim().toLowerCase();
            return studentText.contains(expectedKeyword) ? getMaxMarks() : 0.0;
        }
    }

    // ==========================================
    // Attempt States & Evaluation
    // ==========================================

    public enum AttemptStatus {
        InProgress,
        Submitted
    }

    public static class Attempt {
        private final String attemptId;
        private final Student student;
        private final Examination examination;
        private AttemptStatus status;
        private final Map<Integer, Answer> answers = new HashMap<>();
        private final Map<Integer, Double> questionScores = new HashMap<>();
        private double totalScore = 0.0;

        public Attempt(String attemptId, Student student, Examination examination) {
            this.attemptId = attemptId;
            this.student = student;
            this.examination = examination;
            this.status = AttemptStatus.InProgress;
        }

        public String getAttemptId() {
            return attemptId;
        }

        public Student getStudent() {
            return student;
        }

        public Examination getExamination() {
            return examination;
        }

        public AttemptStatus getStatus() {
            return status;
        }

        public double getTotalScore() {
            return totalScore;
        }

        public Map<Integer, Double> getQuestionScores() {
            return Collections.unmodifiableMap(questionScores);
        }

        public void recordAnswer(int questionId, String responseText) {
            if (status == AttemptStatus.Submitted) {
                throw new IllegalStateException("Attempt is already Submitted! Answers cannot be changed.");
            }
            answers.put(questionId, new Answer(questionId, responseText));
            System.out.println("[SAVED] Answer for Q" + questionId + " recorded as: '" + responseText + "'");
        }

        public void submit() {
            if (status == AttemptStatus.Submitted) {
                throw new IllegalStateException("Attempt has already been submitted!");
            }

            totalScore = 0.0;
            for (Question q : examination.getQuestions()) {
                Answer ans = answers.get(q.getQuestionId());
                double score = q.evaluate(ans);
                questionScores.put(q.getQuestionId(), score);
                totalScore += score;
            }

            this.status = AttemptStatus.Submitted;
            examination.markAttemptSubmitted(student);
            System.out.println("[SUBMITTED] Attempt " + attemptId + " by " + student.getName() +
                    " successfully submitted. Total Score: " + totalScore + "/" + examination.getTotalMarks());
        }

        public void displayResult() {
            System.out.println("\n--- Result Summary for Attempt: " + attemptId + " (" + student.getName() + ") ---");
            System.out.println("Status: " + status);
            for (Question q : examination.getQuestions()) {
                Answer ans = answers.get(q.getQuestionId());
                double score = questionScores.getOrDefault(q.getQuestionId(), 0.0);
                System.out.println(" Q" + q.getQuestionId() + " [" + q.getQuestionType() + "] Student Answer: " +
                        (ans != null ? ans.getResponseText() : "(No Answer)") +
                        " -> Score: " + score + "/" + q.getMaxMarks());
            }
            System.out.println("FINAL SCORE: " + totalScore + " / " + examination.getTotalMarks());
        }
    }

    // ==========================================
    // Examination System
    // ==========================================

    public static class Examination {
        private final String examId;
        private final String title;
        private final List<Question> questions = new ArrayList<>();
        private final Set<String> submittedStudentIds = new HashSet<>();

        public Examination(String examId, String title) {
            this.examId = examId;
            this.title = title;
        }

        public String getExamId() {
            return examId;
        }

        public String getTitle() {
            return title;
        }

        public void addQuestion(Question question) {
            questions.add(question);
        }

        public List<Question> getQuestions() {
            return Collections.unmodifiableList(questions);
        }

        public double getTotalMarks() {
            double total = 0.0;
            for (Question q : questions) {
                total += q.getMaxMarks();
            }
            return total;
        }

        public Attempt startAttempt(String attemptId, Student student) {
            if (submittedStudentIds.contains(student.getStudentId())) {
                System.out.println("[BLOCKED] Student " + student.getName() +
                        " has already submitted an attempt for examination: " + title);
                return null;
            }
            return new Attempt(attemptId, student, this);
        }

        public void markAttemptSubmitted(Student student) {
            submittedStudentIds.add(student.getStudentId());
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Online Examination System Demonstration ===");

        // 1. Create Examination and Questions
        Examination exam = new Examination("EX-101", "Java OOP & Design Principles");

        List<String> mcqOptions = List.of("Polymorphism", "Compilation", "Pointers", "Assembly");
        exam.addQuestion(new MCQQuestion(1, "Which of the following is a core pillar of OOP?", 5.0, mcqOptions, 0));
        exam.addQuestion(new TrueFalseQuestion(2, "Interfaces in Java can have abstract methods.", 3.0, true));
        exam.addQuestion(new ShortAnswerQuestion(3, "Which keyword is used to inherit a class in Java?", 4.0, "extends"));

        System.out.println("\nCreated Exam: " + exam.getTitle() + " (Total Marks: " + exam.getTotalMarks() + ")");
        for (Question q : exam.getQuestions()) {
            System.out.println(" - " + q);
        }

        // 2. Student 1 starts attempt
        Student s1 = new Student("STU-01", "Rohan Gupta");
        System.out.println("\n--- Step 1: " + s1.getName() + " starts exam attempt ---");
        Attempt attempt1 = exam.startAttempt("ATT-001", s1);

        // 3. Record and modify answers before submission
        System.out.println("\n--- Step 2: Answering questions (Allowed before submission) ---");
        attempt1.recordAnswer(1, "2"); // wrong answer initially
        attempt1.recordAnswer(2, "true");

        // Changing answer for Q1 before submission
        System.out.println("\n--- Modifying Answer for Q1 before submission ---");
        attempt1.recordAnswer(1, "0"); // revised to correct answer
        attempt1.recordAnswer(3, "extends");

        // 4. Submit Attempt 1
        System.out.println("\n--- Step 3: Submitting Attempt 1 ---");
        attempt1.submit();
        attempt1.displayResult();

        // 5. Attempting to change answer after submission (Forbidden)
        System.out.println("\n--- Step 4: Attempting to modify answer after submission ---");
        try {
            attempt1.recordAnswer(1, "1");
        } catch (IllegalStateException e) {
            System.out.println("[BLOCKED] Caught expected exception: " + e.getMessage());
        }

        // 6. Attempting a second attempt for the same student on the same exam (Forbidden)
        System.out.println("\n--- Step 5: Attempting second attempt for " + s1.getName() + " ---");
        Attempt attempt2 = exam.startAttempt("ATT-002", s1);
        System.out.println("Second attempt creation result: " + (attempt2 == null ? "Blocked" : "Allowed"));

        // 7. Another student takes the exam
        Student s2 = new Student("STU-02", "Neha Patel");
        System.out.println("\n--- Step 6: " + s2.getName() + " takes the exam ---");
        Attempt attempt3 = exam.startAttempt("ATT-003", s2);
        attempt3.recordAnswer(1, "0");
        attempt3.recordAnswer(2, "false");
        attempt3.recordAnswer(3, "inherits");
        attempt3.submit();
        attempt3.displayResult();
    }
}
