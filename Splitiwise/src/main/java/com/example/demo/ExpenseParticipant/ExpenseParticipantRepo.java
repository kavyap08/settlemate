package com.example.demo.ExpenseParticipant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Expense.Expense;
import com.example.demo.Groupmembers.GroupMembers;
import com.example.demo.Groups.Groups;

public interface ExpenseParticipantRepo
        extends JpaRepository<ExpenseParticipant, Integer>{

    List<ExpenseParticipant> findByExpense(Expense expense);
    
    List<ExpenseParticipant> findByMember(GroupMembers member);
    
    boolean existsByMember(GroupMembers member);
   
}