package persistentdao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import customexceptions.CustomException;

public class HelperDao {

	public static <T> List<Object> buildQuery(T existingPojo, T updatedPojo) throws CustomException {
		try {
			StringBuilder sb = new StringBuilder();
			Class<?> clazz = existingPojo.getClass();
			Method[] methods = clazz.getMethods();
			ArrayList<Object> values = new ArrayList<>();
			for (Method method : methods) {
				String name = method.getName();
				if (name.startsWith("get") && !name.equals("getClass")) {
					Object existingValue = method.invoke(existingPojo);
					Object updatedValue = method.invoke(updatedPojo);
					if (updatedValue != null && !updatedValue.equals(existingValue)) {
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
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new CustomException("Updation Failed!", e);
		}
	}

}
