package Lexer;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.Queue;

public class PeekingReader extends Reader {
    private final Reader reader;
    private final Queue<Integer> nextChars = new LinkedList<>();

    private int currentLine;

    public PeekingReader(Reader reader) {
        this.reader = reader;
        this.currentLine = 1;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        return reader.read(cbuf, off, len);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    public int peek(int n) throws IOException {
        while (nextChars.size() < n) {
            int nextChar = reader.read();
            if (nextChar == -1) {
                return -1;
            }
            nextChars.add(nextChar);
        }
        return nextChars.stream().skip(n - 1).findFirst().orElse(-1);
    }

    @Override
    public int read() throws IOException {
        int nextChar;
        if (!nextChars.isEmpty()) {
            nextChar =  nextChars.poll();
        } else {
            nextChar = reader.read();
        }

        currentLine = nextChar == '\n' ? currentLine + 1 : currentLine;

        return nextChar;
    }

    public int getCurrentLine() {
        return currentLine;
    }
}