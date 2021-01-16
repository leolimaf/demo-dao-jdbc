package model.dao.impl;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {

    private Connection connection;

    public DepartmentDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Department obj) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO department " +
                            "(Name) Values (?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            preparedStatement.setString(1, obj.getName());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    obj.setId(id);
                }
            } else {
                throw new DbException("Unexpected error! No rows affected!");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public void update(Department obj) {
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement(
                    "UPDATE department " +
                    "SET Name = ? " +
                    "WHERE Id = ?"
            );
            preparedStatement.setString(1, obj.getName());
            preparedStatement.setInt(2, obj.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM department WHERE Id = ?");
            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0){
                throw new DbException("Id not found! No rows affected!");
            }

        } catch (SQLException e){
            throw new DbIntegrityException(e.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM department " +
                            "WHERE Id = ?"
            );
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){

                Department department = new Department();
                department.setId(resultSet.getInt("Id"));
                department.setName(resultSet.getString("Name"));
                return department;
            }
            return null;
        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
            DB.closeResultSet(resultSet);
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement("SELECT * FROM department");
            resultSet = preparedStatement.executeQuery();
            List<Department> departments = new ArrayList<>();

            while (resultSet.next()){
                Department department = new Department();
                department.setId(resultSet.getInt("Id"));
                department.setName(resultSet.getString("Name"));
                departments.add(department);
            }
            return departments;

        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
            DB.closeResultSet(resultSet);
        }
    }
}
