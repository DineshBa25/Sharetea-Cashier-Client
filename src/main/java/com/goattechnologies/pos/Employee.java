package com.goattechnologies.pos;

/**
 * The Employee class represents an employee in the Point of Sale (POS) system.
 * It stores information about the employee's name and whether they are a manager.
 */
public class Employee {
    private String employeeName;

    private boolean isManager;

    /**
     * The EmployeeHolder class is a private static inner class used to implement the Singleton pattern
     * for the Employee class. It ensures that only one instance of the Employee class is created.
     */
    private static class EmployeeHolder {
        private static final Employee INSTANCE = new Employee();
    }

    /**
     * Get the singleton instance of the Employee class.
     *
     * @return The singleton instance of the Employee.
     */
    public static Employee getInstance() {
        return EmployeeHolder.INSTANCE;
    }

    /**
     * Get the name of the employee.
     *
     * @return The employee's name.
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * Set the name of the employee.
     *
     * @param employeeName The name of the employee.
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    /**
     * Set the manager status of the employee.
     *
     * @param manager A boolean indicating whether the employee is a manager.
     */
    public void setManager(boolean manager) {
        isManager = manager;
    }

    /**
     * Check if the employee is a manager.
     *
     * @return true if the employee is a manager, false otherwise.
     */
    public boolean isManager() {
        return isManager;
    }
}
