public class SplitSinglyLinkedList {
    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
        }
    }

    static Node splitListInHalf(Node head) {
        if (head == null || head.next == null) {
            return null;
        }
        Node slow = head;
        Node fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        Node secondHalf = slow.next;
        slow.next = null;
        return secondHalf;
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
        Node secondHalf = splitListInHalf(head);
        print(head);
        print(secondHalf);
        System.out.println("Time: O(n), Extra space: O(1)");
    }
}
