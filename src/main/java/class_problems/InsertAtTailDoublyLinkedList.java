public class InsertAtTailDoublyLinkedList {
    static class DNode {
        int data;
        DNode prev;
        DNode next;

        DNode(int data) {
            this.data = data;
        }
    }

    static DNode insertAtTailDoubly(DNode head, int value) {
        DNode node = new DNode(value);
        if (head == null) {
            return node;
        }
        DNode current = head;
        while (current.next != null) {
            current = current.next;
        }
        current.next = node;
        node.prev = current;
        return head;
    }

    private static void print(DNode head) {
        while (head != null) {
            System.out.print(head.data);
            if (head.next != null) {
                System.out.print(" <-> ");
            }
            head = head.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        DNode head = new DNode(1);
        head = insertAtTailDoubly(head, 2);
        head = insertAtTailDoubly(head, 3);
        head = insertAtTailDoubly(head, 4);
        print(head);
        print(insertAtTailDoubly(null, 7));
    }
}
