package persistentlayer;

import customexceptions.CustomException;
import customexceptions.InvalidOperationException;
import customexceptions.InvalidValueException;
import model.Audit;
import model.Token;
import model.User;

public interface SessionManager {

	User authenticate(int userId, String password)
			throws CustomException, InvalidValueException, InvalidOperationException;

	void audit(Audit audit) throws CustomException;

	void generateToken(Token token) throws CustomException;

	Token getToken(String token) throws CustomException, InvalidValueException;

	Token getToken(int userId) throws CustomException, InvalidValueException;

	void removeToken(String userToken) throws CustomException;
}
