package constructs.assigment_problems;

public class Participant {
    String name;
    String teamName;

    public Participant(String name, String teamName) {
        this.name = name;
        this.teamName = (teamName == null || teamName.trim().isEmpty()) ? "Unassigned" : teamName;
    }

    public Participant(String name) {
        this(name, "Unassigned");
    }

    public void printStatus() {
        System.out.println("Participant: " + name + " | Team: " + teamName);
    }

    public void display() {
        printStatus();
    }

    public static void main(String[] args) {
        String[] names = {"Alice", "Bob", "Charlie", "Diana"};
        String[] teamNames = {"Alpha", "", "Beta", ""};

        for (int i = 0; i < names.length; i++) {
            Participant participant;
            if (teamNames[i] == null || teamNames[i].isEmpty()) {
                participant = new Participant(names[i]);
            } else {
                participant = new Participant(names[i], teamNames[i]);
            }
            participant.printStatus();
        }
    }
}
