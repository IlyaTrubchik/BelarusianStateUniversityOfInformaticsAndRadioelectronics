import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();
        LFSR a = new LFSR("111100001111000011110000111100001111000");
        a.cypherFile();
    }

}