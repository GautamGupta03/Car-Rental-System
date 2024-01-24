package com.rental.dao;

import java.util.Date;
import java.util.List;

import com.rental.entity.Customer;
import com.rental.entity.Lease;
import com.rental.entity.Vehicle;

public interface ICarLeaseRepository {
	// Vehicle Management
	// done
	void addVehicle(Vehicle vehicle);

	// done
	void removeVehicle(int vehicleID);

	// done
	List<Vehicle> listAvailableVehicles();

	List<Vehicle> listRentedVehicles();

	Vehicle findVehicleById(int vehicleID);

	// Customer Management
	void addCustomer(Customer customer);

	void removeCustomer(int customerID);

	List<Customer> listCustomers();

	Customer findCustomerById(int customerID);

	// Lease Management
	Lease createLease(int leaseID, int customerID, int vehicleID, Date startDate, Date endDate);

	void returnVehicle(int leaseID);

	List<Lease> listActiveLeases();

	List<Lease> listLeaseHistory();

	// Payment Handling
	void recordPayment(int paymentID, Lease lease, double amount);

	Lease getLeaseById(int leaseID);
}