package org.trad.pcl.Lexer;

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
    public int read(char[] cbuf, int off, int len) {
        try {
            return this.reader.read(cbuf, off, len);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            this.reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int peek(int n) {
        while (this.nextChars.size() < n) {
            int nextChar;
            try {
                nextChar = this.reader.read();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (nextChar == -1) {
                return -1;
            }
            this.nextChars.add(nextChar);
        }
        return this.nextChars.stream().skip(n - 1).findFirst().orElse(-1);
    }

    @Override
    public int read() {
        int nextChar;
        if (!this.nextChars.isEmpty()) {
            nextChar = this.nextChars.poll();
        } else {
            try {
                nextChar = this.reader.read();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        this.currentLine = nextChar == '\n' ? this.currentLine + 1 : this.currentLine;

        return nextChar;
    }

    public int getCurrentLine() {
        return this.currentLine;
    }
}