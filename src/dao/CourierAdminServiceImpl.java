package dao;

import entity.CourierCompany;
import entity.Employee;
import exceptions.InvalidEmployeeIdException;
import util.DBConnUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CourierAdminServiceImpl implements ICourierAdminService {
    private CourierCompany companyObj; // Retained for compatibility

    public CourierAdminServiceImpl(CourierCompany companyObj) {
        this.companyObj = companyObj;
    }

    @Override
    public int addCourierStaff(Employee employee) throws InvalidEmployeeIdException {
        // Validate employee ID
        if (employee.getEmployeeID() <= 0) {
            throw new InvalidEmployeeIdException("Invalid Employee ID: " + employee.getEmployeeID());
        }

        String sql = "INSERT INTO employees (employee_id, employee_name, email, contact_number, role, salary) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnUtil.getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters for the SQL query
            pstmt.setInt(1, employee.getEmployeeID());
            pstmt.setString(2, employee.getEmployeeName());
            pstmt.setString(3, employee.getEmail());
            pstmt.setString(4, employee.getContactNumber());
            pstmt.setString(5, employee.getRole());
            pstmt.setDouble(6, employee.getSalary());

            // Execute the insert operation
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new SQLException("Failed to add employee: No rows affected");
            }

            return employee.getEmployeeID();

        } catch (SQLException e) {
            // Handle duplicate entry or other SQL errors
            if (e.getSQLState().equals("23000")) { // Primary key violation
                throw new InvalidEmployeeIdException("Employee ID already exists: " + employee.getEmployeeID());
            }
            throw new InvalidEmployeeIdException("Database error: " + e.getMessage());
        }
    }
}