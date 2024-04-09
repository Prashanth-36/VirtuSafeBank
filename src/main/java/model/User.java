
package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;

import utility.ActiveStatus;
import utility.Gender;
import utility.UserType;
import utility.Utils;

public class User implements Serializable {

	private static final Long serialVersionUID = 1L;
	private Integer userId;
	private String name;
	private Long dob;
	private Long number;
	private ActiveStatus status;
	private String password;
	private UserType type;
	private String location;
	private String city;
	private String state;
	private String email;
	private Gender gender;
	private Long modifiedOn;
	private int modifiedBy;

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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getDob() {
		return dob;
	}

	public void setDob(Long dob) {
		this.dob = dob;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
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

	public int getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(int modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Override
	public String toString() {
		LocalDate date = Utils.millisToLocalDate(dob, ZoneId.systemDefault());
		return "User [userId=" + userId + ", name=" + name + ", dob=" + date + ", number=" + number + ", status="
				+ status + ", password=" + password + ", type=" + type + ", location=" + location + ", city=" + city
				+ ", state=" + state + ", email=" + email + ", gender=" + gender + ", modifiedOn=" + getModifiedOn()
				+ ", modifiedBy=" + modifiedBy + "]";
	}

	public Long getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Long modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
}