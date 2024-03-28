package model;

public class Customer extends User {
	private static final Long serialVersionUID = 1L;
	private Long aadhaarNo;
	private String panNo;

	public Long getAadhaarNo() {
		return aadhaarNo;
	}

	public void setAadhaarNo(Long aadhaarNo) {
		this.aadhaarNo = aadhaarNo;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	@Override
	public String toString() {
		return "Customer [aadhaarNo=" + aadhaarNo + ", panNo=" + panNo + ", " + super.toString() + "]";
	}

}
