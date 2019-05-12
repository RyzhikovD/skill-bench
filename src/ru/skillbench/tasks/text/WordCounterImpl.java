package ru.skillbench.tasks.text;

import java.io.PrintStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordCounterImpl implements WordCounter {
    private String text;

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Map<String, Long> getWordCounts() {
        if(getText() == null) {
            throw new IllegalStateException();
        }
        HashMap<String, Long> frequencyMap = new HashMap<>();
        String processedText = getText().replaceAll("<\\s*\\S*\\s*>|[,.?:;!\"()—]", "").toLowerCase();
        Pattern wordPattern = Pattern.compile("\\S+");
        Matcher matcher = wordPattern.matcher(processedText);
        while (matcher.find()) {
            Long frequency = frequencyMap.get(matcher.group());
            frequencyMap.put(matcher.group(), frequency == null ? 1 : frequency + 1);
        }
        return frequencyMap;
    }

    @Override
    public List<Map.Entry<String, Long>> getWordCountsSorted() {
        return sort(getWordCounts(), new Comparator<>() {
            @Override
            public int compare(Map.Entry<String, Long> entry1, Map.Entry<String, Long> entry2) {
                int frequencyDifference = entry2.getValue().compareTo(entry1.getValue());
                return frequencyDifference != 0 ? frequencyDifference : entry1.getKey().compareTo(entry2.getKey());
            }
        });
    }

    @Override
    public <K extends Comparable<K>, V extends Comparable<V>> List<Map.Entry<K, V>> sort(Map<K, V> map, Comparator<Map.Entry<K, V>> comparator) {
        ArrayList<Map.Entry<K, V>> mapAsList = new ArrayList<>(map.entrySet());
        mapAsList.sort(comparator);
        return mapAsList;
    }

    @Override
    public <K, V> void print(List<Map.Entry<K, V>> entries, PrintStream ps) {
        for(Map.Entry<K, V> entry : entries) {
            ps.println(entry.getKey() + " " + entry.getValue());
        }
    }
}