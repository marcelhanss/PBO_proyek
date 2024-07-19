package org.example.uas.dao;

import org.example.uas.Database.JdbcConnection;
import org.example.uas.beans.Employees;
import org.example.uas.beans.Students;

import java.sql.*;
import java.util.ArrayList;

public class StudentDao implements CRUD_DAO<Students> {
    @Override
    public ArrayList<Students> getAll() throws SQLException {
        String query = "select e.nrp, e.nama, e.email, d.department_name, m.nama as manager from students e\n" +
                "left join employees m on m.employee_id = e.manager_id\n" +
                "join departments d on e.department_id = d.department_id where e.status_aktif=1";
        Statement statement = db.createStatement();
        ResultSet result = statement.executeQuery(query);
        ArrayList<Students> listStudents = new ArrayList<>();
        while (result.next()) {
            String id = result.getString("nrp");
            String student_name = result.getString("nama");
            String email = result.getString("email");
            String departmentId = result.getString("department_name");
            String managerId = result.getString("manager");


            listStudents.add(new Students(id, student_name,email,departmentId,managerId));
        }

        return listStudents;
    }

    public static Students get(String nrp) {
        PreparedStatement statement = null;
        ResultSet result = null;
        String query = "select e.nrp, e.nama, e.email, d.department_name, m.nama as manager from students e\n" +
                "left join employees m on m.employee_id = e.manager_id\n" +
                "join departments d on e.department_id = d.department_id\n" +
                "where e.status_aktif = 1 and e.nrp = ? order by e.student_id asc";
        Students students = null;
        try {
            statement = db.prepareStatement(query);
            statement.setString(1, nrp);
            result = statement.executeQuery();

            while(result.next()) {
                students = new Students();
                students.setNrp(result.getString("nrp"));
                students.setStudent_name(result.getString("nama"));
                students.setEmail(result.getString("email"));
                students.setDepartmentName(result.getString("department_name"));
                students.setManagerName(result.getString("manager"));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcConnection.closeConnection(result, statement);
        }
        return students;

    }

    public ArrayList<String> getAllDepartments() throws SQLException{
        String query = "select distinct department_name from employees e join departments d on e.department_id = d.department_id";
        Statement statement = db.createStatement();
        ResultSet result = statement.executeQuery(query);
        ArrayList<String> listDepartments = new ArrayList<>();
        while (result.next()) {
            String department = result.getString("department_name");
            listDepartments.add(department);
        }

        return listDepartments;
    }

    @Override
    public void save(Students students) throws SQLException {
        PreparedStatement statement = null;
        String query = "insert into students(nrp, nama, email, department_id, manager_id) values (?, ?, ?, ?, ?)";
        try {
            statement = db.prepareStatement(query);
            statement.setString(1, students.getNrp());
            statement.setString(2, students.getStudent_name());
            statement.setString(3, students.getEmail());
            statement.setInt(4, students.getDepartmentId());
            int manager_id = findCorrespondingManager(students.getDepartmentId());
            statement.setInt(5, manager_id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcConnection.closeConnection(statement);
        }
    }
    public int findRightDepartmentId(String department_name) throws SQLException {
        String query = "select distinct e.department_id from departments d join employees e on d.department_id = e.department_id where department_name = '" + department_name + "'";
        Statement statement = db.createStatement();
        ResultSet result = statement.executeQuery(query);
        int department = 0;
        while(result.next()) {
            department = result.getInt("department_id");
        }

        return department;
    }
    public int findCorrespondingManager(int department_id) throws SQLException {
        String query = "select m.employee_id FROM employees m " +
                "join departments d ON m.department_id = d.department_id " +
                "where d.department_id = ? limit 1";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, department_id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt("employee_id");
            }
        }
        return 0;
    }
    @Override
    public boolean update(Students students) throws SQLException {
        return false;
    }

    @Override
    public void delete(String nrp) throws SQLException {
        String query = "update students set status_aktif = 0 where nrp = ?";
        try(PreparedStatement preparedStatement = db.prepareStatement(query)) {
            preparedStatement.setString(1, nrp);
            preparedStatement.executeUpdate();
        }
    }

    public ArrayList<Students> historyStudent() throws SQLException {
        String query = "select e.nrp, e.nama, d.department_name from students e\n" +
                "join departments d using(department_id)\n" +
                "where e.status_aktif = 0";
        Statement statement = db.createStatement();
        ResultSet result = statement.executeQuery(query);
        ArrayList<Students> listStudent = new ArrayList<>();
        while (result.next()) {
            Students student = new Students();
            student.setNrp(result.getString("nrp"));
            student.setStudent_name(result.getString("nama"));
            student.setDepartmentName(result.getString("department_name"));
            listStudent.add(student);

        }

        return listStudent;
    }

    public void update(Students students, String nrp) throws SQLException {
        PreparedStatement statement = null;
        String query = "update students \n" +
                "set nrp = ?, nama = ?, email = ?, department_id = ?,\n" +
                "manager_id = ?\n" +
                "where nrp = ?";
        try {
            statement = db.prepareStatement(query);
            statement.setString(1, students.getNrp());
            statement.setString(2, students.getStudent_name());
            statement.setString(3, students.getEmail());
            statement.setInt(4, students.getDepartmentId());
            statement.setInt(5, students.getManagerId());
            statement.setString(6, nrp);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcConnection.closeConnection(statement);
        }


    }
}
