package com.example.demo.Groups;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.User.User;
import com.example.demo.User.UserService;
import com.example.demo.Groupmembers.*;

import com.example.demo.Expense.*;
import com.example.demo.ExpenseParticipant.ExpenseParticipantService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class GroupsController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private ExpenseParticipantService expenseParticipantService;

	@Autowired
	private GroupsService groupsService;

	@Autowired
	private GroupMembersService groupmembersService;

	@Autowired
	private ExpenseService expenseService;

	@PostMapping("/save-group")
	public String saveGroup(HttpServletRequest request) {

	    User user = (User) request.getSession().getAttribute("sessionUser");

	    if (user == null) {

	        return "redirect:/login";

	    }

	    String groupName = request.getParameter("groupName");

	    String[] names = request.getParameterValues("memberName");
	    String[] emails = request.getParameterValues("memberEmail");

	    // Validate all member emails first
	    if (emails != null) {

	        for (String email : emails) {

	            if (email != null
	                    && !email.trim().isEmpty()
	                    && !email.equalsIgnoreCase(user.getEmail())) {

	                User existingUser = userService.findByEmail(email);

	                if (existingUser == null) {

	                    return "redirect:/dashboard?error=" + email;

	                }

	            }

	        }

	    }

	    // Create group only after all emails are validated
	    Groups group = new Groups();

	    group.setGroupName(groupName);
	    
	    group.setActive(true);

	    groupsService.addGroup(group);

	    // Add creator as first member
	    GroupMembers creator = new GroupMembers();

	    creator.setName(user.getName());

	    creator.setEmail(user.getEmail());

	    creator.setGroup(group);

	    groupmembersService.addMember(creator);

	    // Add remaining members
	    if (names != null && emails != null) {

	        for (int i = 0; i < names.length; i++) {

	            if (names[i] != null
	                    && !names[i].trim().isEmpty()
	                    && !emails[i].equalsIgnoreCase(user.getEmail())) {

	                User existingUser = userService.findByEmail(emails[i]);

	                GroupMembers member = new GroupMembers();

	                member.setName(existingUser.getName());

	                member.setEmail(existingUser.getEmail());

	                member.setGroup(group);

	                groupmembersService.addMember(member);

	            }

	        }

	    }

	    return "redirect:/dashboard?success=groupCreated";

	}

	@GetMapping("/group")
	public String openGroup(HttpServletRequest request, Model model) {

		User user = (User) request.getSession().getAttribute("sessionUser");

		if (user == null) {

			return "redirect:/login";
		}

		String idValue = request.getParameter("id");

		int id = Integer.parseInt(idValue);

		Groups group = groupsService.findGroup(id);
		
		if(group == null || !group.isActive()){

		    return "redirect:/dashboard";

		}

		model.addAttribute("user", user);
		model.addAttribute("groupSelected", group);
		model.addAttribute("groups", groupsService.getGroupsForUser(user));
		model.addAttribute("totalExpense", expenseService.getTotalExpense(group.getId()));

		model.addAttribute("memberCount", groupmembersService.countMembers(group));
		model.addAttribute("recentExpenses", expenseService.latestExpenses(group));
		model.addAttribute("stayTotal", expenseService.getStayTotal(group.getId()));

		model.addAttribute("foodTotal", expenseService.getFoodTotal(group.getId()));

		model.addAttribute("transportTotal", expenseService.getTransportTotal(group.getId()));

		model.addAttribute("shoppingTotal", expenseService.getShoppingTotal(group.getId()));

		model.addAttribute("activitiesTotal", expenseService.getActivitiesTotal(group.getId()));

		model.addAttribute("othersTotal", expenseService.getOthersTotal(group.getId()));

		GroupMembers member = groupmembersService.findMemberByEmail(group, user.getEmail());

		double paid = expenseService.getPaidByUser(user.getEmail(), group.getId());

		double share = expenseParticipantService.getTotalShare(member);

		double balance = paid - share;

		model.addAttribute("balance", balance);

		if (balance >= 0) {

			model.addAttribute("youAreOwed", balance);
			model.addAttribute("youOwe", 0);

		} else {

			model.addAttribute("youAreOwed", 0);
			model.addAttribute("youOwe", Math.abs(balance));

		}

		model.addAttribute("paid", paid);
		model.addAttribute("share", share);
		System.out.println("Paid = " + paid);
		System.out.println("Share = " + share);
		System.out.println("Balance = " + balance);
		return "dashboard";
	}
	
	@PostMapping("/delete-group")
	public String deleteGroup(HttpServletRequest request){

	    int groupId =
	            Integer.parseInt(request.getParameter("groupId"));

	    groupsService.deleteGroup(groupId);

	    return "redirect:/dashboard";

	}

}