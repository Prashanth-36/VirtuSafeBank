package model;

public class Employee extends User {

	private static final Long serialVersionUID = 1L;
	private Integer branchId;

	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	@Override
	public String toString() {
		return "Employee [branchId=" + branchId + ", " + super.toString() + "]";
	}
}
