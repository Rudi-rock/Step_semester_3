public class DeleteLastNodeDoublyLinkedList {
    static class DNode {
        int data;
        DNode prev;
        DNode next;

        DNode(int data) {
            this.data = data;
        }
    }

    static DNode deleteLastNodeDoubly(DNode head) {
        if (head == null || head.next == null) {
            return null;
        }
        DNode tail = head;
        while (tail.next != null) {
            tail = tail.next;
        }
        tail.prev.next = null;
        return head;
    }

    private static void print(DNode head) {
        while (head != null) {
            System.out.print(head.data + (head.next == null ? "" : " <-> "));
            head = head.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        DNode head = new DNode(1);
        DNode second = new DNode(2);
        DNode third = new DNode(3);
        head.next = second;
        second.prev = head;
        second.next = third;
        third.prev = second;
        print(deleteLastNodeDoubly(head));
        print(deleteLastNodeDoubly(new DNode(5)));
    }
}
