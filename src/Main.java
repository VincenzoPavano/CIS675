import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        // Import file as a String
        try {
            String inputFile = new String(Files.readAllBytes(Paths.get("homework2.gv")));
            System.out.println(inputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
