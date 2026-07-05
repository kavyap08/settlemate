package com.example.demo.Groups;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupsRepo extends JpaRepository<Groups,Integer>{

    List<Groups> findAll();

}