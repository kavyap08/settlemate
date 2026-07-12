package com.example.demo.Settlement;

import java.time.LocalDateTime;

import com.example.demo.Groups.Groups;
import com.example.demo.Groupmembers.GroupMembers;

import jakarta.persistence.*;

@Entity
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Groups group;

    @ManyToOne
    @JoinColumn(name = "payer_id")
    private GroupMembers payer;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private GroupMembers receiver;

    private double amount;

    private LocalDateTime settledOn;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Groups getGroup() {
		return group;
	}

	public void setGroup(Groups group) {
		this.group = group;
	}

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

	public LocalDateTime getSettledOn() {
		return settledOn;
	}

	public void setSettledOn(LocalDateTime settledOn) {
		this.settledOn = settledOn;
	}
      
    
}