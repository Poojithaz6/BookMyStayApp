import java.util.Scanner;

public class PalindromeCheckerApp {
    static boolean isPalindromeRecursive(String str, int start, int end) {
        if (start >= end) {
            return true;
        }
        if (str.charAt(start) != str.charAt(end)) {
            return false;
        }
        return isPalindromeRecursive(str, start + 1, end - 1);
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String input = "madam";

        boolean result = isPalindromeRecursive(input, 0, input.length() - 1);

        System.out.println("Input: " + input);
        System.out.println("Is palindrome?: " + result);

        scanner.close();
    }
}