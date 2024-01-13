package org.trad.pcl.Tester;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.function.Executable;
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.io.IOException;
//import java.io.File;
//import java.nio.file.*;
//import java.util.List;
//import java.util.stream.Collectors;

public class LexerTest {

//    private Path testFolder;
//    private Path solutionFolder;
//
//    @BeforeEach
//    public void setUp() {
//        testFolder = Paths.get("tests/lexer/tests");
//        solutionFolder = Paths.get("tests/lexer/solutions");
//    }
//
//    private void performLexerTest(File testFile, File solutionFile) {
//        try {
//            ErrorService.resetInstance();
//
//            Lexer lexer = Lexer.newInstance(testFile);
//            List<Token> tokens = lexer.getAllTokens();
//            String actualResult = tokens.stream()
//                    .map(Token::toString)
//                    .collect(Collectors.joining()).trim();
//
//            String actualErrors = ErrorService.getInstance().getLexicalErrors().stream()
//                    .map(Exception::getMessage)
//                    .collect(Collectors.joining("\n"));
//
//            String[] solution = Files.readString(solutionFile.toPath()).split("\n\n");
//            String expectedResult = solution[0].trim();
//            String expectedErrors;
//            if (solution.length > 1) {
//                expectedErrors = solution[1].trim();
//            } else {
//                expectedErrors = "";
//            }
//
//            assertEquals(expectedResult, actualResult);
//            assertEquals(expectedErrors, actualErrors);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            fail("IOException occurred: " + e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//            fail("Unexpected exception occurred: " + e.getMessage());
//        }
//    }
//
//
//    @Test
//    public void testLexer() throws IOException {
//        List<Executable> tests = Files.list(testFolder)
//                .filter(file -> file.toString().endsWith(".canAda"))
//                .map(testFile -> (Executable) () -> {
//                    File test = testFile.toFile();
//                    System.out.println("Testing " + test.getName());
//                    File solution = solutionFolder.resolve(test.getName().replace(".canAda", "_solution.txt")).toFile();
//                    performLexerTest(test, solution);
//                })
//                .collect(Collectors.toList());
//
//        assertAll("Lexer Tests", tests);
//    }

}
