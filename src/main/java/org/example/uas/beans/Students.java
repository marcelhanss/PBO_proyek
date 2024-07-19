package org.example.uas.beans;

public class Students {
    private String nrp;
    private String student_name;
    private  String email;
    private int departmentId;
    private int managerId;
    private String departmentName;
    private String managerName;

    public Students(String nrp, String student_name, String email, int departmentId,int managerId) {
        this.nrp = nrp;
        this.student_name = student_name;
        this.email = email;
        this.departmentId = departmentId;
        this.managerId = managerId;
    }
    public Students(){

    }

    public Students(String nrp, String student_name, String email, String departmentName, String managerName) {
        this.nrp = nrp;
        this.student_name = student_name;
        this.email = email;
        this.departmentName = departmentName;
        this.managerName = managerName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getNrp() {
        return nrp;
    }

    public void setNrp(String nrp) {
        this.nrp = nrp;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }
}
