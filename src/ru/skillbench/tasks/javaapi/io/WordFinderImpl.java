package ru.skillbench.tasks.javaapi.io;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordFinderImpl implements WordFinder {
    private String text;
    private Set<String> wordSet;

    private void checkArgument(Object argument) {
        if(argument == null) {
            throw new IllegalArgumentException();
        }
    }

    private void checkState(Object field) {
        if(field == null) {
            throw new IllegalStateException();
        }
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        checkArgument(text);
        this.text = text;
    }

    @Override
    public void setInputStream(InputStream is) throws IOException {
        checkArgument(is);
        byte[] buf = new byte[is.available()];
        if(is.read(buf) != -1) {
            text = new String(buf);
        }
    }

    @Override
    public void setFilePath(String filePath) throws IOException {
        checkArgument(filePath);
        setInputStream(new FileInputStream(filePath));
    }

    @Override
    public void setResource(String resourceName) throws IOException {
        checkArgument(resourceName);
        setInputStream(getClass().getResourceAsStream(resourceName));
    }

    @Override
    public Stream<String> findWordsStartWith(String begin) {
        checkState(getText());
        String notNullBegin = begin == null ? "" : begin;
        wordSet = new HashSet<>(Arrays.asList(getText().split("\\s+")))
                .parallelStream()
                .filter(word -> word.regionMatches(true, 0, notNullBegin, 0, notNullBegin.length()))
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        return wordSet.parallelStream();
    }

    @Override
    public void writeWords(OutputStream os) throws IOException {
        checkState(wordSet);
        Optional<String> stringOptional = wordSet.parallelStream()
                .sorted()
                .reduce((s1, s2) -> s1 + " " + s2);
        if(stringOptional.isPresent()) {
            os.write(stringOptional.get().getBytes());
        }
    }
}