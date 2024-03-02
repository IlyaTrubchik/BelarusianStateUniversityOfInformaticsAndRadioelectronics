import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<String> tokens = new ArrayList<>();
        tokens.add("ID");
        tokens.add("ASSIGN");
        tokens.add("OPENBR");
        tokens.add("NUM");
        tokens.add("BITWISE");
        tokens.add("ID");
        tokens.add("ARITHM_S");
       // tokens.add("ID"); //ДОБАВИЛ ПОСЛЕ ОДНООПЕРНДНОГО ЕЩЕ ПЕРЕМЕННУЮ
        tokens.add("CLOSEBR");
        tokens.add("ARITHM");
        tokens.add("OPENBR");
        tokens.add("NUM");
        tokens.add("BITWISE");
        tokens.add("ID");
        tokens.add("CLOSEBR");


     //  tokens.add("ID");
        Parser parser = new Parser(tokens.toArray(new String[0]));

        System.out.println(parser.parse());

    }
}