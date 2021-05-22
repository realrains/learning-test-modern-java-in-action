package net.realrain.chap3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Lambda {
    public static String processFile(String filename) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            return bufferedReader.readLine();
        }
    }

    @FunctionalInterface
    public static interface BufferedReaderProcessor {
        String process(BufferedReader bufferedReader) throws IOException;
    }

    public static String parameterizedProcessFile(BufferedReaderProcessor processor, String filename) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            return processor.process(bufferedReader);
        }
    }
}
