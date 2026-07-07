package com.example.demo.ExpenseParticipant;

import com.example.demo.Expense.Expense;
import com.example.demo.Groupmembers.GroupMembers;

import jakarta.persistence.*;

@Entity
@Table(name = "expense_participant")
public class ExpenseParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "expense_id")
    private Expense expense;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private GroupMembers member;

    private double share;
    private boolean settled = false;
    
    

	public boolean isSettled() {
		return settled;
	}

	public void setSettled(boolean settled) {
		this.settled = settled;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Expense getExpense() {
		return expense;
	}

	public void setExpense(Expense expense) {
		this.expense = expense;
	}

	public GroupMembers getMember() {
		return member;
	}

	public void setMember(GroupMembers member) {
		this.member = member;
	}

	public double getShare() {
		return share;
	}

	public void setShare(double share) {
		this.share = share;
	}
    
}