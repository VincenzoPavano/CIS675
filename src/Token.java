public class Token {

    private TokenType tokenType;

    public Token(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public String toString() {
        String tokenString = tokenType.name();
        System.out.print(tokenString + " ");
        return tokenString;
    }
}
