
package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;

import utility.ActiveStatus;
import utility.Gender;
import utility.UserType;
import utility.Utils;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	private int userId;
	private String name;
	private long dob;
	private long number;
	private ActiveStatus status;
	private String password;
	private UserType type;
	private String location;
	private String city;
	private String state;
	private String email;
	private Gender gender;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getDob() {
		return dob;
	}

	public void setDob(long dob) {
		this.dob = dob;
	}

	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
	}

	public ActiveStatus getStatus() {
		return status;
	}

	public void setStatus(ActiveStatus active) {
		this.status = active;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		LocalDate date = Utils.millisToLocalDate(dob, ZoneId.systemDefault());

		return "User [userId=" + userId + ", name=" + name + ", dob=" + date + ", number=" + number + ", status="
				+ status + ", password=" + password + ", type=" + type + ", location=" + location + ", city=" + city
				+ ", state=" + state + ", email=" + email + ", gender=" + gender + "]";
	}
}
