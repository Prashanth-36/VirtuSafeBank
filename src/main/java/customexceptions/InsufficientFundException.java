package customexceptions;

public class InsufficientFundException extends Exception {

	private static final long serialVersionUID = 1L;

	public InsufficientFundException() {
		super();
	}

	public InsufficientFundException(String message) {
		super(message);
	}

	public InsufficientFundException(Throwable cause) {
		super(cause);
	}

	public InsufficientFundException(String message, Throwable cause) {
		super(message, cause);
	}

}