package org.example.uas.dao;

import org.example.uas.Database.JdbcConnection;
import org.example.uas.beans.Departments;
import org.example.uas.beans.Employees;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class EmployeeDao implements CRUD_DAO<Employees> {
    @Override
    public ArrayList<Employees> getAll() throws SQLException {
        String query = "select e.nip, e.nama, e.email, d.department_name, m.nama as manager from employees e\n" +
                "left join employees m on m.employee_id = e.manager_id\n" +
                "left join departments d on e.department_id = d.department_id\nwhere e.status_aktif = 1 order by e.employee_id";
        Statement statement = db.createStatement();
        ResultSet result = statement.executeQuery(query);
        ArrayList<Employees> listEmployees = new ArrayList<>();
        while (result.next()) {
            String nip = result.getString("nip");
            String employeeName = result.getString("nama");
            String email = result.getString("email");
            String deparment_name = result.getString("department_name");
            String manager = result.getString("manager");

            if(manager == null)  {
                manager = "-";
            }

            if(deparment_name == null) {
                deparment_name = "-";
            }

            listEmployees.add(new Employees(nip,employeeName,email,deparment_name, manager));
        }
        return listEmployees;
    }

    public static Employees get(String nip) {
        PreparedStatement statement = null;
        ResultSet result = null;
        String query = "select e.nip, e.nama, e.email, d.department_name, m.nama as manager from employees e\n" +
                "left join employees m on m.employee_id = e.manager_id\n" +
                "left join departments d on e.department_id = d.department_id\n" +
                "where e.status_aktif = 1 and e.nip = ? order by e.employee_id asc";
        Employees employee = null;
        try {
            statement = db.prepareStatement(query);
            statement.setString(1, nip);
            result = statement.executeQuery();

            while(result.next()) {
                employee = new Employees();
                employee.setNip(result.getString("nip"));
                employee.setNama(result.getString("nama"));
                employee.setEmail(result.getString("email"));
                employee.setDepartmentName(result.getString("department_name"));
                employee.setManager(result.getString("manager"));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcConnection.closeConnection(result, statement);
        }
        return employee;
    }

    public ArrayList<Employees> getAllAvailableEmployee() throws SQLException {
        String query = "select nip, nama, email from employees\n" +
                "where status_aktif = 1 and isManager = false";
        Statement statement = db.createStatement();
        ResultSet result = statement.executeQuery(query);
        ArrayList<Employees> listEmployees = new ArrayList<>();
        while (result.next()) {
            String nip = result.getString("nip");
            String employeeName = result.getString("nama");
            String email = result.getString("email");

            listEmployees.add(new Employees(nip,employeeName,email));
        }

        return listEmployees;
    }

    public ArrayList<Employees> getAllManager() throws SQLException {
        String query = "select e.nip, e.nama, e.email, d.department_name from employees e\n" +
                "join departments d on e.department_id = d.department_id\n" +
                "where e.status_aktif = 1 and isManager = true";
        Statement statement = db.createStatement();
        ResultSet result = statement.executeQuery(query);
        ArrayList<Employees> listEmployees = new ArrayList<>();
        while (result.next()) {
            String nip = result.getString("nip");
            String employeeName = result.getString("nama");
            String email = result.getString("email");
            String deparmentName = result.getString("department_name");

            listEmployees.add(new Employees(nip,employeeName,email, deparmentName));
        }

        return listEmployees;
    }


    public ArrayList<String> getAllDepartments() throws SQLException {
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

    public ArrayList<String> getAllAvailableDepartments() throws SQLException {
        String query = "select distinct department_name from departments where manager_id is null or manager_id = 0";
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
    public void save(Employees employee) throws SQLException {
        PreparedStatement statement = null;
        String query = "insert into employees(nip, nama, email, department_id, manager_id) values (?, ?, ?, ?, ?)";
        try {
            statement = db.prepareStatement(query);
            statement.setString(1, employee.getNip());
            statement.setString(2, employee.getNama());
            statement.setString(3, employee.getEmail());
            statement.setInt(4, employee.getDeparmentID());
            statement.setInt(5, employee.getManagerID());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcConnection.closeConnection(statement);
        }
    }

    public int findRightDepartment(String department_name) throws SQLException {
        String query = "select distinct department_id from departments where department_name = '" + department_name + "'";
        Statement statement = db.createStatement();
        ResultSet result = statement.executeQuery(query);
        int department = 0;
        while(result.next()) {
            department = result.getInt("department_id");
        }

        return department;
    }


    public int findCorrespondingManager(int department_id) throws SQLException {
        String query = "select distinct d.manager_id from employees m join departments d on m.department_id = d.department_id where d.department_id = " + department_id;
        Statement statement = db.createStatement();
        ResultSet result = statement.executeQuery(query);
        int manager_id = 0;
        while(result.next()) {
            manager_id = result.getInt("manager_id");
        }
        return manager_id;
    }


    @Override
    public boolean update(Employees employees) throws SQLException {
        return false;
    }

    @Override
    public void delete(String description) throws SQLException {
        String query = "update employees set status_aktif = 0 where nip = ?";
        try(PreparedStatement preparedStatement = db.prepareStatement(query)) {
            preparedStatement.setString(1, description);
            preparedStatement.executeUpdate();
        }
    }

    public ArrayList<Employees> historyEmployee() throws SQLException {
        String query = "select e.nip, e.nama, d.department_name from employees e\n" +
                "join departments d using(department_id)\n" +
                "where e.status_aktif = 0";
        Statement statement = db.createStatement();
        ResultSet result = statement.executeQuery(query);
        ArrayList<Employees> listDepartments = new ArrayList<>();
        while (result.next()) {
            Employees employee = new Employees();
            employee.setNip(result.getString("nip"));
            employee.setNama(result.getString("nama"));
            employee.setDepartmentName(result.getString("department_name"));
            listDepartments.add(employee);

        }

        return listDepartments;
    }


    public void update(Employees employee, String nip) throws SQLException {
        PreparedStatement statement = null;
        String query = "update employees \n" +
                "set nip = ?, nama = ?, email = ?, department_id = ?,\n" +
                "manager_id = ?\n" +
                "where nip = ?";
        try {
            statement = db.prepareStatement(query);
            statement.setString(1, employee.getNip());
            statement.setString(2, employee.getNama());
            statement.setString(3, employee.getEmail());
            statement.setInt(4, employee.getDeparmentID());
            statement.setInt(5, employee.getManagerID());
            statement.setString(6, nip);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcConnection.closeConnection(statement);
        }
    }

    public boolean canAssignManager(String departmentName) throws SQLException {
        String query = "select count(*) from employees e join departments d ON e.department_id = d.department_id " +
                "where d.department_name = ? and e.isManager = true";
        PreparedStatement preparedStatement = db.prepareStatement(query);
        preparedStatement.setString(1, departmentName);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1) == 0;
        }
        return false;
    }

    public void assignManager(Employees employee, String departmentName) throws SQLException {
        String query = "update employees set isManager = true, manager_id = null  where nip = ?";
        //1. ubah department id si employeee(manager baru) menjadi department id yang dipilih
        // dapatkan deparment id dari combo box di assign view, findRightDepartment()

        //2. ubah di table DEPARMENTS manager idnya sesuai dengan employee (manager baru yang dipilih)
        // dapatkan dari parameter Employees employee, update deparments set manager_id = employee.getNIP where
        // department id = .......

        // 3. ubah semua orang yang bukan manager, manager_id sesuai dengan employee(manager yang dipilih)
        setDeparmentsForNewManager(employee.getNip(), departmentName);

        int employeeId = findEmployeeId(employee.getNip());
        int departmentId = findRightDepartment(departmentName);
        String query2 = "update departments set manager_id = " + employeeId + " where department_id = " + departmentId;
        String query3 = "update employees set manager_id = " + employeeId + " where department_id = " + departmentId + " and ismanager = false";

        PreparedStatement preparedStatement = db.prepareStatement(query);
        preparedStatement.setString(1, employee.getNip());
        preparedStatement.executeUpdate();

        Statement statement = db.createStatement();
        statement.executeUpdate(query2);

        Statement statement1 = db.createStatement();
        statement1.executeUpdate(query3);

        setManagerIdForStudent(employeeId, departmentId);

        JdbcConnection.closeConnection(preparedStatement);
    }


    public void setDeparmentsForNewManager(String nip, String departmentName) throws SQLException {
        int departmentId = findRightDepartment(departmentName);
        System.out.println(departmentId);
        String query = "update employees set department_id = " + departmentId + " where nip = '" + nip + "'";
        Statement statement = db.createStatement();
        statement.executeUpdate(query);
        JdbcConnection.closeConnection(statement);
    }

    private int findEmployeeId(String NIP) throws SQLException {
        String query = "select employee_id from employees where nip = '" + NIP + "'";
        Statement statement = db.createStatement();
        ResultSet result = statement.executeQuery(query);

        int employeeId = 0;
        while(result.next()) {
            employeeId = result.getInt("employee_id");
        }
        return employeeId;
    }

    public void removeManager(Employees employee) throws SQLException {
        EmployeeDao employeeDao = new EmployeeDao();
        String query = "update employees set isManager = false, department_id = null where nip = ?" ;
        String query2 = "update departments set manager_id = null where department_id = ?";

        PreparedStatement preparedStatement = db.prepareStatement(query);
        PreparedStatement preparedStatement2 = db.prepareStatement(query2);
        preparedStatement.setString(1, employee.getNip());
        int departmentId = employeeDao.findRightDepartment(employee.getDepartmentName());
        preparedStatement2.setInt(1, departmentId);
        preparedStatement.executeUpdate();
        preparedStatement2.executeUpdate();

        setNullManagerIdForEmployeeStudent(departmentId);
    }

    private void setNullManagerIdForEmployeeStudent(int departmentId) {
        String queryUpdateEmployee = "update employees set manager_id = null where department_id = " + departmentId;
        String queryUpdateStudent = "update students set manager_id = null where department_id = " + departmentId;
        try {
            Statement statement = db.createStatement();
            statement.executeUpdate(queryUpdateEmployee);
            statement.executeUpdate(queryUpdateStudent);
            JdbcConnection.closeConnection(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setManagerIdForStudent(int manager_id, int department_id){
        String query = "update students set manager_id = " + manager_id + " where department_id = " + department_id;
        try {
            Statement statement = db.createStatement();
            statement.executeUpdate(query);
            JdbcConnection.closeConnection(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
