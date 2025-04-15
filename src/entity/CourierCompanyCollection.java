package entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CourierCompanyCollection {
    private List<Courier> courierDetails = new ArrayList<>();
    private List<Employee> employeeDetails = new ArrayList<>();
    private List<Location> locationDetails = new ArrayList<>();
    private HashMap<String, Courier> trackingMap = new HashMap<>();

    public CourierCompanyCollection() {}

    public CourierCompanyCollection(String companyName) {
    }

    // Getters & Setters
    public List<Courier> getCourierDetails() { return courierDetails; }
    public List<Employee> getEmployeeDetails() { return employeeDetails; }
    public List<Location> getLocationDetails() { return locationDetails; }
    public HashMap<String, Courier> getTrackingMap() { return trackingMap; }
    
    public void addCourier(Courier courier) {
        courierDetails.add(courier);
        trackingMap.put(courier.getTrackingNumber(), courier);
    }
    
    public void removeCourier(String trackingNumber) {
        Courier toRemove = trackingMap.remove(trackingNumber);
        if(toRemove != null) {
            courierDetails.remove(toRemove);
        }
    }
}