package model;

import java.time.LocalDateTime;
import java.time.ZoneId;

import utility.TransactionType;
import utility.Utils;

public class Transaction {
	private String id;
	private TransactionType type;
	private double amount;
	private int primaryAccount;
	private int transactionalAccount;
	private long timestamp;
	private String description;
	private int customerId;
	private double balance;
	private String ifsc;

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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getPrimaryAccount() {
		return primaryAccount;
	}

	public void setPrimaryAccount(int primaryAccount) {
		this.primaryAccount = primaryAccount;
	}

	public int getTransactionalAccount() {
		return transactionalAccount;
	}

	public void setTransactionalAccount(int transactionalAccount) {
		this.transactionalAccount = transactionalAccount;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getIfsc() {
		return ifsc;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}

	@Override
	public String toString() {
		LocalDateTime time = Utils.millisToLocalDateTime(timestamp, ZoneId.systemDefault());
		return "Transaction [id=" + id + ", type=" + type + ", amount=" + amount + ", primaryAccount=" + primaryAccount
				+ ", transactionalAccount=" + transactionalAccount + ", timestamp=" + time + ", description="
				+ description + ", customerId=" + customerId + ", balance=" + balance + ", ifsc=" + ifsc + "]";
	}

}
