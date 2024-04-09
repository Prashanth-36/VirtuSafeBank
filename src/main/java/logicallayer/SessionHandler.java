package logicallayer;

import customexceptions.CustomException;
import customexceptions.InvalidOperationException;
import customexceptions.InvalidValueException;
import model.Audit;
import model.User;
import persistentdao.SessionDao;
import persistentlayer.SessionManager;
import utility.Utils;

public class SessionHandler {

	public User authenticate(int userId, String password)
			throws CustomException, InvalidValueException, InvalidOperationException {
		Utils.checkNull(password);
		SessionManager sessionManager = new SessionDao();
		return sessionManager.authenticate(userId, password);
	}

	public void audit(Audit audit) throws CustomException {
		SessionManager sessionManager = new SessionDao();
		sessionManager.audit(audit);
	}
}
