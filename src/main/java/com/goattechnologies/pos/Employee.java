package com.goattechnologies.pos;

public class Employee {
    private String employeeName;

    private static class EmployeeHolder {
        private static final Employee INSTANCE = new Employee();
    }

    public static Employee getInstance() {
        return EmployeeHolder.INSTANCE;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}
