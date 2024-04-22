package logicallayer;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import customexceptions.CustomException;
import customexceptions.InvalidOperationException;
import customexceptions.InvalidValueException;
import model.Audit;
import model.Token;
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

	public Token generateToken(int userId, int accessLevel) throws CustomException {
		String tokenString = createToken();
		Token token = new Token();
		token.setUserId(userId);
		token.setAccessLevel(accessLevel);
		token.setToken(tokenString);
		long validTill = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1);
		token.setValidTill(validTill);
		sessionManager.generateToken(token);
		return token;
	}

	public Token getToken(String token) throws CustomException, InvalidValueException {
		return sessionManager.getToken(token);
	}

	public Token getToken(int userId) throws CustomException, InvalidValueException {
		return sessionManager.getToken(userId);
	}

	private static String createToken() {
		StringBuffer sb = new StringBuffer();
		String key = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
		int size = key.length();
		Random random = new Random();
		for (int i = 0; i < 40; i++) {
			int index = random.nextInt(size);
			sb.append(key.charAt(index));
		}
		return sb.toString();
	}

	public void removeToken(String userToken) throws CustomException {
		sessionManager.removeToken(userToken);
	}

}
