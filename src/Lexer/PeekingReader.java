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
        return this.reader.read(cbuf, off, len);
    }

    @Override
    public void close() throws IOException {
        this.reader.close();
    }

    public int peek(int n) throws IOException {
        while (this.nextChars.size() < n) {
            int nextChar = this.reader.read();
            if (nextChar == -1) {
                return -1;
            }
            this.nextChars.add(nextChar);
        }
        return this.nextChars.stream().skip(n - 1).findFirst().orElse(-1);
    }

    @Override
    public int read() throws IOException {
        int nextChar;
        if (!this.nextChars.isEmpty()) {
            nextChar =  this.nextChars.poll();
        } else {
            nextChar = this.reader.read();
        }

        this.currentLine = nextChar == '\n' ? this.currentLine + 1 : this.currentLine;

        return nextChar;
    }

    public int getCurrentLine() {
        return this.currentLine;
    }
}