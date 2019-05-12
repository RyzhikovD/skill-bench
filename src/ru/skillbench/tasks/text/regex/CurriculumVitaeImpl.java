package ru.skillbench.tasks.text.regex;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurriculumVitaeImpl implements CurriculumVitae {
    private String text;
    private String firstName;
    private String middleName;
    private String lastName;
    private HashMap<String, String > hiddenPieces = new HashMap<>();

    @Override
    public void setText(String text) {
        this.text = text;
        firstName = null;
        hiddenPieces.clear();
    }

    @Override
    public String getText() {
        if(text == null) {
            throw new IllegalStateException();
        }
        return text;
    }

    @Override
    public List<Phone> getPhones() {
        List<Phone> phones = new LinkedList<>();
        Pattern phonePattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = phonePattern.matcher(getText());
        while (matcher.find()) {
            int areaCode = matcher.group(2) != null ? Integer.parseInt(matcher.group(2)) : -1;
            int extension = matcher.group(matcher.groupCount()) != null ? Integer.parseInt(matcher.group(matcher.groupCount())) : -1;
            phones.add(new Phone(matcher.group(), areaCode, extension));
        }
        return phones;
    }

    @Override
    public String getFullName() {
        Pattern fullNamePattern = Pattern.compile("([A-Z][a-z]*[a-z.])( ([A-Z][a-z]*[a-z.]))? ([A-Z][a-z]*[a-z.])");
        Matcher matcher = fullNamePattern.matcher(getText());
        if(matcher.find()) {
            firstName = matcher.group(1);
            lastName = matcher.group(4);
            middleName = matcher.group(3);
            return matcher.group();
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public String getFirstName() {
        if(firstName == null) {
            getFullName();
        }
        return firstName;
    }

    @Override
    public String getMiddleName() {
        if(firstName == null) {
            getFullName();
        }
        return middleName;
    }

    @Override
    public String getLastName() {
        if(firstName == null) {
            getFullName();
        }
        return lastName;
    }

    @Override
    public void updateLastName(String newLastName) {
        text = getText().replace(getLastName(), newLastName);
        lastName = newLastName;
    }

    @Override
    public void updatePhone(Phone oldPhone, Phone newPhone) {
        if(getText().contains(oldPhone.getNumber())) {
            text = getText().replace(oldPhone.getNumber(), newPhone.getNumber());
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void hide(String piece) {
        if(getText().contains(piece)) {
            String replacement = piece.replaceAll("[^ .@]", "X");
            text = getText().replace(piece, replacement);
            hiddenPieces.put(replacement, piece);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void hidePhone(String phone) {
        if(getText().contains(phone)) {
            String replacement = phone.replaceAll("[\\d]", "X");
            text = getText().replace(phone, replacement);
            hiddenPieces.put(replacement, phone);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public int unhideAll() {
        if(text == null) {
            throw new IllegalStateException();
        }
        int replaced = hiddenPieces.keySet().size();
        for(String key : hiddenPieces.keySet()) {
            text = getText().replace(key, hiddenPieces.get(key));
        }
        hiddenPieces.clear();
        return replaced;
    }
}