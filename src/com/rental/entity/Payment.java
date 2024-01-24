package com.rental.entity;

import java.sql.Date;

public class Payment {

	private int paymentID;
	private int leaseID;
	private Date paymentDate;
	private double amount;

	public int getPaymentID() {
		return paymentID;
	}

	public void setPaymentID(int paymentID) {
		this.paymentID = paymentID;
	}

	public int getLeaseID() {
		return leaseID;
	}

	public void setLeaseID(int leaseID) {
		this.leaseID = leaseID;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "Payment [paymentID=" + paymentID + ", leaseID=" + leaseID + ", paymentDate=" + paymentDate + ", amount="
				+ amount + "]";
	}

	public Payment(int paymentID, int leaseID, Date paymentDate, double amount) {
		super();
		this.paymentID = paymentID;
		this.leaseID = leaseID;
		this.paymentDate = paymentDate;
		this.amount = amount;
	}

	public Payment() {
		super();
		// TODO Auto-generated constructor stub
	}

}
