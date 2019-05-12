package ru.skillbench.tasks.basics.entity;

public class EmployeeImpl implements Employee {
    private int salary;
    private String firstName;
    private String lastName;
    private Employee manager;

    public EmployeeImpl() {
        salary = 1000;
    }

    @Override
    public int getSalary() {
        return salary;
    }

    @Override
    public void increaseSalary(int value) {
        salary+=value;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public void setManager(Employee manager) {
        this.manager = manager;
    }

    @Override
    public String getManagerName() {
        return manager != null ? manager.getFullName() : "No manager";
    }

    @Override
    public Employee getTopManager() {
        return manager != null ? manager.getTopManager() : this;
    }
}