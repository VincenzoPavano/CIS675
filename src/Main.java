import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        // Import file as a String
        try {
            String inputFile = new String(Files.readAllBytes(Paths.get("homework2.gv")));
            Lexer lexer = new Lexer(inputFile);
            lexer.nextToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
