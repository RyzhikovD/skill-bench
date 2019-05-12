package ru.skillbench.tasks.text.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternsImpl implements Patterns {
    @Override
    public Pattern getSQLIdentifierPattern() {
        return Pattern.compile("[a-zA-Z]\\w{0,29}");
    }

    @Override
    public Pattern getEmailPattern() {
        return Pattern.compile("(?i)[a-z\\d][\\w.\\-]{0,20}[a-z\\d]" +
                "@([a-z\\d][a-z\\d\\-]*[a-z\\d]\\.)+(?-i)" +
                "(ru|com|net|org)");
    }

    @Override
    public Pattern getHrefTagPattern() {
        return Pattern.compile("<(?i)(\\s*a\\s*href)(?-i)\\s*=\\s*(\".*?\"|\\S+?)\\s*/?>");
    }

    @Override
    public List<String> findAll(String input, Pattern pattern) {
        Matcher matcher = pattern.matcher(input);
        List<String> matchingList = new ArrayList<>();
        while(matcher.find()) {
            matchingList.add(matcher.group());
        }
        return matchingList;
    }

    @Override
    public int countMatches(String input, String regex) {
        Pattern pattern = Pattern.compile("(?i)".concat(regex));
        Matcher matcher = pattern.matcher(input);
        int count = 0;
        while(matcher.find()) {
            count++;
        }
        return count;
    }
}