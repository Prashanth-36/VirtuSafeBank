package logicallayer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import customexceptions.CustomException;
import customexceptions.InvalidOperationException;
import customexceptions.InvalidValueException;
import model.Audit;
import model.User;
import persistentdao.SessionDao;
import persistentlayer.SessionManager;
import utility.Utils;

public class SessionHandler {

	ExecutorService executorService = Executors.newCachedThreadPool();
	SessionManager sessionManager = new SessionDao();

	public User authenticate(int userId, String password)
			throws CustomException, InvalidValueException, InvalidOperationException {
		Utils.checkNull(password);
		return sessionManager.authenticate(userId, password);
	}

	public void audit(Audit audit) throws CustomException {
		executorService.execute(() -> {
			try {
				sessionManager.audit(audit);
			} catch (CustomException e) {
				e.printStackTrace();
			}
		});
	}
}
