package will.tesler.vowels;
import java.io.Console;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RegexTest {

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        while (true) {

        	System.out.print("\nEnter your regex: ");

            Pattern pattern =
            Pattern.compile(scanner.nextLine());

            System.out.print("\nEnter input string to search: ");

            Matcher matcher = pattern.matcher(scanner.nextLine());

            boolean found = false;
            while (matcher.find()) {
                System.out.format("I found the text" +
                    " \"%s\" starting at " +
                    "index %d and ending at index %d.%n",
                    matcher.group(),
                    matcher.start(),
                    matcher.end());
                found = true;
            }
            if(!found){
                System.out.format("No match found.\n");
            }
        }
    }
}
