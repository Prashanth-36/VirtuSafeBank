package model;

import java.time.LocalDateTime;
import java.time.ZoneId;

import utility.TransactionType;
import utility.Utils;

public class Transaction {
	private String id;
	private TransactionType type;
	private Double amount;
	private Integer primaryAccount;
	private Integer transactionalAccount;
	private Long timestamp;
	private String description;
	private Integer customerId;
	private Double balance;
	private String ifsc;
	private Long modifiedOn;
	private int modifiedBy;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getPrimaryAccount() {
		return primaryAccount;
	}

	public void setPrimaryAccount(Integer primaryAccount) {
		this.primaryAccount = primaryAccount;
	}

	public Integer getTransactionalAccount() {
		return transactionalAccount;
	}

	public void setTransactionalAccount(Integer transactionalAccount) {
		this.transactionalAccount = transactionalAccount;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getIfsc() {
		return ifsc;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
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
		LocalDateTime time = Utils.millisToLocalDateTime(timestamp, ZoneId.systemDefault());
		return "Transaction [id=" + id + ", type=" + type + ", amount=" + amount + ", primaryAccount=" + primaryAccount
				+ ", transactionalAccount=" + transactionalAccount + ", timestamp=" + time + ", description="
				+ description + ", customerId=" + customerId + ", balance=" + balance + ", ifsc=" + ifsc
				+ ", modifiedOn=" + modifiedOn + ", modifiedBy=" + modifiedBy + "]";
	}
}