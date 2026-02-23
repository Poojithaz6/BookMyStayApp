import java.util.Stack;

public class PalindromeCheckerApp {

    public static void main(String[] args) {
        //boolean isPalindrome = true;
        String input = "noon";
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < input.length(); i++) {
            stack.push(input.charAt(i));
        }
        String reversed = "";
        while (!stack.isEmpty()) {
            reversed += stack.pop();
        }
        boolean isPalindrome = input.equals(reversed);
        System.out.println("Input: " + input);
        System.out.println("Reversed using Stack: " + reversed);
        System.out.println("Is palindrome?: " + isPalindrome);
    }
}