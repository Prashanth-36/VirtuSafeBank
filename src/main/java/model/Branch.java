package model;

import utility.ActiveStatus;

public class Branch {
	private Integer id;
	private String ifsc;
	private String location;
	private String city;
	private String state;
	private ActiveStatus status;
	private Long modifiedOn;
	private int modifiedBy;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIfsc() {
		return ifsc;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}

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

	public ActiveStatus getStatus() {
		return status;
	}

	public void setStatus(ActiveStatus status) {
		this.status = status;
	}

	public int getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(int modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Long getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Long modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	@Override
	public String toString() {
		return "Branch [id=" + id + ", ifsc=" + ifsc + ", location=" + location + ", city=" + city + ", state=" + state
				+ ", status=" + status + ", modifiedOn=" + modifiedOn + ", modifiedBy=" + modifiedBy + "]";
	}

}