import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Tokenizer implements Enumeration<Boolean> {

    private final String code;

    private final ITokenType[] tokenTypes;

    private final Matcher matcher;

    private int currentPosition = 0;

    public Tokenizer(String code, ITokenType[] TokenTypes) {
        this.code = code;
        this.tokenTypes = TokenTypes;

        List<String> regexList = new ArrayList<>();
        for (int i = 0; i < tokenTypes.length; i++) {
            ITokenType tokenType = tokenTypes[i];
            regexList.add("(?<g" + i + ">" + tokenType.getRegex() + ")");
        }
        String regex = regexList.stream().collect(Collectors.joining("|"));
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        // Запускаем первый поиск
        matcher = pattern.matcher(code);
        matcher.find();
    }


    @Override
    public boolean hasMoreElements() {
        return currentPosition < code.length();
    }

    @Override
    public token nextElement() {
        boolean found = currentPosition > matcher.start() ? matcher.find() : true;

        int start = found ? matcher.start() : code.length();
        int end = found ? matcher.end() : code.length();

        if(found && currentPosition == start) {
            currentPosition = end;
            // Лексема найдена- определяем тип
            for (int i = 0; i < tokenTypes.length; i++) {
                String si = "g" + i;
                if (matcher.start(si) == start && matcher.end(si) == end) {
                    return createToken(code, tokenTypes[i], start, end);
                }
            }
        }
        throw new IllegalStateException("Не удается определить лексему в позиции " + currentPosition);
    }

    protected token createToken(String content, ITokenType tokenType, int start, int end) {
        return new token(content.substring(start, end), tokenType, start);
    }

    /**
     * Список типов токенов для SQL
     */
    public enum SQLTokenType implements ITokenType {
        KEYWORD("\\b(?:select|from|where|group|by|order|or|and|not|exists|having|join|left|right|inner)\\b"),
        ID("[A-Za-z][A-Za-z0-9]*"),
        REAL_NUMBER("[0-9]+\\.[0-9]*"),
        NUMBER("[0-9]+"),
        STRING("'[^']*'"),
        SPACE("\\s+"),
        COMMENT("\\-\\-[^\\n\\r]*"),
        OPERATION("[+\\-\\*/.=\\(\\)]");

        private final String regex;

        SQLTokenType(String regex) {
            this.regex = regex;
        }

        @Override
        public String getRegex() {
            return regex;
        }

    }
}

