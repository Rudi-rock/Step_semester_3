public class DeleteNthNode {
    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
        }
    }

    static Node deleteNthNode(Node head, int n) {
        if (n == 1) {
            return head.next;
        }
        Node previous = head;
        for (int position = 1; position < n - 1; position++) {
            previous = previous.next;
        }
        previous.next = previous.next.next;
        return head;
    }

    private static void print(Node head) {
        while (head != null) {
            System.out.print(head.data + (head.next == null ? "" : " -> "));
            head = head.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Node head = new Node(10);
        head.next = new Node(20);
        head.next.next = new Node(30);
        head.next.next.next = new Node(40);
        print(deleteNthNode(head, 3));
        print(deleteNthNode(new Node(10), 1));
    }
}
