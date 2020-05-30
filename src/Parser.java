public class Parser {
    /**
     * An LL(1) parser defining the DOT language
     * <p>
     * Grammars:
     * 1: ID '[' elements ']'
     * 2: ID '->' ID ';'
     * '[' elements ']'
     * <p>
     * elements: element (',' element)*
     * element: ID | list
     * <p>
     * ';' at EOL (alist)
     * have lexer keep track of number of new lines/characters for error reporting
     */
    private Lexer lexer;
    private Token lookahead;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.lookahead = lexer.nextToken();
    }

    private void consume() {
        lookahead = lexer.nextToken();
    }

    private void match(TokenType tokenType) {
        if (lookahead.getTokenType() == tokenType) {
            consume();
        } else {
            System.out.println("Error in parsing token type: " + tokenType.name());
        }
    }

    /**
     * NOTE: In an ideal world, the compiler won't need to manually "match" each case
     */
    public void parseClass() {

        match(TokenType.DIGRAPH);
        match(TokenType.ID);
        match(TokenType.L_BRACE);

        match(TokenType.ID);
        parseList();
        match(TokenType.SEMICOLON);

        parseEdgeop();
    }

    private void parseElement() {
        if (lookahead.getTokenType() == TokenType.ID) {
            match(TokenType.ID);
        } else if (lookahead.getTokenType() == TokenType.NUMBER) {
            match(TokenType.NUMBER);
        } else if (lookahead.getTokenType() == TokenType.L_BRACKET) {
            parseList();
        } else if (lookahead.getTokenType() == TokenType.EDGEOP) {
            parseEdgeop();
        } else if (lookahead.getTokenType() == TokenType.EQUAL
                || lookahead.getTokenType() == TokenType.R_BRACKET) {
            consume();
        } else {
            System.out.println("Error in parsing element: " + lookahead.getTokenType());
        }
    }

    private void parseEdgeop() {
        // ID '[' elements ']' ';'
        // ID ';'
        match(TokenType.ID);
        match(TokenType.EDGEOP);
        match(TokenType.ID);

        if (lookahead.getTokenType() == TokenType.L_BRACKET) {
            parseElements();
            match(TokenType.SEMICOLON);
        } else if (lookahead.getTokenType() == TokenType.SEMICOLON) {
            consume();
        } else {
            System.out.println("Error in parsing element: " + lookahead.getTokenType());
        }
    }

    private void parseList() {
        match(TokenType.L_BRACKET);
        parseElements();
        match(TokenType.R_BRACKET);
    }

    private void parseElements() {
        parseElement();
        while (lookahead.getTokenType() == TokenType.EQUAL) {
            match(TokenType.EQUAL);
            parseElement();
        }
    }
}
