package com.example.demo.Settlement;

import com.example.demo.Groupmembers.GroupMembers;

public class SettlementResult {

    private GroupMembers payer;
    private GroupMembers receiver;
    private double amount;
	public GroupMembers getPayer() {
		return payer;
	}
	public void setPayer(GroupMembers payer) {
		this.payer = payer;
	}
	public GroupMembers getReceiver() {
		return receiver;
	}
	public void setReceiver(GroupMembers receiver) {
		this.receiver = receiver;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}

    // Getters and Setters
}