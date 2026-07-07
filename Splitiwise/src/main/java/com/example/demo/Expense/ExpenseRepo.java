package com.example.demo.Expense;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.Groups.Groups;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense, Integer> {

    List<Expense> findByGroupId(int groupId);
    
    List<Expense> findByGroupIdAndCategory(int groupId, String category);
    
    List<Expense> findTop5ByGroupOrderByExpenseDateDesc(Groups group);
    
    @Query("SELECT COALESCE(SUM(e.amount),0) FROM Expense e WHERE e.group.id=:groupId AND e.category='Stay'")
    double getStayTotal(@Param("groupId") int groupId);

    @Query("SELECT COALESCE(SUM(e.amount),0) FROM Expense e WHERE e.group.id=:groupId AND e.category='Food'")
    double getFoodTotal(@Param("groupId") int groupId);

    @Query("SELECT COALESCE(SUM(e.amount),0) FROM Expense e WHERE e.group.id=:groupId AND e.category='Transport'")
    double getTransportTotal(@Param("groupId") int groupId);

    @Query("SELECT COALESCE(SUM(e.amount),0) FROM Expense e WHERE e.group.id=:groupId AND e.category='Shopping'")
    double getShoppingTotal(@Param("groupId") int groupId);

    @Query("SELECT COALESCE(SUM(e.amount),0) FROM Expense e WHERE e.group.id=:groupId AND e.category='Activities'")
    double getActivitiesTotal(@Param("groupId") int groupId);

    @Query("SELECT COALESCE(SUM(e.amount),0) FROM Expense e WHERE e.group.id=:groupId AND e.category='Others'")
    double getOthersTotal(@Param("groupId") int groupId);
}