package com.seekerscloud.pos.dao;

import com.seekerscloud.pos.db.DBConnection;
import com.seekerscloud.pos.entity.Customer;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseAccessCode {
    // save Customer
    public boolean saveCustomer(Customer c) throws SQLException, ClassNotFoundException {
            String sql="INSERT INTO Customer VALUES (?,?,?,?)";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1,c.getId());
            statement.setString(2,c.getName());
            statement.setString(3,c.getAddress());
            statement.setDouble(4,c.getSalary());
            return statement.executeUpdate()>0;
    }
    // update Customer
    public boolean updateCustomer(Customer c) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Customer SET name=?,address=?,salary=? WHERE id=?";
        PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
        statement.setString(1, c.getName());
        statement.setString(2, c.getAddress());
        statement.setDouble(3, c.getSalary());
        statement.setString(4, c.getId());
        return statement.executeUpdate()>0;
    }
}
