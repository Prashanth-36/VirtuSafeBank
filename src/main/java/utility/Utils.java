package utility;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import customexceptions.InvalidValueException;

public class Utils {

	public final static long MONTH_MILLIS = 1000l * 60 * 60 * 24 * 30;

	public static void checkNull(Object obj) throws InvalidValueException {
		if (obj == null) {
			throw new InvalidValueException("Input cannot be null.");
		}
	}

	public static LocalDate millisToLocalDate(long millis, ZoneId zoneId) {
		return millisToLocalDateTime(millis, zoneId).toLocalDate();
	}

	public static LocalDateTime millisToLocalDateTime(long millis, ZoneId zoneId) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), zoneId);
	}

	public static long getMillis(LocalDate date) {
		return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	public static void checkRange(int min, int value, int max, String message) throws InvalidValueException {
		if (value < min || value > max) {
			throw new InvalidValueException(message);
		}
	}

	public static void checkRange(int min, int value, int max) throws InvalidValueException {
		checkRange(min, value, max, "Invalid input given value is not within acceptable range");
	}

	public static Logger customLogger(Logger logger, String fileName) {
		try {
			FileHandler handler = new FileHandler(fileName);
			handler.setFormatter(new SimpleFormatter());
			logger.addHandler(handler);
			ConsoleHandler handler2 = new ConsoleHandler();
			handler2.setFormatter(new SimpleFormatter() {
//				@Override
//				public String format(LogRecord record) {
//					return record.getMessage() + System.lineSeparator();
//				}
				@Override
				public String format(LogRecord record) {
					StringBuilder message = new StringBuilder();
					String colorCode;
					if (record.getLevel() == Level.INFO) {
						colorCode = "\u001B[32m"; // Green
					} else if (record.getLevel() == Level.WARNING) {
						colorCode = "\u001B[33m"; // Yellow
					} else {
						colorCode = "\u001B[31m"; // Red
					}

					message.append(colorCode).append(record.getMessage()).append("\u001B[0m")
							.append(System.lineSeparator()); // Reset color

					Throwable thrown = record.getThrown();
					if (thrown != null) {
						message.append(System.lineSeparator()).append("Exception: ");
						message.append(thrown.toString());
						for (StackTraceElement element : thrown.getStackTrace()) {
							message.append(System.lineSeparator()).append("\t").append(element.toString());
						}
						thrown = thrown.getCause();
					}
					while (thrown != null) {
						message.append(System.lineSeparator()).append("Caused by: ");
						message.append(thrown.toString());
						for (StackTraceElement element : thrown.getStackTrace()) {
							message.append(System.lineSeparator()).append("\t").append(element.toString());
						}
						thrown = thrown.getCause();
					}

					return message.toString();
				}

			});
			logger.addHandler(handler2);
		} catch (SecurityException | IOException e) {
			logger.log(Level.SEVERE, "File Handler exception", e);
		}
		logger.setUseParentHandlers(false);
		return logger;
	}

	public static int pagination(int pageNo, int limit) throws InvalidValueException {
		if (pageNo < 1) {
			throw new InvalidValueException("Page number should be positive int greater than 0");
		}
		Utils.checkRange(5, limit, 50, "Limit should be within 5 to 50.");
		int offset = (pageNo - 1) * limit;
		return offset;
	}

	public static String hashPassword(String password) throws InvalidValueException {
		checkNull(password);
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new InvalidValueException("Invalid hashing algorithm", e);
		}
		byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
		BigInteger number = new BigInteger(1, hash);
		StringBuilder hexString = new StringBuilder(number.toString(16));
		while (hexString.length() < 32) {
			hexString.insert(0, '0');
		}
		return hexString.toString();
	}
}
