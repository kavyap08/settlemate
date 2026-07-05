package com.example.demo.Expense;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepo expenseRepo;

    public Expense addExpense(Expense expense) {

        return expenseRepo.save(expense);

    }

    public List<Expense> findExpenses(int groupId) {

        return expenseRepo.findByGroupId(groupId);

    }

}