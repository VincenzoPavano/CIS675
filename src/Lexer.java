import java.util.ArrayList;

public class Lexer {

    private String inputString;
    private int currentPos;
    private char currentChar;

    public Lexer(String inputString) {
        this.inputString = inputString;
        this.currentPos = 0;
        this.currentChar = this.inputString.charAt(this.currentPos);
    }

    /**
     * Read char, accept
     * 1. Take entire input file, separate (don't do this)
     * 2. CHANGE TO: Character by character - logic of lexer should recognize letter by letter, then separate at space
     * 2a. Identify all the words, combination of letter, throw away comments, etc
     * *Don't confuse with the parser
     */
    private void consume() {
        // Sole job of consume() is to advance to the next character
        currentPos++;
        if (currentPos < inputString.length()) {
            currentChar = inputString.charAt(currentPos);
        } else {
            //tokenType = TokenType.EOF;
        }
    }

    public Token nextToken() {
        Token t = null;
        ArrayList<Character> whitespace = new ArrayList<>();
        whitespace.add(' ');
        whitespace.add('\t');
        whitespace.add('\n');
        whitespace.add('\r');

        while (currentPos < inputString.length()) {
            if (whitespace.contains(currentChar)) {
                consume();
            } else if (currentChar == '{') {
                consume();
                t = new Token(TokenType.L_BRACE, "{");
                t.toString();
                return t;
            } else if (currentChar == '}') {
                consume();
                t = new Token(TokenType.R_BRACE, "}");
                t.toString();
                return t;
            } else if (currentChar == '[') {
                consume();
                t = new Token(TokenType.L_BRACKET, "[");
                t.toString();
                return t;
            } else if (currentChar == ']') {
                consume();
                t = new Token(TokenType.R_BRACKET, "]");
                t.toString();
                return t;
            } else if (currentChar == '=') {
                consume();
                t = new Token(TokenType.EQUAL, "=");
                t.toString();
                return t;
            } else if (currentChar == ';') {
                consume();
                t = new Token(TokenType.SEMICOLON, ";");
                t.toString();
                return t;
            } else if (currentChar == ':') {
                consume();
                t = new Token(TokenType.COLON, ":");
                t.toString();
                return t;
            } else if (currentChar == ',') {
                consume();
                t = new Token(TokenType.COMMA, ",");
                t.toString();
                return t;
            } else if (currentChar == '/') {
                // Could be the start of a comment
                // Loop through the following characters and check
                consume();
                if (currentChar == '*') {
                    // Ignore all text in the block until the final '*/'
                    while (currentChar != '/') {
                        consume();
                    }
                }
            } else if (currentChar == '-') {
                // Could possibly be a edgeop
                consume();
                if (currentChar == '>') {
                    consume();
                    t = new Token(TokenType.EDGEOP, "->");
                    t.toString();
                    return t;
                }
            } else if (currentChar == '\"') {
                // Property - ignore until the next quote and call "ID"
                consume();
                while (currentChar != '\"') {
                    consume();
                }
                consume();
            } else if (Character.isDigit(currentChar)) {
                consume();
                t = new Token(TokenType.NUMBER, "");
                t.toString();
                return t;
            } else if (Character.isLetter(currentChar)) {
                // Build the word and return
                StringBuilder builder = new StringBuilder();
                while (Character.isLetter(currentChar) || currentChar == '_') {
                    builder.append(currentChar);
                    consume();
                }
                String lexeme = builder.toString();

                if (lexeme.equals("digraph")) {
                    t = new Token(TokenType.DIGRAPH, "digraph");
                    t.toString();
                } else if (lexeme.equals("node")) {
                    t = new Token(TokenType.NODE, "node");
                    t.toString();
                } else {
                    t = new Token(TokenType.ID, lexeme);
                    t.toString();
                }
                return t;
            } else {
                consume();
                t = new Token(TokenType.ID, "");
                t.toString();
                return t;
            }
        }
        return null;
    }

    public char getCurrentChar() {
        return currentChar;
    }
}