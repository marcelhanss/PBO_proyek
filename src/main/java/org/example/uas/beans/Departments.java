package org.example.uas.beans;

public class Departments {
    private int departmentId;
    private String name;
    private int managerId;
    private String manager;
    private String nameEmployee;

    private int count;

    public Departments(int departmentId, String name, int managerId) {
        this.departmentId = departmentId;
        this.name = name;
        this.managerId = managerId;
    }

    public Departments() {

    }

    public Departments(int departmentId, String name, String manager) {
        this.departmentId = departmentId;
        this.name = name;
        this.manager = manager;
    }

    public String getNameEmployee() {
        return nameEmployee;
    }

    public void setNameEmployee(String nameEmployee) {
        this.nameEmployee = nameEmployee;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
