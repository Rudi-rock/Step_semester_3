public class RemoveFirstKNodes {
    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
        }
    }

    static Node removeFirstKNodes(Node head, int k) {
        while (head != null && k > 0) {
            head = head.next;
            k--;
        }
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
        Node head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        head.next.next.next = new Node(4);
        head.next.next.next.next = new Node(5);
        print(removeFirstKNodes(head, 2));
        print(removeFirstKNodes(new Node(1), 5));
    }
}
