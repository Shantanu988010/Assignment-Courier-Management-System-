package entity;

import java.util.ArrayList;
import java.util.List;

public class CourierCompany {
    private String companyName;
    private List<Courier> courierDetails = new ArrayList<>();
    private List<Employee> employeeDetails = new ArrayList<>();
    private List<Location> locationDetails = new ArrayList<>();

    public CourierCompany() {}

    public CourierCompany(String companyName) {
        this.companyName = companyName;
    }

    // Getters & Setters
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public List<Courier> getCourierDetails() { return courierDetails; }
    public void setCourierDetails(List<Courier> courierDetails) { this.courierDetails = courierDetails; }
    public List<Employee> getEmployeeDetails() { return employeeDetails; }
    public void setEmployeeDetails(List<Employee> employeeDetails) { this.employeeDetails = employeeDetails; }
    public List<Location> getLocationDetails() { return locationDetails; }
    public void setLocationDetails(List<Location> locationDetails) { this.locationDetails = locationDetails; }

    @Override
    public String toString() {
        return "CourierCompany [companyName=" + companyName + 
               ", couriers=" + courierDetails.size() + 
               ", employees=" + employeeDetails.size() + 
               ", locations=" + locationDetails.size() + "]";
    }
}
