package com.rental.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;

import org.junit.Test;

import com.rental.dao.ICarLeaseRepository;
import com.rental.dao.ICarLeaseRepositoryImpl;
import com.rental.entity.Customer;
import com.rental.entity.Lease;
import com.rental.entity.Vehicle;
import com.rental.exception.CustomerNotFoundException;
import com.rental.exception.LeaseNotFoundException;
import com.rental.exception.VehicleNotFoundException;

public class CarLeaseRepositoryTest {
	@Test
	public void testCarCreation() {
		// Arrange
		ICarLeaseRepository carLeaseRepository = new ICarLeaseRepositoryImpl();
		Vehicle vehicle = new Vehicle(7, "Toyota", "Corolla", 2022, 50.0, "available", 5, 2000);

		// Act
		carLeaseRepository.addVehicle(vehicle);
		Vehicle retrievedVehicle = carLeaseRepository.findVehicleById(7);

		// Assert
		assertNotNull(retrievedVehicle);
		assertEquals(vehicle.getVehicleID(), retrievedVehicle.getVehicleID());
		assertEquals(vehicle.getMake(), retrievedVehicle.getMake());
		assertEquals(vehicle.getModel(), retrievedVehicle.getModel());
		assertEquals(vehicle.getYear(), retrievedVehicle.getYear());
		assertEquals(vehicle.getDailyRate(), retrievedVehicle.getDailyRate(), 0.001);
		assertEquals(vehicle.getStatus(), retrievedVehicle.getStatus());
		assertEquals(vehicle.getPassengerCapacity(), retrievedVehicle.getPassengerCapacity());
		assertEquals(vehicle.getEngineCapacity(), retrievedVehicle.getEngineCapacity());
	}

	@Test
	public void testLeaseCreation() {
		// Arrange
		ICarLeaseRepository carLeaseRepository = new ICarLeaseRepositoryImpl();
		Customer customer = new Customer(1, "John", "Doe", "john.doe@example.com", "123-456-7890");
		carLeaseRepository.addCustomer(customer);

		Vehicle vehicle = new Vehicle(1, "Toyota", "Corolla", 2022, 50.0, "available", 5, 2000);
		carLeaseRepository.addVehicle(vehicle);

		// Act
		Lease lease = carLeaseRepository.createLease(1, customer.getCustomerID(), vehicle.getVehicleID(),
				new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));

		// Assert
		assertNotNull(lease);
		assertEquals(1, lease.getLeaseID());
		assertEquals(customer.getCustomerID(), lease.getCustomerID());

		// Adjust the assertion to compare Vehicle objects
		assertEquals(vehicle.getVehicleID(), lease.getVehicleID().getVehicleID());
		assertEquals(vehicle.getMake(), lease.getVehicleID().getMake());
		assertEquals(vehicle.getModel(), lease.getVehicleID().getModel());
		assertEquals(vehicle.getYear(), lease.getVehicleID().getYear());
		assertEquals(vehicle.getDailyRate(), lease.getVehicleID().getDailyRate(), 0.001);
		assertEquals(vehicle.getStatus(), lease.getVehicleID().getStatus());
		assertEquals(vehicle.getPassengerCapacity(), lease.getVehicleID().getPassengerCapacity());
		assertEquals(vehicle.getEngineCapacity(), lease.getVehicleID().getEngineCapacity());
	}

	@Test
	public void testGetLeaseById() {
		// Arrange
		ICarLeaseRepository carLeaseRepository = new ICarLeaseRepositoryImpl();
		Customer customer = new Customer(3, "rohan", "Doe", "john.doe@example.com", "123-456-7890");
//		carLeaseRepository.addCustomer(customer);

		Vehicle vehicle = new Vehicle(3, "safari", "Corolla", 2022, 50.0, "notAvailable", 5, 2000);
//		carLeaseRepository.addVehicle(vehicle);

		Date startDate = new Date(System.currentTimeMillis());
		Date endDate = new Date(startDate.getTime() + 24 * 60 * 60 * 1000); // Adding one day

//		Lease createdLease = carLeaseRepository.createLease(3, customer.getCustomerID(), vehicle.getVehicleID(),
//				startDate, endDate);

		Lease createdLease = carLeaseRepository.getLeaseById(4);

		// Act
		Lease retrievedLease = carLeaseRepository.getLeaseById(createdLease.getLeaseID());

		// To check what is output
		System.out.println("Created Lease: " + createdLease);
		System.out.println("Retrieved Lease: " + retrievedLease);

		// Assert
		assertNotNull(retrievedLease);
		assertEquals(createdLease.getLeaseID(), retrievedLease.getLeaseID());
		assertEquals(createdLease.getCustomerID(), retrievedLease.getCustomerID());

		// Adjust the assertion to compare Vehicle objects
		assertEquals(createdLease.getVehicleID().getVehicleID(), retrievedLease.getVehicleID().getVehicleID());
		assertEquals(createdLease.getVehicleID().getMake(), retrievedLease.getVehicleID().getMake());
		assertEquals(createdLease.getVehicleID().getModel(), retrievedLease.getVehicleID().getModel());
		assertEquals(createdLease.getVehicleID().getYear(), retrievedLease.getVehicleID().getYear());
		assertEquals(createdLease.getVehicleID().getDailyRate(), retrievedLease.getVehicleID().getDailyRate(), 0.001);
		assertEquals(createdLease.getVehicleID().getStatus(), retrievedLease.getVehicleID().getStatus());
		assertEquals(createdLease.getVehicleID().getPassengerCapacity(),
				retrievedLease.getVehicleID().getPassengerCapacity());
		assertEquals(createdLease.getVehicleID().getEngineCapacity(),
				retrievedLease.getVehicleID().getEngineCapacity());
		assertEquals(createdLease.getStartDate(), retrievedLease.getStartDate());
		assertEquals(createdLease.getEndDate(), retrievedLease.getEndDate());
		assertEquals(createdLease.getType(), retrievedLease.getType());
	}

	@Test(expected = LeaseNotFoundException.class)
	public void testGetLeaseById_LeaseNotFound() {
		// Arrange
		int nonExistentLeaseId = 999; // Assuming this ID does not exist in the database
		ICarLeaseRepository carLeaseRepository = new ICarLeaseRepositoryImpl();

		// Act & Assert
		carLeaseRepository.getLeaseById(nonExistentLeaseId);
		// If the method call above does not throw LeaseNotFoundException, the test will
		// fail
	}

	@Test(expected = CustomerNotFoundException.class)
	public void testFindCustomerById_CustomerNotFound() {
		// Arrange
		int nonExistentCustomerId = 999; // Assuming this ID does not exist in the database
		ICarLeaseRepository carLeaseRepository = new ICarLeaseRepositoryImpl();

		// Act
		carLeaseRepository.findCustomerById(nonExistentCustomerId);
	}

	@Test(expected = VehicleNotFoundException.class)
	public void testFindVehicleById_VehicleNotFound() {
		// Arrange
		int nonExistentVehicleId = 999; // Assuming this ID does not exist in the database
		ICarLeaseRepository carLeaseRepository = new ICarLeaseRepositoryImpl();

		// Act
		carLeaseRepository.findVehicleById(nonExistentVehicleId);
	}

}
