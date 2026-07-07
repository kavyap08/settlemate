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
    public GroupMembers findMember(int id){

        return groupMembersRepo.findById(id).orElse(null);

    }
    public int countMembers(Groups group){

        return groupMembersRepo.findByGroup(group).size();

    }
    public GroupMembers findMemberByName(Groups group, String name){

        List<GroupMembers> members = groupMembersRepo.findByGroup(group);

        for(GroupMembers member : members){

            if(member.getName().equals(name)){

                return member;

            }

        }

        return null;

    }
    public GroupMembers findMemberByEmail(Groups group, String email){

        List<GroupMembers> members = groupMembersRepo.findByGroup(group);

        for(GroupMembers member : members){

            if(member.getEmail().equals(email)){

                return member;

            }

        }

        return null;

    }
    

}