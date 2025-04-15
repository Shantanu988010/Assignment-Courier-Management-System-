package dao;

import entity.*;
import exceptions.TrackingNumberNotFoundException;
import exceptions.InvalidEmployeeIdException;
import java.util.List;
import java.util.stream.Collectors;

public class CourierUserServiceCollectionImpl implements ICourierUserService {
    private CourierCompanyCollection companyObj;

    public CourierUserServiceCollectionImpl(CourierCompanyCollection companyObj) {
        this.companyObj = companyObj;
    }

    @Override
    public String placeOrder(Courier courierObj) {
        companyObj.addCourier(courierObj);
        return courierObj.getTrackingNumber();
    }

    @Override
    public String getOrderStatus(String trackingNumber) throws TrackingNumberNotFoundException {
        Courier courier = companyObj.getTrackingMap().get(trackingNumber);
        if(courier == null) {
            throw new TrackingNumberNotFoundException("Tracking #" + trackingNumber + " not found!");
        }
        return courier.getStatus();
    }

    @Override
    public boolean cancelOrder(String trackingNumber) throws TrackingNumberNotFoundException {
        if(!companyObj.getTrackingMap().containsKey(trackingNumber)) {
            throw new TrackingNumberNotFoundException("Invalid tracking number: " + trackingNumber);
        }
        companyObj.removeCourier(trackingNumber);
        return true;
    }

    @Override
    public List<Courier> getAssignedOrder(int courierStaffId) throws InvalidEmployeeIdException {
        // Verify employee exists
        boolean employeeExists = companyObj.getEmployeeDetails().stream()
            .anyMatch(e -> e.getEmployeeID() == courierStaffId);
        
        if(!employeeExists) {
            throw new InvalidEmployeeIdException("Invalid employee ID: " + courierStaffId);
        }
        
        // Filter couriers assigned to staff (example implementation)
        return companyObj.getCourierDetails().stream()
            .filter(c -> c.getUserId() == courierStaffId)
            .collect(Collectors.toList());
    }
}