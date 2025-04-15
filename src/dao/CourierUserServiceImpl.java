package dao;

import entity.Courier;
import entity.CourierCompany;
import exceptions.InvalidEmployeeIdException;
import exceptions.TrackingNumberNotFoundException;
import dao.ICourierUserService;
import util.DBConnUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourierUserServiceImpl implements ICourierUserService {
    private CourierCompany companyObj; // Retained for compatibility

    public CourierUserServiceImpl(CourierCompany companyObj) {
        this.companyObj = companyObj;
    }

    @Override
    public String placeOrder(Courier courierObj) {
        String sql = "INSERT INTO couriers (sender_name, sender_address, receiver_name, receiver_address, "
                   + "weight, status, tracking_number, user_id, delivery_date) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnUtil.getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courierObj.getSenderName());
            pstmt.setString(2, courierObj.getSenderAddress());
            pstmt.setString(3, courierObj.getReceiverName());
            pstmt.setString(4, courierObj.getReceiverAddress());
            pstmt.setDouble(5, courierObj.getWeight());
            pstmt.setString(6, courierObj.getStatus());
            pstmt.setString(7, courierObj.getTrackingNumber());
            pstmt.setLong(8, courierObj.getUserId());
            pstmt.setDate(9, new java.sql.Date(courierObj.getDeliveryDate().getTime()));

            pstmt.executeUpdate();
            return courierObj.getTrackingNumber();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to place order: " + e.getMessage(), e);
        }
    }

    @Override
    public String getOrderStatus(String trackingNumber) throws TrackingNumberNotFoundException {
        String sql = "SELECT status FROM couriers WHERE tracking_number = ?";
        
        try (Connection conn = DBConnUtil.getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, trackingNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("status");
            }
            throw new TrackingNumberNotFoundException("Tracking number not found: " + trackingNumber);

        } catch (SQLException e) {
            throw new TrackingNumberNotFoundException("Database error: " + e.getMessage());
        }
    }

    @Override
    public boolean cancelOrder(String trackingNumber) throws TrackingNumberNotFoundException {
        String sql = "DELETE FROM couriers WHERE tracking_number = ?";
        
        try (Connection conn = DBConnUtil.getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, trackingNumber);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new TrackingNumberNotFoundException("No order found with tracking number: " + trackingNumber);
            }
            return true;

        } catch (SQLException e) {
            throw new TrackingNumberNotFoundException("Database error: " + e.getMessage());
        }
    }

    @Override
    public List<Courier> getAssignedOrder(int courierStaffId) throws InvalidEmployeeIdException {
        // First verify employee exists
        String employeeCheckSql = "SELECT employee_id FROM employees WHERE employee_id = ?";
        try (Connection conn = DBConnUtil.getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(employeeCheckSql)) {

            pstmt.setInt(1, courierStaffId);
            ResultSet rs = pstmt.executeQuery();
            
            if (!rs.next()) {
                throw new InvalidEmployeeIdException("Employee ID not found: " + courierStaffId);
            }

        } catch (SQLException e) {
            throw new InvalidEmployeeIdException("Database error: " + e.getMessage());
        }

        // Get assigned orders
        String orderSql = "SELECT * FROM couriers WHERE assigned_employee_id = ?";
        List<Courier> assignedOrders = new ArrayList<>();

        try (Connection conn = DBConnUtil.getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(orderSql)) {

            pstmt.setInt(1, courierStaffId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Courier courier = new Courier();
                courier.setCourierID(rs.getLong("courier_id"));
                courier.setSenderName(rs.getString("sender_name"));
                courier.setSenderAddress(rs.getString("sender_address"));
                courier.setReceiverName(rs.getString("receiver_name"));
                courier.setReceiverAddress(rs.getString("receiver_address"));
                courier.setWeight(rs.getDouble("weight"));
                courier.setStatus(rs.getString("status"));
                courier.setTrackingNumber(rs.getString("tracking_number"));
                courier.setDeliveryDate(rs.getDate("delivery_date"));
                courier.setUserId(rs.getLong("user_id"));
                
                assignedOrders.add(courier);
            }
            return assignedOrders;

        } catch (SQLException e) {
            throw new InvalidEmployeeIdException("Error retrieving orders: " + e.getMessage());
        }
    }
}