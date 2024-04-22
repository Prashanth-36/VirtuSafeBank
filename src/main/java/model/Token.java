package model;

public class Token {
	private int userId;
	private String token;
	private int accessLevel;
	private long validTill;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(int accessLevel) {
		this.accessLevel = accessLevel;
	}

	public long getValidTill() {
		return validTill;
	}

	public void setValidTill(long validTill) {
		this.validTill = validTill;
	}

	@Override
	public String toString() {
		return "Token [userId=" + userId + ", token=" + token + ", accessLevel=" + accessLevel + ", validTill="
				+ validTill + "]";
	}
}
