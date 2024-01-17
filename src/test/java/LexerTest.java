//

import com.diogonunes.jcolor.AnsiFormat;
import com.diogonunes.jcolor.Attribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.provider.MethodSource;
import org.trad.pcl.Constants;
import org.trad.pcl.Exceptions.BadFileExtension;
import org.trad.pcl.Helpers.FileHelper;
import org.trad.pcl.Lexer.Lexer;
import org.trad.pcl.Lexer.Tokens.Token;
import org.trad.pcl.Main;
import org.trad.pcl.Parser.Parser;
import org.trad.pcl.Services.ErrorService;
import org.trad.pcl.ast.ProgramNode;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.diogonunes.jcolor.Ansi.colorize;
import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {

    private Path testFolder;
    private Path solutionFolder;


    @BeforeEach
    public void setUp() {
        testFolder = Paths.get("src", "main", "resources", "tests", "lexer", "tests");
        solutionFolder = Paths.get("src", "main", "resources", "tests", "lexer", "solutions");
    }

    private void performLexerTest(File testFile, File solutionFile) {
        try {
            ErrorService.resetInstance();

            Lexer lexer = new Lexer(testFile);
            List<Token> tokens = lexer.getAllTokens();
            String actualResult = tokens.stream()
                    .map(Token::printWithoutColor)
                    .collect(Collectors.joining()).trim();

            String actualErrors = ErrorService.getInstance().getLexicalErrors().stream()
                    .map(Exception::getMessage)
                    .collect(Collectors.joining("\n"));

            String[] solution = Files.readString(solutionFile.toPath()).split("\n\n");
            String expectedResult = solution[0].trim();
            String expectedErrors;
            if (solution.length > 1) {
                expectedErrors = solution[1].trim();
            } else {
                expectedErrors = "";
            }

            assertEquals(expectedResult, actualResult);
            assertEquals(expectedErrors, actualErrors);

        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException occurred: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }


    @Test
    public void testLexer() throws IOException {
        List<Executable> tests = Files.list(testFolder)
                .filter(file -> file.toString().endsWith(".canAda"))
                .map(testFile -> (Executable) () -> {
                    File test = testFile.toFile();
                    System.out.println("Testing " + test.getName());
                    File solution = solutionFolder.resolve(test.getName().replace(".canAda", "_solution.txt")).toFile();
                    performLexerTest(test, solution);
                })
                .collect(Collectors.toList());

        assertAll("Lexer Tests", tests);
    }

}
