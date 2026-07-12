package com.example.demo.Groupmembers;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.Groups.Groups;

@Repository
public interface GroupMembersRepo extends JpaRepository<GroupMembers,Integer>{

    List<GroupMembers> findByGroup(Groups group);
    
    List<GroupMembers> findByEmail(String email);
    
    @Query("""
    		SELECT g
    		FROM GroupMembers g
    		WHERE g.group = :group
    		AND LOWER(g.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    		""")
    		List<GroupMembers> searchMembers(Groups group, String keyword);
    
    

}