package com.example.demo.Groupmembers;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Groups.Groups;

@Repository
public interface GroupMembersRepo extends JpaRepository<GroupMembers,Integer>{

    List<GroupMembers> findByGroup(Groups group);

}