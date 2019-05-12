package ru.skillbench.tasks.text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.*;

public class ContactCardImpl implements ContactCard {
    private String fullName;
    private String organization;
    private boolean isWoman;
    private Calendar birthday;
    private HashMap<String, String> phone = new HashMap<>();

    @Override
    public ContactCard getInstance(Scanner scanner) {
        ContactCardImpl contactCard = new ContactCardImpl();
        String line = scanner.nextLine();
        if(!line.equals("BEGIN:VCARD")) {
            throw new NoSuchElementException();
        }
        while(scanner.hasNextLine()) {
            line = scanner.nextLine();
            if(!line.contains(":")) {
                throw new InputMismatchException();
            }
            if(line.startsWith("FN")) {
                contactCard.fullName = line.substring(3);
            } else if(line.startsWith("ORG")) {
                contactCard.organization = line.substring(4);
            } else if(line.startsWith("GENDER")) {
                if(!(line.endsWith("F") || line.endsWith("M"))) {
                    throw new InputMismatchException();
                }
                contactCard.isWoman = line.endsWith("F");
            } else if(line.startsWith("TEL")) {
                String value = line.substring(line.indexOf(":") + 1);
                if (!value.matches("\\d{10}")) {
                    throw new InputMismatchException();
                }
                contactCard.phone.put(line.substring(line.indexOf("=") + 1, line.indexOf(":")), value);
            } else if(line.startsWith("BDAY")) {
                try {
                    contactCard.birthday = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("dd-MM-yyyy");
                    simpleDateFormat.setLenient(false);
                    contactCard.birthday.setTime(simpleDateFormat.parse(line.substring(5)));
                } catch (ParseException e) {
                    InputMismatchException exception = new InputMismatchException();
                    exception.initCause(e);
                    throw exception;
                }
            }
        }
        if(!line.equals("END:VCARD") || contactCard.fullName == null || contactCard.organization == null) {
            throw new NoSuchElementException();
        }
        System.out.println(contactCard.phone);
        return contactCard;
    }

    @Override
    public ContactCard getInstance(String data) {
        return getInstance(new Scanner(data));
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public String getOrganization() {
        return organization;
    }

    @Override
    public boolean isWoman() {
        return isWoman;
    }

    @Override
    public Calendar getBirthday() {
        if(birthday == null) {
            throw new NoSuchElementException();
        }
        return birthday;
    }

    @Override
    public Period getAge() {
        if(birthday == null) {
            throw new NoSuchElementException();
        }
        Calendar result = Calendar.getInstance();
        result.add(Calendar.DAY_OF_MONTH, -birthday.get(Calendar.DAY_OF_MONTH));
        result.add(Calendar.MONTH, -birthday.get(Calendar.MONTH));
        result.add(Calendar.YEAR, -birthday.get(Calendar.YEAR));
        return Period.of(result.get(Calendar.YEAR), result.get(Calendar.MONTH), result.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public int getAgeYears() {
        if(birthday == null) {
            throw new NoSuchElementException();
        }
        return getAge().getYears();
    }

    @Override
    public String getPhone(String type) {
        if(!phone.keySet().contains(type)) {
            throw new NoSuchElementException();
        }
        StringBuilder stringBuilder = new StringBuilder(phone.get(type));
        stringBuilder.insert(0, "(");
        stringBuilder.insert(4, ")");
        stringBuilder.insert(5, " ");
        stringBuilder.insert(9, "-");
        return stringBuilder.toString();
    }
}