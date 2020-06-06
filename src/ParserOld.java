public class ParserOld {
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
    private int lineNumber;

    public ParserOld(Lexer lexer) {
        this.lexer = lexer;
        this.lookahead = lexer.nextToken();
        this.lineNumber = 1;
    }

    private void consume() {
        lookahead = lexer.nextToken();
    }

    private void match(TokenType tokenType) {
        if (lookahead.getTokenType() == tokenType) {
            consume();
        } else {
            System.out.println("Error in parsing token type: " + tokenType.name());
            System.out.println("Line Number: " + lineNumber);
            System.exit(1);
        }
    }

    /**
     * NOTE: In an ideal world, the compiler won't need to manually "match" each case
     */
    public void parseClass() {

        match(TokenType.DIGRAPH);
        match(TokenType.ID);
        match(TokenType.L_BRACE);
        lineNumber++;

        match(TokenType.ID);
        parseList();
        match(TokenType.SEMICOLON);
        lineNumber++;

        parseEdgeop();
        lineNumber++;
        parseEdgeop();
        lineNumber++;
        parseEdgeop();
        lineNumber++;
        parseEdgeop();
        lineNumber++;
        parseEdgeop();
        lineNumber++;
        parseEdgeop();
        lineNumber++;
        parseEdgeop();
        lineNumber++;

        System.out.println("No syntax errors found!");
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
        } else if (lookahead.getTokenType() == TokenType.EQUAL) {
            consume();
        } else if (lookahead.getTokenType() == TokenType.R_BRACKET) {
            // TODO: This is a bug, but for now, just ignore R_BRACKET
        } else {
            System.out.println("Error in parsing element: " + lookahead.getTokenType());
            System.out.println("Line Number: " + lineNumber);
            System.exit(1);
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
            System.out.println("Line Number: " + lineNumber);
            System.exit(1);
        }
    }

    private void parseList() {
        match(TokenType.L_BRACKET);
        parseElements();

        while (lookahead.getTokenType() == TokenType.COMMA) {
            // Call parseElements again until the list is finished
            match(TokenType.COMMA);
            parseElements();
        }
        match(TokenType.R_BRACKET);
    }

    private void parseElements() {
        parseElement();
        while (lookahead.getTokenType() == TokenType.EQUAL) {
            match(TokenType.EQUAL);
            match(TokenType.ID);
            //parseElement();
        }
    }
}
