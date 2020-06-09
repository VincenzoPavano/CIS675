import org.json.JSONObject;

public class Parser {

    private final Lexer lexer;
    private final JSONObject jsonGlobal;
    private final JSONObject json;
    private Token lookahead;
    private String lastKey;
    private String className;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.lookahead = lexer.nextToken();
        this.jsonGlobal = new JSONObject();
        this.json = new JSONObject();
        this.lastKey = "";
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
            className = lookahead.getText();
            match(TokenType.ID);
        }

        // {
        match(TokenType.L_BRACE);

        // Parse statements
        while (lookahead.getTokenType() != TokenType.R_BRACE) {
            parseStmtList();
        }

        // Close the class
        match(TokenType.R_BRACE);

        jsonGlobal.put(className, json);
        System.out.println(jsonGlobal.toString());
    }

    private void parseStmtList() {
        // [ stmt [ ';' ] stmt_list ]
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
            // Next layer
            parseIDEqualsID();
        }

        // Ignoring subgraph case
    }

    private void parseNodeStmt() {
        // Build first level
        lastKey = lookahead.getText();
        JSONObject firstLevel = json.put(lastKey, new JSONObject());
        JSONObject secondLevel = firstLevel.getJSONObject(lastKey);

        // Create second-level only if they don't exist
//        if (!secondLevel.has("attributes")) {
            // Tie first and second layers together
            secondLevel.put("attributes", new JSONObject());
            secondLevel.put("neighbors", new JSONObject());
//        }


        match(TokenType.ID);

        if (lookahead.getTokenType() == TokenType.L_BRACKET) {
            parseAttrList();
        }
    }

    private void parseEdgeStmt() {
        if (!parseNodeId()) {
            parseEdgeRHS();

            if (parseNodeId() && lookahead.getTokenType() != TokenType.SEMICOLON) {
                parseAttrList();
            }
        }
    }

    private void parseEdgeRHS() {
        if (lookahead.getTokenType() == TokenType.EDGEOP) {
            match(TokenType.EDGEOP);

            // "Neighbors"
//            JSONObject rhsObj = new JSONObject();
            JSONObject firstLayer = (JSONObject) json.get(lastKey);
            JSONObject neighbors = (JSONObject) firstLayer.get("neighbors");

            neighbors.put(lookahead.getText(), new JSONObject());
            // Update the key
            //lastKey = lookahead.getText();

//            rhsObj.put(lookahead.getText(), null);
//            firstLayer.putIfAbsent(lastKey, rhsObj);
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
            //match(lookahead.getTokenType());
            parseAList();
        }
    }

    private void parseIDEqualsID() {
        if (lookahead.getTokenType() == TokenType.ID) {
            String key = lookahead.getText();
            match(TokenType.ID);

            if (lookahead.getTokenType() == TokenType.EQUAL) {
                match(TokenType.EQUAL);

                if (lookahead.getTokenType() == TokenType.ID) {
                    String value = lookahead.getText();
                    match(TokenType.ID);

                    // Update the existing JSON Object with the new one w/ children
                    JSONObject currentObj = (JSONObject) json.get(lastKey);
                    JSONObject lastObj = new JSONObject();

                    lastObj.put(key, value);
                    currentObj.put("attributes", lastObj);
                }
            }
        }
    }

    private void onError(TokenType tokenType) {
        System.out.println("Error in parsing token type: " + tokenType.name());
        System.exit(1);
    }
}
