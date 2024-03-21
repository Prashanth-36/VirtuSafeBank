package utility;

public class Validate {

	public static boolean email(String email) {
		return email.matches("[a-z]+[.a-z0-9]*@[a-z]{1,}\\.[a-z]{2,}(\\.[a-z]{2,})?");
	}

	public static boolean mobile(String number) {
		return number.matches("[1-9]{1}[0-9]{9}");
	}

	public static boolean pan(String pan) {
		return pan.matches("[A-Z]{5}[0-9]{4}[A-Z]");
	}
}
