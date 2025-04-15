package dao;
import entity.Courier;
import exceptions.TrackingNumberNotFoundException;
import exceptions.InvalidEmployeeIdException;
import java.util.List;

public interface ICourierUserService {
    String placeOrder(Courier courierObj);
    String getOrderStatus(String trackingNumber) throws TrackingNumberNotFoundException;
    boolean cancelOrder(String trackingNumber) throws TrackingNumberNotFoundException;
    List<Courier> getAssignedOrder(int courierStaffId) throws InvalidEmployeeIdException;
}