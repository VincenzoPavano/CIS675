public class Parser {

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
            onError(tokenType);
        }
    }

    public void parseGraph() {
        // [ strict ] (graph | digraph) [ ID ] '{' stmt_list '}'
        if (lookahead.getTokenType() == TokenType.STRICT) {
            match(TokenType.STRICT);
        }

        // Should be graph|digraph
        if (lookahead.getTokenType() == TokenType.GRAPH) {
            match(TokenType.GRAPH);
        } else if (lookahead.getTokenType() == TokenType.DIGRAPH) {
            match(TokenType.DIGRAPH);
        } else {
            onError(lookahead.getTokenType());
        }

        // Class name
        if (lookahead.getTokenType() == TokenType.ID) {
            match(TokenType.ID);
        }

        // {
        match(TokenType.L_BRACE);

        // Parse statements
        parseStmtList();

        // Close the class
        match(TokenType.R_BRACE);
    }

    private void parseStmtList() {
        parseStmt();

        if (lookahead.getTokenType() == TokenType.SEMICOLON) {
            match(TokenType.SEMICOLON);
        }

        //parseStmtList();
    }

    private void parseStmt() {
        parseNodeStmt();
        parseEdgeStmt();

        if (lookahead.getTokenType() == TokenType.ID) {
            match(TokenType.ID);

            parseIDEqualsID();
        }

        // Ignoring subgraph case
    }

    private void parseNodeStmt() {
        match(TokenType.ID);

        if (lookahead.getTokenType() == TokenType.L_BRACKET) {
            parseAttrList();
        }
    }

    private void parseEdgeStmt() {
        if (parseNodeId()) {
            parseEdgeRHS();
        }
    }

    private void parseEdgeRHS() {
        match(TokenType.EDGEOP);

        if (parseNodeId()) {
            // Recursively call function
        }
    }

    private boolean parseNodeId() {
        if (lookahead.getTokenType() == TokenType.ID) {
            match(TokenType.ID);
            return true;
        }

        return false;
    }

    private void parseAttrList() {
        match(TokenType.L_BRACKET);

        parseAList();

        match(TokenType.R_BRACKET);

        // Recursively call
//        parseAttrList();
    }

    private void parseAList() {
        parseIDEqualsID();

        if (lookahead.getTokenType() == TokenType.COMMA
                || lookahead.getTokenType() == TokenType.SEMICOLON) {
            match(lookahead.getTokenType());
        }

        // Recursively
//        parseAList();
    }

    private void parseIDEqualsID() {
        if (lookahead.getTokenType() == TokenType.EQUAL) {
            match(TokenType.ID);

            if (lookahead.getTokenType() == TokenType.ID) {
                match(TokenType.ID);
            }
        }
    }

    private void onError(TokenType tokenType) {
        System.out.println("Error in parsing token type: " + tokenType.name());
        System.exit(1);
    }
}
