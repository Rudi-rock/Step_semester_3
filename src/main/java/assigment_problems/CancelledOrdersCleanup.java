import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CancelledOrdersCleanup {
    public static final class Node {
        public int data;
        public Node next;

        Node(int data) {
            this.data = data;
        }
    }

    public static Node removeAll(Node head, int code) {
        while (head != null && head.data == code) {
            head = head.next;
        }
        Node current = head;
        while (current != null && current.next != null) {
            if (current.next.data == code) {
                current.next = current.next.next;
            } else {
                current = current.next;
            }
        }
        return head;
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
        String values = reader.readLine();
        String code = reader.readLine();
        if (values != null && code != null) {
            printList(removeAll(parseList(values), Integer.parseInt(code.trim())));
        }
    }
}
