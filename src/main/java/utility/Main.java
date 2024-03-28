package utility;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Employee;
import persistentdao.DBConnection;

public class Main {
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		Employee employee = new Employee();
		employee.setUserId(5);
		employee.setName("dfghj");
		employee.setGender(Gender.MALE);

		Employee currentEmployee = new Employee();
		currentEmployee.setUserId(5);
		currentEmployee.setName("HEllo");
		currentEmployee.setGender(Gender.FEMALE);

		Connection connection = DBConnection.getConnection();
		StringBuilder sb = new StringBuilder("UPDATE user u JOIN employee e on u.id=e.id SET");
		ArrayList<Object> query = buildQuery(currentEmployee, employee);
		System.out.println(query);
		if (!query.isEmpty()) {
			sb.append(query.get(0));
			sb.append(" where id = ?");
			PreparedStatement statement = connection.prepareStatement(sb.toString());
			for (int i = 1; i < query.size(); i++) {
				statement.setObject(i, query.get(i));
			}
			statement.setInt(query.size(), 1);
			System.out.println(statement.toString());
		}

	}

	private static <T> ArrayList<Object> buildQuery(T existingPojo, T updatedPojo)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		StringBuilder sb = new StringBuilder();
		Class<?> clazz = existingPojo.getClass();
		Method[] methods = clazz.getMethods();
		ArrayList<Object> values = new ArrayList<>();
		for (Method method : methods) {
			String name = method.getName();
			if (name.startsWith("get") && !name.equals("getClass")) {
				Object existingValue = method.invoke(existingPojo);
				Object updatedValue = method.invoke(updatedPojo);
				if (updatedValue != null && existingValue != updatedValue) {
					sb.append(" " + method.getName().replaceFirst("get", "").toLowerCase() + " = ?,");
					if (updatedValue instanceof Enum<?>) {
						Enum<?> enumObj = (Enum<?>) updatedValue;
						values.add(enumObj.ordinal());
					} else {
						values.add(updatedValue);
					}
				}
			}
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
			values.add(0, sb.toString());
		}
		return values;
	}

}
