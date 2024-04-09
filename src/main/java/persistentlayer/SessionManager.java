package persistentlayer;

import customexceptions.CustomException;
import customexceptions.InvalidOperationException;
import customexceptions.InvalidValueException;
import model.Audit;
import model.User;

public interface SessionManager {

	User authenticate(int userId, String password)
			throws CustomException, InvalidValueException, InvalidOperationException;

	void audit(Audit audit) throws CustomException;

}
