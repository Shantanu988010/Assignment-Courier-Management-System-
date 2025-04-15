package main;

import dao.CourierAdminServiceImpl;
import dao.CourierUserServiceImpl;
import dao.ICourierAdminService;
import dao.ICourierUserService;
import entity.Courier;
import entity.CourierCompany;
import entity.Employee;
import exceptions.InvalidEmployeeIdException;
import exceptions.TrackingNumberNotFoundException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class MainMeathod {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        CourierCompany company = new CourierCompany("TrackFast Pvt Ltd");
        ICourierAdminService adminService = new CourierAdminServiceImpl(company);
        ICourierUserService userService = new CourierUserServiceImpl(company);

        boolean exit = false;

        while (!exit) {
            System.out.println("\n---- Courier Management System ----");
            System.out.println("1. Place Order");
            System.out.println("2. Get Order Status");
            System.out.println("3. Cancel Order");
            System.out.println("4. Get Assigned Orders");
            System.out.println("5. Add Courier Staff");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1: // placeOrder
                    try {
                        Courier courier = new Courier();
                        System.out.print("Enter Sender Name: ");
                        courier.setSenderName(sc.nextLine());

                        System.out.print("Enter Sender Address: ");
                        courier.setSenderAddress(sc.nextLine());

                        System.out.print("Enter Receiver Name: ");
                        courier.setReceiverName(sc.nextLine());

                        System.out.print("Enter Receiver Address: ");
                        courier.setReceiverAddress(sc.nextLine());

                        System.out.print("Enter Weight: ");
                        courier.setWeight(sc.nextDouble());
                        sc.nextLine();

                        System.out.print("Enter Delivery Date (yyyy-MM-dd): ");
                        Date deliveryDate = new SimpleDateFormat("yyyy-MM-dd").parse(sc.nextLine());
                        courier.setDeliveryDate(deliveryDate);

                        System.out.print("Enter User ID: ");
                        courier.setUserId(sc.nextLong());
                        sc.nextLine();

                        courier.setStatus("yetToTransit");
                        courier.setTrackingNumber(Courier.generateTrackingNumber());

                        String trackingNum = userService.placeOrder(courier);
                        System.out.println("Order placed successfully! Tracking Number: " + trackingNum);
                    } catch (Exception e) {
                        System.out.println("Error placing order: " + e.getMessage());
                    }
                    break;

                case 2: // getOrderStatus
                    try {
                        System.out.print("Enter Tracking Number: ");
                        String trackingNumber = sc.nextLine();
                        String status = userService.getOrderStatus(trackingNumber);
                        System.out.println("Order Status: " + status);
                    } catch (TrackingNumberNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 3: // cancelOrder
                    try {
                        System.out.print("Enter Tracking Number: ");
                        String trackingNumber = sc.nextLine();
                        boolean cancelled = userService.cancelOrder(trackingNumber);
                        System.out.println(cancelled ? "Order cancelled successfully!" : "Cancellation failed!");
                    } catch (TrackingNumberNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 4: // getAssignedOrder
                    try {
                        System.out.print("Enter Courier Staff ID: ");
                        int staffId = sc.nextInt();
                        sc.nextLine();
                        List<Courier> orders = userService.getAssignedOrder(staffId);
                        if (orders.isEmpty()) {
                            System.out.println("No orders assigned to this employee.");
                        } else {
                            System.out.println("Assigned Orders:");
                            for (Courier c : orders) {
                                System.out.println("Tracking: " + c.getTrackingNumber() + ", Status: " + c.getStatus());
                            }
                        }
                    } catch (InvalidEmployeeIdException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 5: // addCourierStaff
                    try {
                        System.out.print("Enter Employee ID: ");
                        int empId = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter Employee Name: ");
                        String name = sc.nextLine();

                        System.out.print("Enter Email: ");
                        String email = sc.nextLine();

                        System.out.print("Enter Contact Number: ");
                        String contact = sc.nextLine();

                        System.out.print("Enter Role: ");
                        String role = sc.nextLine();

                        System.out.print("Enter Salary: ");
                        double salary = sc.nextDouble();
                        sc.nextLine();

                        Employee emp = new Employee(empId, name, email, contact, role, salary);
                        int addedId = adminService.addCourierStaff(emp);
                        System.out.println("Courier staff added with ID: " + addedId);
                    } catch (InvalidEmployeeIdException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 6:
                    exit = true;
                    System.out.println("Thank you! Exiting system.");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        sc.close();
    }
}
