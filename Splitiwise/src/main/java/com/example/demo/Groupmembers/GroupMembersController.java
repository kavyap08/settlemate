package com.example.demo.Groupmembers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class GroupMembersController {

    @Autowired
    private GroupMembersService groupMembersService;

}