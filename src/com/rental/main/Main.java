package com.rental.main;

import com.rental.dao.ICarLeaseRepository;
import com.rental.dao.ICarLeaseRepositoryImpl;
import com.rental.entity.Vehicle;

public class Main {

	// code to check database connection
//	public static void main(String[] args) {
//		// Initialize the database connection
//		Connection connection = DBConnection.getConnection();
//
//		try {
//			// Check if the connection is successful
//			if (connection != null && !connection.isClosed()) {
//				System.out.println("Database connection established successfully!");
//			} else {
//				System.out.println("Failed to connect to the database.");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (connection != null && !connection.isClosed()) {
//					connection.close();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//	}

	public static void main(String[] args) {
		// Create an instance of the repository
		ICarLeaseRepository carLeaseRepository = new ICarLeaseRepositoryImpl();

		// Add a sample vehicle
//		Vehicle vehicle = new Vehicle(8, "Audi", "Camry", 2022, 50.0, "available", 5, 2000);
//		carLeaseRepository.addVehicle(vehicle);
//
//		// Add a sample customer
//		Customer customer = new Customer(6, "Jay", "Doe", "john.doe@example.com", "123-456-7890");
//		carLeaseRepository.addCustomer(customer);

		// Find Vehicle by ID
		System.out.println("Vehicle by Id");
		Vehicle vehicleFound = carLeaseRepository.findVehicleById(8);
		System.out.println(vehicleFound);

		// Remove vehicle by ID
//		carLeaseRepository.removeVehicle(6);

		// List available vehicles
//		System.out.println("Available Vehicles:");
//		List<Vehicle> availableVehicles = carLeaseRepository.listAvailableVehicles();
//		for (Vehicle availableVehicle : availableVehicles) {
//			System.out.println(availableVehicle);
//		}

		// List of Rented Vehicle
//		System.out.println("List of Rented Vehicle");
//		List<Vehicle> rentedVehicle = carLeaseRepository.listRentedVehicles();

		// Create a lease for the customer with the available vehicle
//		Date startDate;
//		Date endDate;
//		try {
//			startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-15");
//			endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-25");
//
//			// Manually specify the leaseID and paymentID
//			int leaseID = 111;
//			int paymentID = 444;
//
//			Lease lease = carLeaseRepository.createLease(leaseID, customer.getCustomerID(),
//					availableVehicles.get(0).getVehicleID(), startDate, endDate);
//			System.out.println("Lease created: " + lease);
//
//			// List active leases
//			System.out.println("\nActive Leases:");
//			List<Lease> activeLeases = carLeaseRepository.listActiveLeases();
//			for (Lease activeLease : activeLeases) {
//				System.out.println(activeLease);
//			}
//
//			// List lease history
//			System.out.println("\nLease History:");
//			List<Lease> leaseHistory = carLeaseRepository.listLeaseHistory();
//			for (Lease historyLease : leaseHistory) {
//				System.out.println(historyLease);
//			}
//
//			// Record a payment for the lease with manually specified paymentID
//			carLeaseRepository.recordPayment(paymentID, lease, 1000.0);
//			System.out.println("\nPayment recorded for Lease ID " + lease.getLeaseID() + ": $" + 100.0);
//
//			// Return the vehicle
//			carLeaseRepository.returnVehicle(lease.getLeaseID());
//			System.out.println("\nVehicle returned for Lease ID " + lease.getLeaseID());
//
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}

	}

}
