package com.example.demo.Groupmembers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Groups.Groups;

@Service
public class GroupMembersService {

    @Autowired
    private GroupMembersRepo groupMembersRepo;


    public void addMember(GroupMembers member){

        groupMembersRepo.save(member);

    }


    public List<GroupMembers> getMembersByGroup(Groups group){

        return groupMembersRepo.findByGroup(group);

    }

}