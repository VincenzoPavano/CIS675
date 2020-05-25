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

    public void nextToken() {
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
                new Token(TokenType.L_BRACE).toString();
            } else if (currentChar == '}') {
                consume();
                new Token(TokenType.R_BRACE).toString();
            } else if (currentChar == '[') {
                consume();
                new Token(TokenType.L_BRACKET).toString();
            } else if (currentChar == ']') {
                consume();
                new Token(TokenType.R_BRACKET).toString();
            } else if (currentChar == '=') {
                consume();
                new Token(TokenType.EQUAL).toString();
            } else if (currentChar == ';') {
                consume();
                new Token(TokenType.SEMICOLON).toString();
            } else if (currentChar == ':') {
                consume();
                new Token(TokenType.COLON).toString();
            } else if (currentChar == ',') {
                consume();
                new Token(TokenType.COMMA).toString();
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
                    new Token(TokenType.EDGEOP).toString();
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
                new Token(TokenType.NUMBER).toString();
            } else if (Character.isLetter(currentChar)) {
                // Build the word and return
                StringBuilder builder = new StringBuilder();
                while (Character.isLetter(currentChar)) {
                    builder.append(currentChar);
                    consume();
                }
                String lexeme = builder.toString();

                if (lexeme.equals("digraph")) {
                    new Token(TokenType.DIGRAPH).toString();
                } else if (lexeme.equals("node")) {
                    new Token(TokenType.NODE).toString();
                } else {
                    new Token(TokenType.ID).toString();
                }
            } else {
                consume();
                new Token(TokenType.ID).toString();
            }
        }
    }
}