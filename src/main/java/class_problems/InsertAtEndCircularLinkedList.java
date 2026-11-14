public class InsertAtEndCircularLinkedList {
    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
        }
    }

    static Node insertAtEndCircular(Node head, int value) {
        Node node = new Node(value);
        if (head == null) {
            node.next = node;
            return node;
        }
        Node tail = head;
        while (tail.next != head) {
            tail = tail.next;
        }
        tail.next = node;
        node.next = head;
        return head;
    }

    private static void print(Node head) {
        if (head == null) {
            System.out.println();
            return;
        }
        Node current = head;
        do {
            System.out.print(current.data);
            current = current.next;
            if (current != head) {
                System.out.print(" -> ");
            }
        } while (current != head);
        System.out.println(" -> back to " + head.data);
    }

    public static void main(String[] args) {
        Node head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        head.next.next.next = head;
        print(insertAtEndCircular(head, 4));
        print(insertAtEndCircular(null, 9));
    }
}
