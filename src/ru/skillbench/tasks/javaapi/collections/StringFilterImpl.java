package ru.skillbench.tasks.javaapi.collections;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class StringFilterImpl implements StringFilter {
    private HashSet<String> stringSet = new HashSet<>();

    private interface Filter {
        boolean itemIsCorrespond(String item);
    }

    private Iterator<String> filterToIterator(String pattern, Filter filter) {
        if(pattern == null || pattern.equals("")) {
            return getCollection().iterator();
        }
        HashSet<String> selection = new HashSet<>();
        for(String item : getCollection()) {
            if(item != null && filter.itemIsCorrespond(item)) {
                selection.add(item);
            }
        }
        return selection.iterator();
    }

    @Override
    public void add(String s) {
        if(s != null) {
            s = s.toLowerCase();
        }
        getCollection().add(s);
    }

    @Override
    public boolean remove(String s) {
        s = s == null ? null : s.toLowerCase();
        return getCollection().remove(s);
    }

    @Override
    public void removeAll() {
        getCollection().clear();
    }

    @Override
    public Collection<String> getCollection() {
        return stringSet;
    }

    @Override
    public Iterator<String> getStringsContaining(String chars) {
        return filterToIterator(chars, new Filter() {
            @Override
            public boolean itemIsCorrespond(String item) {
                return item.contains(chars);
            }
        });
    }

    @Override
    public Iterator<String> getStringsStartingWith(String begin) {
        return filterToIterator(begin, new Filter() {
            @Override
            public boolean itemIsCorrespond(String item) {
                return item.startsWith(begin.toLowerCase());
            }
        });
    }

    @Override
    public Iterator<String> getStringsByNumberFormat(String format) {
        return filterToIterator(format, new Filter() {
            @Override
            public boolean itemIsCorrespond(String item) {
                byte[] itemBytes = item.getBytes();
                for(int i = 0; i < itemBytes.length; i++) {
                    if(itemBytes[i] >= '0' && itemBytes[i] <= '9') {
                        itemBytes[i] = '#';
                    }
                }
                return (new String(itemBytes)).equals(format);
            }
        });
    }

    @Override
    public Iterator<String> getStringsByPattern(String pattern) {
        return filterToIterator(pattern, new Filter() {
            @Override
            public boolean itemIsCorrespond(String item) {
                if(item.length() < pattern.replace("*", "").length()) {
                    return false;
                }
                int firstIndex = pattern.indexOf("*");
                int patternLastIndex = pattern.lastIndexOf("*");
                int itemLastIndex = item.length() + patternLastIndex - pattern.length();

                boolean middlePartAreEqual;
                if(firstIndex == patternLastIndex) {
                    if(firstIndex == -1) {              //there is no "*" in pattern
                        return item.equals(pattern);
                    } else {                            //there is no middle part
                        middlePartAreEqual = true;
                    }
                } else {
                    middlePartAreEqual = item.substring(firstIndex, itemLastIndex + 1).contains(pattern.substring(firstIndex + 1, patternLastIndex));
                }
                boolean firstPartsAreEqual = item.substring(0, firstIndex).equals(pattern.substring(0, firstIndex));
                boolean lastPartsAreEqual = item.substring(itemLastIndex + 1).equals(pattern.substring(patternLastIndex + 1));
                return firstPartsAreEqual && middlePartAreEqual && lastPartsAreEqual;
            }
        });
    }
}