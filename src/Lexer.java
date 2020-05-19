public class Lexer {

    private String inputString;

    public Lexer(String inputString) {
        this.inputString = inputString;
    }

    public void consume() {
        // Break up word by space and determine token
        String[] tokens = inputString.split("\\s+");
        for (String token : tokens) {
            TokenType currentType = nextToken(token);
            Token currentToken = new Token(currentType);
            currentToken.toString();
        }
    }

    private TokenType nextToken(String token) {
        if (token.equals("digraph")) {
            return TokenType.DIGRAPH;
        } else if (token.equals("{")) {
            return TokenType.L_BRACE;
        } else if (token.equals("}")) {
            return TokenType.R_BRACE;
        } else if (token.equals("[")) {
            return TokenType.L_BRACKET;
        } else if (token.equals("]")) {
            return TokenType.R_BRACKET;
        } else if (token.equals("->")) {
            return TokenType.EDGEOP;
        } else if (token.equals("/*")) {
            return TokenType.L_BLOCK_COMMENT;
        } else if (token.equals("*/")) {
            return TokenType.R_BLOCK_COMMENT;
        } else if (token.matches("\\[[a-z]+\\=[a-z]+")) {
            // List found
        } else {
            return TokenType.ID;
        }
    }
}
