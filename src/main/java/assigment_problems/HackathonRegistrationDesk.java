public class HackathonRegistrationDesk {
    public static class Team {
        private final String name;
        private final int size;

        public Team(String name, int size) {
            this.name = name;
            this.size = size;
        }

        @Override
        public String toString() {
            return name + "(" + size + ")";
        }
    }

    public void sortTeams(Team[] teams) {
        for (int i = 1; i < teams.length; i++) {
            Team current = teams[i];
            int position = i - 1;
            while (position >= 0 && teams[position].size > current.size) {
                teams[position + 1] = teams[position];
                position--;
            }
            teams[position + 1] = current;
        }
    }

    private static void print(Team[] teams) {
        for (int i = 0; i < teams.length; i++) {
            System.out.print(teams[i] + (i == teams.length - 1 ? "" : ", "));
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Team[] teams = {
            new Team("Pixel", 4), new Team("Byte", 2), new Team("Nova", 4),
            new Team("Echo", 3), new Team("Atom", 2)
        };
        HackathonRegistrationDesk sorter = new HackathonRegistrationDesk();
        sorter.sortTeams(teams);
        print(teams);
        System.out.println("Best: O(n), Average: O(n^2), Worst: O(n^2), Extra space: O(1), Stable: Yes");
    }
}
