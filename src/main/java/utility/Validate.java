package utility;

import customexceptions.InvalidValueException;

public class Validate {

	public static void email(String email) throws InvalidValueException {
		if (!email.matches("[a-z]+[.a-z0-9]*@[a-z]{1,}\\.[a-z]{2,}(\\.[a-z]{2,})?")) {
			throw new InvalidValueException("Invalid Email id");
		}
	}

	public static void mobile(String number) throws InvalidValueException {
		if (!number.matches("[1-9]{1}[0-9]{9}")) {
			throw new InvalidValueException("Invalid Mobile No!");
		}
	}

	public static void pan(String pan) throws InvalidValueException {
		if (!pan.matches("[A-Z]{5}[0-9]{4}[A-Z]")) {
			throw new InvalidValueException("Invalid PAN!");
		}
	}

	public static void aadhaar(String pan) throws InvalidValueException {
		if (!pan.matches("[0-9]{12}")) {
			throw new InvalidValueException("Invalid Aadhaar No!");
		}
	}
}
