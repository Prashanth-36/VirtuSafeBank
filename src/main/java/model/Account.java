package model;

import java.io.Serializable;

import utility.ActiveStatus;

public class Account implements Serializable {
	private static final long serialVersionUID = 1L;
	private int accountNo;
	private int customerId;
	private double currentBalance;
	private boolean isPrimaryAccout;
	private long openDate;
	private int branchId;
	private ActiveStatus status;
	private String mpin;

	public int getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(int accountNo) {
		this.accountNo = accountNo;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public boolean isPrimaryAccout() {
		return isPrimaryAccout;
	}

	public void setPrimaryAccout(boolean isPrimaryAccout) {
		this.isPrimaryAccout = isPrimaryAccout;
	}

	public long getOpenDate() {
		return openDate;
	}

	public void setOpenDate(long openDate) {
		this.openDate = openDate;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public ActiveStatus getStatus() {
		return status;
	}

	public void setStatus(ActiveStatus status) {
		this.status = status;
	}

	public String getMpin() {
		return mpin;
	}

	public void setMpin(String mpin) {
		this.mpin = mpin;
	}

	@Override
	public String toString() {
		return "Account [accountNo=" + accountNo + ", customerId=" + customerId + ", currentBalance=" + currentBalance
				+ ", isPrimaryAccout=" + isPrimaryAccout + ", openDate=" + openDate + ", branchId=" + branchId
				+ ", status=" + status + ", mpin=" + mpin + "]";
	}
}
