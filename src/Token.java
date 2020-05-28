public class Token {

    private TokenType tokenType;
    private String text;

    public Token(TokenType tokenType, String text) {
        this.tokenType = tokenType;
        this.text = text;
    }

    @Override
    public String toString() {
        String tokenString = tokenType.name();
        System.out.print(tokenString + " ");
        return tokenString;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getText() {
        return text;
    }
}
