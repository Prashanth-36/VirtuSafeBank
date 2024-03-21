package model;

public class Customer extends User {
	private static final long serialVersionUID = 1L;
	private long aadhaarNo;
	private String panNo;

	public long getAadhaarNo() {
		return aadhaarNo;
	}

	public void setAadhaarNo(long aadhaarNo) {
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
