import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReverseTheQueue {
    public static final class Node {
        public int data;
        public Node next;

        Node(int data) {
            this.data = data;
        }
    }

    public static Node reverse(Node head) {
        Node previous = null;
        Node current = head;
        while (current != null) {
            Node next = current.next;
            current.next = previous;
            previous = current;
            current = next;
        }
        return previous;
    }

    private static Node parseList(String line) {
        Node head = null;
        Node tail = null;
        String cleaned = line.replace("->", " ").replace(",", " ").trim();
        if (cleaned.isEmpty() || cleaned.equals("null")) {
            return null;
        }
        for (String value : cleaned.split("\\s+")) {
            if (value.equals("null")) {
                break;
            }
            Node node = new Node(Integer.parseInt(value));
            if (head == null) {
                head = tail = node;
            } else {
                tail.next = node;
                tail = node;
            }
        }
        return head;
    }

    private static void printList(Node head) {
        while (head != null) {
            System.out.print(head.data + " -> ");
            head = head.next;
        }
        System.out.println("null");
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        if (line != null) {
            printList(reverse(parseList(line)));
        }
    }
}
