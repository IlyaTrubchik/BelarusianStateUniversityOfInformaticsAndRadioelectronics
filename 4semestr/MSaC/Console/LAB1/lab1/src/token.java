public class token {
    private final String text;

    private final ITokenType tokenType;
    private final int start;

    public token(String text, ITokenType tokenType, int start) {
        this.text = text;
        this.start = start;
        this.tokenType = tokenType;
    }

    public String getText() {
        return text;
    }

    public ITokenType getType() {
        return tokenType;
    }

    public int getStart() {
        return start;
    }
}
