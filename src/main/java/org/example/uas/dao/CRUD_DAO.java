package org.example.uas.dao;

import org.example.uas.Database.JdbcConnection;
import org.example.uas.beans.Employees;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface CRUD_DAO<T> {
    Connection db = JdbcConnection.getConnection();
    ArrayList<T> getAll() throws SQLException;
    void save(T t) throws SQLException;
    boolean update(T t) throws SQLException;
    void delete(String id) throws SQLException;
}


