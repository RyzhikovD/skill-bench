package ru.skillbench.tasks.basics.entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocationImpl implements Location{
    private String name;
    private Type type;
    private Location parent;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public void setParent(Location parent) {
        this.parent = parent;
    }

    @Override
    public String getParentName() {
        return parent != null ? parent.getName() : "--";
    }

    @Override
    public Location getTopLocation() {
        return parent != null ? parent.getTopLocation() : this;
    }

    @Override
    public boolean isCorrect() {
        if(parent != null) {
            return type.compareTo(parent.getType()) > 0 && parent.isCorrect();
        }
        return true;
    }

    @Override
    public String getAddress() {
        Pattern pattern = Pattern.compile(("^\\S+\\.\\S*\\s\\S*"));
        Matcher matcher = pattern.matcher(this.getName());
        if(parent != null){
            return (matcher.find()||this.getName().endsWith(".") ?
                    this.getName() : this.getType().getNameForAddress() + this.getName()) + ", " + parent.getAddress();
        } else {
            return (matcher.find()||this.getName().endsWith(".") ?
                    this.getName() : this.getType().getNameForAddress() + this.getName());
        }
    }

    public String toString() {
        return name + " (" +type.toString() + ")";
    }
}