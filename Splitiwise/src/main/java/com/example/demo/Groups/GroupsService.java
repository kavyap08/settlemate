package com.example.demo.Groups;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupsService {

    @Autowired
    private GroupsRepo groupsRepo;

    public void addGroup(Groups group){

        groupsRepo.save(group);

    }

    public List<Groups> findAllGroups(){

        return groupsRepo.findAll();

    }
    public Groups findGroup(int id) {

        return groupsRepo.findById(id).orElse(null);

    }

}