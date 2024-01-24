package com.rental.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.rental.entity.Customer;
import com.rental.entity.Lease;
import com.rental.entity.Vehicle;
import com.rental.exception.CustomerNotFoundException;
import com.rental.exception.LeaseNotFoundException;
import com.rental.exception.VehicleNotFoundException;
import com.rental.util.DBConnection;

public class ICarLeaseRepositoryImpl implements ICarLeaseRepository {

	private Connection connection;

	public ICarLeaseRepositoryImpl() {
		this.connection = DBConnection.getConnection();
	}

	@Override
	public void addVehicle(Vehicle vehicle) {
		String sql = "INSERT INTO Vehicle (vehicleID, make, model, year, dailyRate, status, passengerCapacity, engineCapacity) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, vehicle.getVehicleID()); // Set the provided vehicleID
			preparedStatement.setString(2, vehicle.getMake());
			preparedStatement.setString(3, vehicle.getModel());
			preparedStatement.setInt(4, vehicle.getYear());
			preparedStatement.setDouble(5, vehicle.getDailyRate());
			preparedStatement.setString(6, vehicle.getStatus());
			preparedStatement.setInt(7, vehicle.getPassengerCapacity());
			preparedStatement.setInt(8, vehicle.getEngineCapacity());

			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Creating vehicle failed, no rows affected.");
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error adding vehicle to the database.", e);
		}
	}

	@Override
	public void removeVehicle(int vehicleID) {
		String sql = "DELETE FROM Vehicle WHERE vehicleID = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, vehicleID);
			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Removing vehicle failed, no rows affected.");
			} else {
				System.out.println("Removed vehicle id: " + vehicleID);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error removing vehicle from the database.", e);
		}
	}

	@Override
	public List<Vehicle> listAvailableVehicles() {
		List<Vehicle> vehicles = new ArrayList<>();
		try (Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM Vehicle WHERE status = 'available'")) {

			while (resultSet.next()) {
				Vehicle vehicle = mapResultSetToVehicle(resultSet);
				vehicles.add(vehicle);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error listing available vehicles from the database.", e);
		}
		return vehicles;
	}

	@Override
	public List<Vehicle> listRentedVehicles() {
		List<Vehicle> vehicles = new ArrayList<>();
		String sql = "SELECT * FROM Vehicle WHERE status = 'notAvailable'";
		try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
			while (resultSet.next()) {
				Vehicle vehicle = mapResultSetToVehicle(resultSet);
				vehicles.add(vehicle);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error listing rented vehicles from the database.", e);
		}
		return vehicles;
	}

	@Override
	public Vehicle findVehicleById(int vehicleID) {
		String sql = "SELECT * FROM Vehicle WHERE vehicleID = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, vehicleID);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					return mapResultSetToVehicle(resultSet);
				} else {
					// If the vehicle is not found, throw CarNotFoundException
					throw new VehicleNotFoundException("Vehicle not found with ID: " + vehicleID);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error finding vehicle from the database.", e);
		}
	}

	private Vehicle mapResultSetToVehicle(ResultSet resultSet) throws SQLException {
		Vehicle vehicle = new Vehicle();
		vehicle.setVehicleID(resultSet.getInt("vehicleID"));
		vehicle.setMake(resultSet.getString("make"));
		vehicle.setModel(resultSet.getString("model"));
		vehicle.setYear(resultSet.getInt("year"));
		vehicle.setDailyRate(resultSet.getDouble("dailyRate"));
		vehicle.setStatus(resultSet.getString("status"));
		vehicle.setPassengerCapacity(resultSet.getInt("passengerCapacity"));
		vehicle.setEngineCapacity(resultSet.getInt("engineCapacity"));
		return vehicle;
	}

	@Override
	public void addCustomer(Customer customer) {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(
						"INSERT INTO Customer(customerID, firstName, lastName, email, phoneNumber) VALUES (?, ?, ?, ?, ?)")) {

			preparedStatement.setInt(1, customer.getCustomerID()); // Set the provided customerID
			preparedStatement.setString(2, customer.getFirstName());
			preparedStatement.setString(3, customer.getLastName());
			preparedStatement.setString(4, customer.getEmail());
			preparedStatement.setString(5, customer.getPhoneNumber());

			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace(); // Handle or throw a custom exception
		}
	}

	@Override
	public void removeCustomer(int customerID) {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement("DELETE FROM Customer WHERE customerID = ?")) {

			preparedStatement.setInt(1, customerID);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace(); // Handle or throw a custom exception
		}
	}

	@Override
	public List<Customer> listCustomers() {
		List<Customer> customers = new ArrayList<>();

		try (Connection connection = DBConnection.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM Customer")) {

			while (resultSet.next()) {
				Customer customer = new Customer(resultSet.getInt("customerID"), resultSet.getString("firstName"),
						resultSet.getString("lastName"), resultSet.getString("email"),
						resultSet.getString("phoneNumber"));
				customers.add(customer);
			}

		} catch (SQLException e) {
			e.printStackTrace(); // Handle or throw a custom exception
		}

		return customers;
	}

	@Override
	public Customer findCustomerById(int customerID) {
		Customer customer = null;

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement("SELECT * FROM Customer WHERE customerID = ?")) {

			preparedStatement.setInt(1, customerID);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					customer = new Customer(resultSet.getInt("customerID"), resultSet.getString("firstName"),
							resultSet.getString("lastName"), resultSet.getString("email"),
							resultSet.getString("phoneNumber"));
				} else {
					// If the customer is not found, throw CustomerNotFoundException
					throw new CustomerNotFoundException("Customer with ID " + customerID + " not found.");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace(); // Handle or throw a custom exception
		}

		return customer;
	}

	@Override
	public Lease createLease(int leaseID, int customerID, int vehicleID, Date startDate, Date endDate) {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement checkLeaseIdStatement = connection
						.prepareStatement("SELECT leaseID FROM Lease WHERE leaseID = ?");
				PreparedStatement insertLeaseStatement = connection.prepareStatement(
						"INSERT INTO Lease(leaseID, vehicleID, customerID, startDate, endDate, type) VALUES (?, ?, ?, ?, ?, ?)");
				PreparedStatement updateVehicleStatusStatement = connection
						.prepareStatement("UPDATE Vehicle SET status = 'notAvailable' WHERE vehicleID = ?")) {

			// Check if the lease ID is already in use
			checkLeaseIdStatement.setInt(1, leaseID);
			ResultSet resultSet = checkLeaseIdStatement.executeQuery();

			if (resultSet.next()) {
				throw new SQLException("Lease ID already in use.");
			}

			// Insert a new lease
			insertLeaseStatement.setInt(1, leaseID);
			insertLeaseStatement.setInt(2, vehicleID);
			insertLeaseStatement.setInt(3, customerID);
			insertLeaseStatement.setDate(4, new java.sql.Date(startDate.getTime()));
			insertLeaseStatement.setDate(5, new java.sql.Date(endDate.getTime()));
			insertLeaseStatement.setString(6, "DailyLease"); // Adjust type as needed
			int affectedRows = insertLeaseStatement.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Creating lease failed, no rows affected.");
			}

			// Fetch the details of the newly created lease from the database
			Vehicle leasedVehicle = getLeasedVehicleDetailsFromDatabase(vehicleID, connection);

			// Create a Lease object with retrieved details
			Lease newLease = new Lease(leaseID, leasedVehicle, customerID, startDate, endDate, "DailyLease");

			// Update the vehicle status
			updateVehicleStatusStatement.setInt(1, vehicleID);
			updateVehicleStatusStatement.executeUpdate();

			return newLease;

		} catch (SQLException e) {
			e.printStackTrace(); // Log or handle the exception appropriately
			return null; // Handle the exception and return accordingly
		}
	}

	@Override
	public Lease getLeaseById(int leaseID) {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement selectLeaseStatement = connection
						.prepareStatement("SELECT * FROM Lease WHERE leaseID = ?")) {

			selectLeaseStatement.setInt(1, leaseID);

			try (ResultSet resultSet = selectLeaseStatement.executeQuery()) {
				if (resultSet.next()) {
					int fetchedLeaseID = resultSet.getInt("leaseID");
					int vehicleID = resultSet.getInt("vehicleID");
					int customerID = resultSet.getInt("customerID");
					Date startDate = resultSet.getDate("startDate");
					Date endDate = resultSet.getDate("endDate");
					String type = resultSet.getString("type");

					Vehicle leasedVehicle = getLeasedVehicleDetailsFromDatabase(vehicleID, connection);

					return new Lease(fetchedLeaseID, leasedVehicle, customerID, startDate, endDate, type);
				} else {
					// If the lease is not found, throw LeaseNotFoundException
					throw new LeaseNotFoundException("Lease with ID " + leaseID + " not found.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Log or handle the exception appropriately
			return null; // Handle the exception and return accordingly
		}
	}

	// Helper method to retrieve leased vehicle details from the database
	private Vehicle getLeasedVehicleDetailsFromDatabase(int vehicleID, Connection connection) {
		try (PreparedStatement selectVehicleStatement = connection
				.prepareStatement("SELECT * FROM Vehicle WHERE vehicleID = ?")) {
			selectVehicleStatement.setInt(1, vehicleID);

			try (ResultSet resultSet = selectVehicleStatement.executeQuery()) {
				if (resultSet.next()) {
					// Replace the placeholders with the actual column names in your Vehicle table
					int fetchedVehicleID = resultSet.getInt("vehicleID");
					String make = resultSet.getString("make");
					String model = resultSet.getString("model");
					int year = resultSet.getInt("year");
					double dailyRate = resultSet.getDouble("dailyRate");
					String status = resultSet.getString("status");
					int passengerCapacity = resultSet.getInt("passengerCapacity");
					int engineCapacity = resultSet.getInt("engineCapacity");

					// Create a Vehicle object with the retrieved details
					return new Vehicle(fetchedVehicleID, make, model, year, dailyRate, status, passengerCapacity,
							engineCapacity);
				} else {
					// Handle the case where no vehicle is found with the specified ID
					return null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Log or handle the exception appropriately
			return null; // Handle the exception and return accordingly
		}
	}

	@Override
	public void returnVehicle(int leaseID) {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement deletePaymentStatement = connection
						.prepareStatement("DELETE FROM Payment WHERE leaseID = ?");
				PreparedStatement deleteLeaseStatement = connection
						.prepareStatement("DELETE FROM Lease WHERE leaseID = ?");
				PreparedStatement updateVehicleStatusStatement = connection.prepareStatement(
						"UPDATE Vehicle SET status = 'available' WHERE vehicleID = (SELECT vehicleID FROM Lease WHERE leaseID = ?)")) {

			// Delete payments associated with the lease
			deletePaymentStatement.setInt(1, leaseID);
			deletePaymentStatement.executeUpdate();

			// Delete the lease
			deleteLeaseStatement.setInt(1, leaseID);
			int affectedRows = deleteLeaseStatement.executeUpdate();

			if (affectedRows > 0) {
				// Update the vehicle status to 'available'
				updateVehicleStatusStatement.setInt(1, leaseID);
				updateVehicleStatusStatement.executeUpdate();
			} else {
				throw new LeaseNotFoundException("Lease with ID " + leaseID + " not found.");
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Handle or throw a custom exception
		}
	}

	@Override
	public List<Lease> listActiveLeases() {
		List<Lease> activeLeases = new ArrayList<>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT * FROM Lease WHERE endDate >= CURDATE()");
				ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				Lease lease = extractLeaseFromResultSet(resultSet);
				activeLeases.add(lease);
			}

		} catch (SQLException e) {
			e.printStackTrace(); // Handle or throw a custom exception
		}

		return activeLeases;
	}

	@Override
	public List<Lease> listLeaseHistory() {
		List<Lease> leaseHistory = new ArrayList<>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT * FROM Lease WHERE endDate < CURDATE()");
				ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				Lease lease = extractLeaseFromResultSet(resultSet);
				leaseHistory.add(lease);
			}

		} catch (SQLException e) {
			e.printStackTrace(); // Handle or throw a custom exception
		}

		return leaseHistory;
	}

	@Override
	public void recordPayment(int paymentID, Lease lease, double amount) {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement insertPaymentStatement = connection.prepareStatement(
						"INSERT INTO Payment(paymentID, leaseID, paymentDate, amount) VALUES (?, ?, CURDATE(), ?)")) {

			insertPaymentStatement.setInt(1, paymentID);
			insertPaymentStatement.setInt(2, lease.getLeaseID());
			insertPaymentStatement.setDouble(3, amount);
			insertPaymentStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace(); // Handle or throw a custom exception
		}
	}

	// Helper method to extract Lease details from ResultSet
	private Lease extractLeaseFromResultSet(ResultSet resultSet) throws SQLException {
		int leaseID = resultSet.getInt("leaseID");
		int vehicleID = resultSet.getInt("vehicleID");
		int customerID = resultSet.getInt("customerID");
		Date startDate = resultSet.getDate("startDate");
		Date endDate = resultSet.getDate("endDate");
		String type = resultSet.getString("type");

		// Fetch vehicle details
		Vehicle leasedVehicle = getVehicleById(vehicleID);

		return new Lease(leaseID, leasedVehicle, customerID, startDate, endDate, type);
	}

	// Helper method to get vehicle details by vehicleID
	private Vehicle getVehicleById(int vehicleID) {
		Vehicle vehicle = null;

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement("SELECT * FROM Vehicle WHERE vehicleID = ?")) {

			preparedStatement.setInt(1, vehicleID);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					vehicle = new Vehicle(resultSet.getInt("vehicleID"), resultSet.getString("make"),
							resultSet.getString("model"), resultSet.getInt("year"), resultSet.getDouble("dailyRate"),
							resultSet.getString("status"), resultSet.getInt("passengerCapacity"),
							resultSet.getInt("engineCapacity"));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace(); // Handle or throw a custom exception
		}

		return vehicle;
	}

	// Helper method to get vehicleID by leaseID
	private int getVehicleIdByLeaseId(int leaseID) {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement("SELECT vehicleID FROM Lease WHERE leaseID = ?")) {

			preparedStatement.setInt(1, leaseID);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt("vehicleID");
				} else {
					throw new LeaseNotFoundException("Lease with ID " + leaseID + " not found.");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace(); // Handle or throw a custom exception
			return -1;
		}
	}

}
