package com.example.demo.Settlement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Groups.Groups;

public interface SettlementRepo extends JpaRepository<Settlement,Integer>{

    List<Settlement> findByGroupOrderBySettledOnDesc(Groups group);
    
   



   

}