package model;

public class Employee extends User {

	private static final long serialVersionUID = 1L;
	private int branchId;

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	@Override
	public String toString() {
		return "Employee [branchId=" + branchId + ", " + super.toString() + "]";
	}
}
