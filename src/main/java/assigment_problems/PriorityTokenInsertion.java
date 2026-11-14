import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PriorityTokenInsertion {
    public static final class Node {
        public int data;
        public Node next;

        Node(int data) {
            this.data = data;
        }
    }

    static final class TokenList {
        private Node head;
        private Node tail;
        private int size;

        public void addFirst(int token) {
            Node node = new Node(token);
            node.next = head;
            head = node;
            if (tail == null) {
                tail = node;
            }
            size++;
        }

        public void addLast(int token) {
            Node node = new Node(token);
            if (tail == null) {
                head = tail = node;
            } else {
                tail.next = node;
                tail = node;
            }
            size++;
        }

        public void insertAt(int index, int token) {
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException("Invalid insertion index: " + index);
            }
            if (index == 0) {
                addFirst(token);
                return;
            }
            if (index == size) {
                addLast(token);
                return;
            }
            Node previous = head;
            for (int i = 1; i < index; i++) {
                previous = previous.next;
            }
            Node node = new Node(token);
            node.next = previous.next;
            previous.next = node;
            size++;
        }

        void print() {
            Node current = head;
            while (current != null) {
                System.out.print(current.data + " -> ");
                current = current.next;
            }
            System.out.println("null");
        }
    }

    public static void main(String[] args) throws IOException {
        TokenList list = new TokenList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length == 0 || parts[0].isEmpty()) {
                continue;
            }
            switch (parts[0]) {
                case "addFirst" -> list.addFirst(Integer.parseInt(parts[1]));
                case "addLast" -> list.addLast(Integer.parseInt(parts[1]));
                case "insertAt" -> list.insertAt(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                default -> throw new IllegalArgumentException("Unknown operation: " + parts[0]);
            }
        }
        list.print();
    }
}
