public class Parser {
    /**
     * An LL(1) parser defining the DOT language
     *
     * Grammars:
     * 1: ID '[' elements ']'
     * 2: ID '->' ID ';'
     *            '[' elements ']'
     *
     * elements: element (',' element)*
     * element: ID | list
     *
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
            System.out.println("Error in parsing token type");
        }
    }

    public void parseClass() {
        // Classes should always begin the same way:
        // DIGRAPH ID LBRACKET

        match(TokenType.DIGRAPH);
        match(TokenType.ID);
        match(TokenType.L_BRACE);
        match(TokenType.ID);

        // Parse the rest of the class
        while (lexer.getCurrentChar() != '}') {
            parseElement();
        }
    }

    private void parseElement() {
        if (lookahead.getTokenType() == TokenType.ID) {
            match(TokenType.ID);
        } else if (lookahead.getTokenType() == TokenType.L_BRACKET) {
            parseList();
        } else if (lookahead.getTokenType() == TokenType.EDGEOP) {
            parseEdgeop();
        } else if (lookahead.getTokenType() == TokenType.EQUAL) {
            consume();
        } else {
            System.out.println("Error in parsing element: " + lookahead.getTokenType());
        }
    }

    private void parseEdgeop() {
        // ID '[' elements ']' ';'
        // ID ';'
        match(TokenType.ID);

        if (lookahead.getTokenType() == TokenType.L_BRACKET) {
            parseElements();
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
        while (lookahead.getTokenType() == TokenType.COMMA) {
            match(TokenType.COMMA);
            parseElement();
        }
    }
}
