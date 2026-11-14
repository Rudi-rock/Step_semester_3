public class CheckIfEmpty {
    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
        }
    }

    static boolean checkIfEmpty(Node head) {
        return head == null;
    }

    public static void main(String[] args) {
        System.out.println(checkIfEmpty(null));
        System.out.println(checkIfEmpty(new Node(8)));
    }
}
