package Tester;

import Lexer.Lexer;
import Lexer.Tokens.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.File;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

public class LexerTest {

    private Path testFolder;
    private Path solutionFolder;

    @BeforeEach
    public void setUp() {
        testFolder = Paths.get("tests/lexer/tests");
        solutionFolder = Paths.get("tests/lexer/solutions");
    }

    private void performLexerTest(File testFile, File solutionFile) {
        try {
            Lexer lexer = Lexer.newInstance(testFile);
            List<Token> tokens = lexer.getAllTokens();
            String actualResult = tokens.stream()
                    .map(Token::toString)
                    .collect(Collectors.joining()).trim();

            String expectedResult = new String(Files.readAllBytes(solutionFile.toPath())).trim();

            assertEquals(expectedResult, actualResult);

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
