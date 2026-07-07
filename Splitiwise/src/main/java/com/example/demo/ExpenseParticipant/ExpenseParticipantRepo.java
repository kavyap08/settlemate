package com.example.demo.ExpenseParticipant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Expense.Expense;
import com.example.demo.Groupmembers.GroupMembers;

public interface ExpenseParticipantRepo
        extends JpaRepository<ExpenseParticipant, Integer>{

    List<ExpenseParticipant> findByExpense(Expense expense);
    
    List<ExpenseParticipant> findByMember(GroupMembers member);


}