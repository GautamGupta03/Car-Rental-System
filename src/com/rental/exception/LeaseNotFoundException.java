package com.rental.exception;

public class LeaseNotFoundException extends RuntimeException {
	public LeaseNotFoundException(String message) {
		super(message);
	}
}
