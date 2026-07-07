package com.example.demo.People;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.Expense.ExpenseService;
import com.example.demo.ExpenseParticipant.ExpenseParticipantService;
import com.example.demo.Groupmembers.GroupMembers;
import com.example.demo.Groupmembers.GroupMembersService;
import com.example.demo.Groups.Groups;
import com.example.demo.Groups.GroupsService;
import com.example.demo.User.User;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class PeopleController {
	

	@Autowired
	private ExpenseParticipantService expenseParticipantService;


    @Autowired
    private GroupsService groupsService;

    @Autowired
    private GroupMembersService groupMembersService;

    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/people")
    public String people(HttpServletRequest request, Model model) {

        User user =
                (User) request.getSession()
                .getAttribute("sessionUser");

        if(user == null){

            return "redirect:/login";

        }

        int groupId =
                Integer.parseInt(
                        request.getParameter("groupId"));

        Groups group =
                groupsService.findGroup(groupId);

        List<GroupMembers> members =
                groupMembersService.getMembersByGroup(group);
        
        List<PeopleSummary> people = new ArrayList<>();

        for(GroupMembers member : members){

            double paid =
                    expenseService.getPaidByMember(
                            member.getEmail(),
                            groupId
                    );

            double share =
                    expenseParticipantService.getShareByMember(
                            member
                    );

            PeopleSummary person =
                    new PeopleSummary();
            person.setCurrentUser(
                    member.getEmail().equals(user.getEmail())
            );

            person.setName(member.getName());

            person.setEmail(member.getEmail());

            person.setPaid(paid);

            person.setShare(share);

            person.setBalance(paid - share);

            people.add(person);

        }
        model.addAttribute("recentExpenses",
                expenseService.latestExpenses(group));
        model.addAttribute("people", people);
        model.addAttribute("user", user);
        model.addAttribute("groupSelected", group);
        model.addAttribute("groups", groupsService.findAllGroups());
        model.addAttribute("members", members);

        return "people";

    }

}