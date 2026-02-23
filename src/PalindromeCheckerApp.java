public class PalindromeCheckerApp {

    public static void main(String[] args) {
        String input = "radar";
        char[] chars = input.toCharArray();
        boolean isPalindrome = true;
        int start = 0;
        int end = chars.length - 1;
        while (start < end) {
            if (chars[start] != chars[end]) {
                isPalindrome= false;
                break;
                System.out.println("Input:"+input);
                System.out.println("Is pallindrome? ::"+isPalindrome);
            }

        }
    }
}