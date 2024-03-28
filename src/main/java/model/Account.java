package model;

import java.io.Serializable;

import utility.ActiveStatus;

public class Account implements Serializable {
	private static final Long serialVersionUID = 1L;
	private Integer accountNo;
	private Integer customerId;
	private Double currentBalance;
	private Boolean isPrimaryAccount;
	private Long openDate;
	private Integer branchId;
	private ActiveStatus status;
	private String mpin;

	public Integer getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(Integer accountNo) {
		this.accountNo = accountNo;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(Double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public boolean getIsPrimaryAccount() {
		return isPrimaryAccount;
	}

	public void setIsPrimaryAccount(boolean isPrimaryAccout) {
		this.isPrimaryAccount = isPrimaryAccout;
	}

	public Long getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Long openDate) {
		this.openDate = openDate;
	}

	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
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
				+ ", isPrimaryAccout=" + isPrimaryAccount + ", openDate=" + openDate + ", branchId=" + branchId
				+ ", status=" + status + ", mpin=" + mpin + "]";
	}
}
