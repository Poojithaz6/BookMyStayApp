import java.util.Scanner;

public class PalindromeCheckerApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter a string to check if it's a palindrome: ");
        String word = scanner.nextLine();
        boolean isPalindrome = true;
        int len = word.length();
        for (int i = 0; i < len / 2; i++) {
            if (word.charAt(i) != word.charAt(len - 1 - i)) {
                isPalindrome = false;
                break;
            }
        }
            System.out.println("Input text:" + word);
            System.out.println("Is it a palindrome? :"+isPalindrome);
        scanner.close();
    }
}