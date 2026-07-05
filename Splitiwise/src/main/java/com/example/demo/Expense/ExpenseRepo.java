package com.example.demo.Expense;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense, Integer> {

    List<Expense> findByGroupId(int groupId);

}