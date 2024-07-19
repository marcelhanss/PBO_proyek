package org.example.uas.dao;

import org.example.uas.Database.JdbcConnection;
import org.example.uas.beans.Departments;
import org.example.uas.beans.Employees;

import java.sql.*;
import java.util.ArrayList;
public class DepartmentDao implements CRUD_DAO<Departments> {
    @Override
    public ArrayList<Departments> getAll() throws SQLException {
        String query = "select e.department_id, e.department_name, m.nama from departments e\n" +
                "left join employees m on m.employee_id = e.manager_id where e.status_aktif=1";
        Statement statement = db.createStatement();
        ResultSet result = statement.executeQuery(query);
        ArrayList<Departments> listDepartment = new ArrayList<>();
        while (result.next()) {
            int id = result.getInt("department_id");
            String name = result.getString("department_name");
            String manager = result.getString("nama");
            listDepartment.add(new Departments(id, name, manager));
        }

        return listDepartment;
    }

    public static Departments get(String department_name) {
        PreparedStatement statement = null;
        ResultSet result = null;
        String query = "select e.department_id, e.department_name, m.nama from departments e\n" +
                "left join employees m on m.employee_id = e.manager_id \n" +
                "where e.status_aktif=1 and e.department_name = ? \n" +
                "order by e.department_id asc ";
        Departments departments = null;
        try {
            statement = db.prepareStatement(query);
            statement.setString(1, department_name);
            result = statement.executeQuery();

            while (result.next()) {
                departments = new Departments();
                departments.setDepartmentId(result.getInt("department_id"));
                departments.setName(result.getString("department_name"));
                departments.setNameEmployee(result.getString("nama"));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcConnection.closeConnection(result, statement);
        }
        return departments;

    }

    public ArrayList<String> getAllManager() throws SQLException {
        String query = "select nama from employees where isManager = false\n" +
                "and status_aktif = 1";
        Statement statement = db.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        ArrayList<String> listManager = new ArrayList<>();
        while (resultSet.next()) {
            String manager = resultSet.getString("nama");
            listManager.add(manager);
        }
        return listManager;
    }

    @Override
    public void save(Departments departments) throws SQLException {
        PreparedStatement statement = null;
        String query = "insert into departments(department_id, department_name,manager_id) values (?, ?, ?)";
        try {
            statement = db.prepareStatement(query);
            statement.setInt(1, departments.getDepartmentId());
            statement.setString(2, departments.getName());
            statement.setInt(3, departments.getManagerId());
            if(departments.getManagerId() == 0){
                statement.setNull(3, departments.getManagerId());
            }
            assignManager(departments.getManagerId());
            changeDeparment(departments.getDepartmentId(), departments.getManagerId());
            setManagerIdNull(departments.getManagerId());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcConnection.closeConnection(statement);
        }
    }

    private void assignManager(int employee_id) {
        String query = "update employees set isManager = true where employee_id = " + employee_id;
        Statement statement = null;
        try {
            statement = db.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.getErrorCode();
        } finally {
            JdbcConnection.closeConnection(statement);
        }

    }

    private void changeDeparment(int deparment_id, int employee_id) {
        String query = "update employees set department_id = " + deparment_id + " where employee_id = " + employee_id;
        Statement statement = null;
        try {
            statement = db.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.getErrorCode();
        } finally {
            JdbcConnection.closeConnection(statement);
        }
    }

    private void setManagerIdNull(int employee_id) {
        String query = "update employees set manager_id = null where employee_id = " + employee_id ;
        Statement statement = null;
        try {
            statement = db.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.getErrorCode();
        } finally {
            JdbcConnection.closeConnection(statement);
        }
    }
//    private int setManagerIdNull2(int employee_id) {
//        String query = "update departments set manager_id = null, where department_id = " + employee_id ;
//        Statement statement = null;
//        try {
//            statement = db.createStatement();
//            statement.executeUpdate(query);
//        } catch (SQLException e) {
//            e.getErrorCode();
//        } finally {
//            JdbcConnection.closeConnection(statement);
//        }
//        return employee_id;
//    }

    public int findRightManagerId(String nama) throws SQLException {
        String query = "select employee_id from employees where ismanager = false and nama = '" + nama + "'";
        Statement statement = db.createStatement();
        ResultSet result = statement.executeQuery(query);
        int manager = 0;
        while (result.next()) {
            manager = result.getInt("employee_id");
        }
        return manager;
    }


    @Override
    public boolean update(Departments departments) throws SQLException {
        return false;
    }

    @Override
    public void delete(String departments) throws SQLException {
        String query = "update departments set status_aktif = 0 where department_id = ?";
        try (PreparedStatement preparedStatement = db.prepareStatement(query)) {
            preparedStatement.setInt(1, Integer.parseInt(departments));
            preparedStatement.executeUpdate();
        }
    }

    public void update(Departments departments, String department) throws SQLException {
        PreparedStatement statement = null;
        String query = "update departments\n" +
                "set department_id = ?, department_name = ?, manager_id = ?\n" +
                "where department_name = ?";

        try {
            statement = db.prepareStatement(query);
            statement.setInt(1, departments.getDepartmentId());
            statement.setString(2, departments.getName());
            statement.setInt(3, departments.getManagerId());
            statement.setString(4, department);
            statement.execute();

            assignManager(departments.getManagerId());
            changeDeparment(departments.getDepartmentId(), departments.getManagerId());
            setManagerIdNull(departments.getManagerId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcConnection.closeConnection(statement);
        }
    }

    public ArrayList<Departments> getDepartmentData() {
        String query = "select department_name, count(*) from departments d\n" +
                "left join employees e on d.department_id = e.department_id\n" +
                "left join students s on d.department_id = s.department_id\n" +
                "group by department_name";
        ArrayList<Departments> listDepartments = new ArrayList<>();
        try {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery(query);

            while(result.next()) {
                Departments department = new Departments();
                department.setName(result.getString("department_name"));
                department.setCount(result.getInt("count"));
                listDepartments.add(department);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return listDepartments;
    }
//    private void resetPreviousManager() throws SQLException {
//        String query = "select manager_id from departments where department_id = null";
//        Statement statement = db.createStatement();
//        ResultSet resultSet = statement.executeQuery(query);
//        if (resultSet.next()) {
//            int previousManagerId = resultSet.getInt("manager_id");
//            if (previousManagerId != 0) {
//            }
//        }
//    }
//    private void setManagerFalse(Employees employees) throws SQLException {
//        String query = "update employees set isManager = false where nip =  ?";
//        PreparedStatement preparedStatement = db.prepareStatement(query);
//        preparedStatement.setString(1,employees.getNip());
//        preparedStatement.executeUpdate();
//    }
//    private void setManagerTrue(String nip) throws SQLException {
//        String query = "update employees set ismanager = true where nip = " + nip;
//        Statement statement = db.createStatement();
//        statement.executeUpdate(query);
//    }
//
//    private String findEmployeeId() throws SQLException {
//        String query = "select m.nip from departments d join employees m using (department_id) where m.ismanager = false";
//        Statement statement = db.createStatement();
//        ResultSet resultSet = statement.executeQuery(query);
//        String manager = "";
//        while (resultSet.next()) {
//            manager = resultSet.getString("nip");
//        }
//        return manager;
//    }
}
