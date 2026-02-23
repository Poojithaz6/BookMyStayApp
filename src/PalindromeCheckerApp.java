import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class PalindromeCheckerApp {

    public static void main(String[] args) {

        // Take input from user
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a string to check palindrome using Deque: ");
        String input = scanner.nextLine();

        // Create a Deque to store characters
        Deque<Character> deque = new ArrayDeque<>();

        // Insert all characters into the deque
        for (int i = 0; i < input.length(); i++) {
            deque.addLast(input.charAt(i)); // Insert at rear
        }

        boolean isPalindrome = true;

        // Compare front and rear until deque is empty or mismatch occurs
        while (deque.size() > 1) {
            char front = deque.removeFirst(); // Remove from front
            char rear = deque.removeLast();   // Remove from rear

            if (front != rear) {
                isPalindrome = false;
                break; // Stop checking if mismatch found
            }
        }

        // Print result
        System.out.println("Input: " + input);
        System.out.println("Is palindrome?: " + isPalindrome);

        scanner.close();
    }
}