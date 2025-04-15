package dao;

import entity.Employee;

import exceptions.InvalidEmployeeIdException;

public interface ICourierAdminService {
    int addCourierStaff(Employee employee) throws InvalidEmployeeIdException;
}