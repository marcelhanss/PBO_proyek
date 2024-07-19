package org.example.uas.beans;

public class Employees {
    private String nip;
    private String nama;
    private String email;
    private String departmentName;
    private String manager;
    private int deparmentID;
    private int managerID;
    public Employees() {

    }


    public Employees(String nip, String nama, String email) {
        this.nip = nip;
        this.nama = nama;
        this.email = email;
    }

    public Employees(String nip, String nama, String email, String departmentName) {
        this.nip = nip;
        this.nama = nama;
        this.email = email;
        this.departmentName = departmentName;
    }

    public Employees(String nip, String nama, String email, String departmentName, String manager) {
        this.nip = nip;
        this.nama = nama;
        this.email = email;
        this.departmentName = departmentName;
        this.manager = manager;
    }

    public int getDeparmentID() {
        return deparmentID;
    }

    public void setDeparmentID(int deparmentID) {
        this.deparmentID = deparmentID;
    }

    public int getManagerID() {
        return managerID;
    }

    public void setManagerID(int managerID) {
        this.managerID = managerID;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

}