package com.example.demo.People;

import java.util.ArrayList;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.Expense.ExpenseService;
import com.example.demo.ExpenseParticipant.ExpenseParticipantService;
import com.example.demo.Groupmembers.GroupMembers;
import com.example.demo.Groupmembers.GroupMembersService;
import com.example.demo.Groups.Groups;
import com.example.demo.Groups.GroupsService;
import com.example.demo.Settlement.SettlementService;
import com.example.demo.User.User;
import com.example.demo.User.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class PeopleController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SettlementService settlementService;

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

		User user = (User) request.getSession().getAttribute("sessionUser");

		if (user == null) {

			return "redirect:/login";

		}

		int groupId = Integer.parseInt(request.getParameter("groupId"));

		Groups group = groupsService.findGroup(groupId);
		
		String error = request.getParameter("error");

		if(error != null){

		    model.addAttribute("error", error);

		}

		String keyword = request.getParameter("keyword");

		List<GroupMembers> members;

		if(keyword == null || keyword.isBlank()){

		    members = groupMembersService.getMembersByGroup(group);

		}else{

		    members = groupMembersService.searchMembers(group, keyword);

		}

		model.addAttribute("keyword", keyword);
		System.out.println("Total Members = " + members.size());
		
		for (GroupMembers m : members) {

			System.out.println(m.getId() + " | " + m.getName() + " | " + m.getEmail());

		}
		Map<GroupMembers, Double> balances =
		        settlementService.getMemberBalances(group);
		
		
		List<PeopleSummary> people = new ArrayList<>();
			

		for (GroupMembers member : members) {

			double paid = expenseService.getPaidByMember(member.getEmail(), groupId);

			double share = expenseParticipantService.getShareByMember(member);

			PeopleSummary person = new PeopleSummary();
			person.setCurrentUser(member.getEmail().equals(user.getEmail()));

			person.setName(member.getName());

			person.setEmail(member.getEmail());

			person.setId(member.getId());

			person.setPaid(paid);

			person.setShare(share);

			person.setBalance(balances.get(member));

			people.add(person);

		}
		model.addAttribute("recentExpenses", expenseService.latestExpenses(group));
		model.addAttribute("people", people);
		model.addAttribute("user", user);
		model.addAttribute("groupSelected", group);
		model.addAttribute("groups",
		        groupsService.getGroupsForUser(user));
		model.addAttribute("members", members);

		return "people";

	}

	@PostMapping("/add-member")
	public String addMember(HttpServletRequest request) {

	    int groupId =
	            Integer.parseInt(request.getParameter("groupId"));

	    Groups group =
	            groupsService.findGroup(groupId);

	    String email = request.getParameter("email");

	    User existingUser =
	            userService.findByEmail(email);

	    if(existingUser == null){

	        return "redirect:/people?groupId="
	                + groupId
	                + "&error=userNotFound";

	    }
	    GroupMembers alreadyExists =
	            groupMembersService.findMemberByEmail(group, email);

	    if(alreadyExists != null){

	        return "redirect:/people?groupId="
	                + groupId
	                + "&error=memberExists";

	    }

	    GroupMembers member = new GroupMembers();

	    member.setName(existingUser.getName());

	    member.setEmail(existingUser.getEmail());

	    member.setGroup(group);

	    groupMembersService.addMember(member);

	    return "redirect:/people?groupId=" + groupId;

	}
	@PostMapping("/edit-member")
	public String editMember(HttpServletRequest request) {

		int memberId = Integer.parseInt(request.getParameter("memberId"));

		int groupId = Integer.parseInt(request.getParameter("groupId"));

		GroupMembers member = groupMembersService.findMember(memberId);

		member.setName(request.getParameter("name"));

		member.setEmail(request.getParameter("email"));

		groupMembersService.updateMember(member);

		return "redirect:/people?groupId=" + groupId;

	}

	@PostMapping("/delete-member")
	public String deleteMember(HttpServletRequest request, RedirectAttributes redirectAttributes) {

		int memberId = Integer.parseInt(request.getParameter("memberId"));

		int groupId = Integer.parseInt(request.getParameter("groupId"));

		GroupMembers member = groupMembersService.findMember(memberId);

		if (!groupMembersService.canDeleteMember(member)) {

			redirectAttributes.addFlashAttribute("error", "Member cannot be deleted because they have expenses.");

			return "redirect:/people?groupId=" + groupId;

		}

		groupMembersService.deleteMember(memberId);

		redirectAttributes.addFlashAttribute("success", "Member deleted successfully.");

		return "redirect:/people?groupId=" + groupId;

	}

}