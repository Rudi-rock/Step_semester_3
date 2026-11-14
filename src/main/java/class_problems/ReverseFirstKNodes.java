public class ReverseFirstKNodes {
    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
        }
    }

    static Node reverseFirstKNodes(Node head, int k) {
        Node previous = null;
        Node current = head;
        Node originalHead = head;
        for (int i = 0; i < k; i++) {
            Node next = current.next;
            current.next = previous;
            previous = current;
            current = next;
        }
        originalHead.next = current;
        return previous;
    }

    private static void print(Node head) {
        while (head != null) {
            System.out.print(head.data + (head.next == null ? "" : " -> "));
            head = head.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Node head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        head.next.next.next = new Node(4);
        head.next.next.next.next = new Node(5);
        print(reverseFirstKNodes(head, 3));
    }
}
