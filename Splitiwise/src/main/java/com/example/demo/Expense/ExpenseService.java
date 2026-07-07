package com.example.demo.Expense;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Groups.Groups;

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
    public Expense findExpense(int expenseId) {

        return expenseRepo.findById(expenseId).orElse(null);

    }

    public void updateExpense(Expense expense) {

        expenseRepo.save(expense);

    }
    public void deleteExpense(int expenseId) {

        expenseRepo.deleteById(expenseId);

    }
    public List<Expense> findExpensesByCategory(int groupId,
            String category){

return expenseRepo.findByGroupIdAndCategory(groupId,
                category);

}
    public double getTotalExpense(int groupId) {

        List<Expense> expenses = expenseRepo.findByGroupId(groupId);

        double total = 0;

        for (Expense expense : expenses) {

            total += expense.getAmount();

        }

        return total;

    }
    public List<Expense> latestExpenses(Groups group){

        return expenseRepo.findTop5ByGroupOrderByExpenseDateDesc(group);

    }
    public double getStayTotal(int groupId){

        return expenseRepo.getStayTotal(groupId);

    }

    public double getFoodTotal(int groupId){

        return expenseRepo.getFoodTotal(groupId);

    }

    public double getTransportTotal(int groupId){

        return expenseRepo.getTransportTotal(groupId);

    }

    public double getShoppingTotal(int groupId){

        return expenseRepo.getShoppingTotal(groupId);

    }

    public double getActivitiesTotal(int groupId){

        return expenseRepo.getActivitiesTotal(groupId);

    }

    public double getOthersTotal(int groupId){

        return expenseRepo.getOthersTotal(groupId);

    }
    public double getPaidByUser(String email, int groupId) {

        List<Expense> expenses = expenseRepo.findByGroupId(groupId);

        double total = 0;

        
        System.out.println("Logged in email = " + email);

        for (Expense expense : expenses) {

            System.out.println("Paid By = " + expense.getPaidBy());

            if (expense.getPaidBy().equals(email)) {

                total += expense.getAmount();

            }

        }

        return total;
    }
    public double getPaidByMember(String email, int groupId){

        List<Expense> expenses =
                expenseRepo.findByGroupId(groupId);

        double total = 0;

        for(Expense expense : expenses){

            if(expense.getPaidBy().equals(email)){

                total += expense.getAmount();

            }

        }

        return total;

    }
    
}