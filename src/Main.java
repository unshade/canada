import java.io.File;
import java.io.IOException;
import Lexer.Lexer;
import Lexer.Tokens.*;

public class Main {
    public static void main(String[] args) {

        File file = new File("src/simpleTest.canAda");

        try {
            Lexer lexer = new Lexer(file);

            Token token;
            while ((token = lexer.nextToken()).tag() != Tag.EOF) {
                System.out.println(token);
            }
            System.out.println(token);
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
