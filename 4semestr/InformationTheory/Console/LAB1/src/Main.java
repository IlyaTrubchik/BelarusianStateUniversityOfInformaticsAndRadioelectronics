import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.println("Enter option:");
        System.out.println("1.Шифр Децимации");
        System.out.println("2.Шифр Вижинера");
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        switch (n) {
            case 1->{
                System.out.println("Enter option:");
                System.out.println("1.Cypher text");
                System.out.println("2.Decypher text");
                n= scanner.nextInt();
            switch (n) {
                case 1 -> {
                    System.out.println("Enter text");
                    scanner.nextLine();
                    String plaintext = scanner.nextLine();
                    System.out.println("Enter key");
                    int key = scanner.nextInt();
                    DeciCypher a = new DeciCypher("", plaintext, key);
                    System.out.println(a.getCypher());
                }
                case 2 -> {
                    System.out.println("Enter text");
                    scanner.nextLine();
                    String cyphertext = scanner.nextLine();
                    System.out.println("Enter key");
                    int key = scanner.nextInt();
                    DeciCypher a = new DeciCypher(cyphertext, "", key);
                    System.out.println(a.decypher());
                    }
                }
            }
            case 2->{
                System.out.println("Enter option:");
                System.out.println("1.Cypher text");
                System.out.println("2.Decypher text");
                n= scanner.nextInt();
                switch (n) {
                    case 1 -> {
                        System.out.println("Enter text");
                        scanner.nextLine();
                        String plaintext = scanner.nextLine();
                        System.out.println("Enter key:");
                        String key = scanner.nextLine();
                        VizhenCyphr a = new VizhenCyphr(plaintext,"" , key);
                        System.out.println(a.getCypher());
                    }
                    case 2 -> {
                        System.out.println("Enter text");
                        scanner.nextLine();
                        String cyphertext = scanner.nextLine();
                        System.out.println("Enter key");
                        String key = scanner.nextLine();
                        VizhenCyphr a = new VizhenCyphr("", cyphertext, key);
                        System.out.println(a.getDecypher());
                    }
                }
            }
        }
    }


}