package com.example.demo.Settlement;

import java.util.ArrayList;
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
import com.example.demo.User.User;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SettlementController {
	
	 @Autowired
	    private SettlementService settlementService;

	    @Autowired
	    private GroupsService groupsService;

	    @Autowired
	    private GroupMembersService groupMembersService;

	    @Autowired
	    private ExpenseService expenseService;

	    @Autowired
	    private ExpenseParticipantService expenseParticipantService;

	    @GetMapping("/settle-up")
	    public String settleUp(HttpServletRequest request, Model model) {

	        User user = (User) request.getSession().getAttribute("sessionUser");

	        if (user == null) {
	            return "redirect:/login";
	        }

	        int groupId = Integer.parseInt(request.getParameter("groupId"));

	        Groups group = groupsService.findGroup(groupId);

	        List<SettlementResult> allSettlements =
	                settlementService.generateSettlements(group);

	        List<SettlementResult> pendingPayments =
	                new ArrayList<>();

	        List<SettlementResult> incomingPayments =
	                new ArrayList<>();

	        for (SettlementResult settlement : allSettlements) {

	            // Logged in user owes money
	            if (settlement.getPayer().getEmail()
	                    .equals(user.getEmail())) {

	                pendingPayments.add(settlement);

	            }

	            // Logged in user should receive money
	            if (settlement.getReceiver().getEmail()
	                    .equals(user.getEmail())) {

	                incomingPayments.add(settlement);

	            }

	        }

	        model.addAttribute("pendingPayments", pendingPayments);

	        model.addAttribute("incomingPayments", incomingPayments);
	        model.addAttribute("history",
	                settlementService.getSettlementHistory(group));

	        model.addAttribute("user", user);

	        model.addAttribute("groupSelected", group);

	        model.addAttribute("groups",
	                groupsService.getGroupsForUser(user));

	        return "settle-up";
	    }
	    
	    @PostMapping("/confirm-settlement")
	    public String confirmSettlement(HttpServletRequest request){

	        int groupId =
	                Integer.parseInt(request.getParameter("groupId"));

	        int payerId =
	                Integer.parseInt(request.getParameter("payerId"));

	        int receiverId =
	                Integer.parseInt(request.getParameter("receiverId"));

	        double amount =
	                Double.parseDouble(request.getParameter("amount"));

	        Groups group =
	                groupsService.findGroup(groupId);

	        GroupMembers payer =
	                groupMembersService.findMember(payerId);

	        GroupMembers receiver =
	                groupMembersService.findMember(receiverId);

	        settlementService.settleAmount(
	                group,
	                payer,
	                receiver,
	                amount
	        );

	        return "redirect:/settle-up?groupId=" + groupId;

	    }

}