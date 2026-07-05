package com.example.demo.Groups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.User.User;
import com.example.demo.Groupmembers.*;


import jakarta.servlet.http.HttpServletRequest;

@Controller
public class GroupsController {

    @Autowired
    private GroupsService groupsService;
    
    @Autowired
    private GroupMembersService groupmembersService;
    
    

    @PostMapping("/save-group")
    public String saveGroup(HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("sessionUser");

        if(user == null) {

            return "redirect:/login";
        }

        String groupName = request.getParameter("groupName");

        Groups group = new Groups();

        group.setGroupName(groupName);

        // Save group first
         groupsService.addGroup(group);

        // Get all members
        String[] names = request.getParameterValues("memberName");
        String[] emails = request.getParameterValues("memberEmail");

        if(names != null && emails != null) {

            for(int i = 0; i < names.length; i++) {

                if(names[i] != null && !names[i].trim().isEmpty()) {

                    GroupMembers member = new GroupMembers();

                    member.setName(names[i]);

                    member.setEmail(emails[i]);

                    member.setGroup(group);

                    groupmembersService.addMember(member);

                }

            }

        }

        return "redirect:/dashboard?success=groupCreated";
    }
    
    @GetMapping("/group")
    public String openGroup(HttpServletRequest request,
                            Model model) {

        User user = (User) request.getSession().getAttribute("sessionUser");

        if(user == null) {

            return "redirect:/login";
        }

        String idValue = request.getParameter("id");

        int id = Integer.parseInt(idValue);

        Groups group = groupsService.findGroup(id);

        model.addAttribute("user", user);
        model.addAttribute("groupSelected", group);
        model.addAttribute("groups",
                groupsService.findAllGroups());

        return "dashboard";
    }

}