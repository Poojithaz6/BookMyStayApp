import java.util.Scanner;

public class PalindromeCheckerApp {

    // Node class for singly linked list
    static class Node {
        char data;
        Node next;

        Node(char data) {
            this.data = data;
            this.next = null;
        }
    }

    // Helper method: reverse a linked list
    static Node reverseList(Node head) {
        Node prev = null;
        Node curr = head;
        Node next;

        while (curr != null) {
            next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }

        return prev;
    }

    // Helper method: convert string to linked list
    static Node stringToLinkedList(String s) {
        if (s == null || s.isEmpty()) return null;
        Node head = new Node(s.charAt(0));
        Node current = head;
        for (int i = 1; i < s.length(); i++) {
            current.next = new Node(s.charAt(i));
            current = current.next;
        }
        return head;
    }

    // Helper method: check palindrome using fast & slow pointers
    static boolean isPalindrome(Node head) {
        if (head == null || head.next == null) return true;

        // Find middle using fast and slow pointers
        Node slow = head;
        Node fast = head;

        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // Reverse second half
        Node secondHalf = reverseList(slow.next);

        // Compare first half and reversed second half
        Node p1 = head;
        Node p2 = secondHalf;
        boolean result = true;

        while (p2 != null) {
            if (p1.data != p2.data) {
                result = false;
                break;
            }
            p1 = p1.next;
            p2 = p2.next;
        }

        // Restore the original list (optional)
        slow.next = reverseList(secondHalf);

        return result;
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        //System.out.print("Enter a string to check palindrome using linked list: ");
        String input = "level";

        Node head = stringToLinkedList(input);
        boolean isPalin = isPalindrome(head);

        System.out.println("Input: " + input);
        System.out.println("Is palindrome?: " + isPalin);

        scanner.close();
    }
}