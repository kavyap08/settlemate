package com.example.demo.Groups;

import java.util.List;
import java.util.ArrayList;


import com.example.demo.Groupmembers.GroupMembers;
import com.example.demo.Groupmembers.GroupMembersRepo;
import com.example.demo.User.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupsService {

    @Autowired
    private GroupsRepo groupsRepo;
    
    @Autowired
    private GroupMembersRepo groupMembersRepo;

    public void addGroup(Groups group){

        groupsRepo.save(group);

    }

    public List<Groups> findAllGroups(){

        return groupsRepo.findAll();

    }
    public Groups findGroup(int id) {

        return groupsRepo.findById(id).orElse(null);

    }
    public List<Groups> getGroupsForUser(User user) {

        List<GroupMembers> memberships =
                groupMembersRepo.findByEmail(user.getEmail());

        List<Groups> groups = new ArrayList<>();

        for(GroupMembers member : memberships){

            Groups group = member.getGroup();

            if(group.isActive() && !groups.contains(group)){

                groups.add(group);

            }

        }

        return groups;

    }
    public void deleteGroup(int groupId){

        Groups group = findGroup(groupId);

        if(group != null){

            group.setActive(false);

            groupsRepo.save(group);

        }

    }

}