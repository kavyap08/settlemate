package com.example.demo.Expense;

import java.time.LocalDate;

import com.example.demo.ExpenseParticipant.ExpenseParticipant;
import com.example.demo.ExpenseParticipant.ExpenseParticipantService;
import com.example.demo.Groupmembers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.Groups.Groups;
import com.example.demo.Groups.GroupsService;
import com.example.demo.User.User;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ExpenseController {

	@Autowired
	private ExpenseService expenseService;
	
	@Autowired
	private ExpenseParticipantService expenseParticipantService;

	@Autowired
	private GroupsService groupsService;

	@Autowired
	private GroupMembersService groupMembersService;

	@GetMapping("/expenses")
	public String expenses(HttpServletRequest request, Model model) {

		User user = (User) request.getSession().getAttribute("sessionUser");

		if (user == null) {

			return "redirect:/login";
		}

		String idValue = request.getParameter("groupId");

		int groupId = Integer.parseInt(idValue);
		String category = request.getParameter("category");

		if (category == null || category.isBlank()) {

			model.addAttribute("expenses", expenseService.findExpenses(groupId));

		} else {

			model.addAttribute("expenses", expenseService.findExpensesByCategory(groupId, category));

		}
		Groups group = groupsService.findGroup(groupId);

		model.addAttribute("user", user);
		model.addAttribute("groupSelected", group);
		model.addAttribute("groups",
		        groupsService.getGroupsForUser(user));

		model.addAttribute("members", groupMembersService.getMembersByGroup(group));
		return "expenses";
	}

	@PostMapping("/save-expense")
	public String saveExpense(HttpServletRequest request) {

		User user = (User) request.getSession().getAttribute("sessionUser");

		if (user == null) {

			return "redirect:/login";
		}

		int groupId = Integer.parseInt(request.getParameter("groupId"));

		Groups group = groupsService.findGroup(groupId);

		Expense expense = new Expense();

		expense.setExpenseName(request.getParameter("expenseName"));

		expense.setAmount(Double.parseDouble(request.getParameter("amount")));

		expense.setPaidBy(request.getParameter("paidBy"));

		expense.setCategory(request.getParameter("category"));

		expense.setExpenseDate(LocalDate.now());

		expense.setGroup(group);

		expenseService.addExpense(expense);
		String[] selectedMembers =
		        request.getParameterValues("splitMembers");

		if(selectedMembers != null){

		    double share =
		            expense.getAmount() / selectedMembers.length;

		    for(String memberId : selectedMembers){

		        GroupMembers member =
		                groupMembersService.findMember(
		                        Integer.parseInt(memberId));

				ExpenseParticipant participant =new ExpenseParticipant();

		        participant.setExpense(expense);

		        participant.setMember(member);

		        participant.setShare(share);

		        expenseParticipantService.addParticipant(participant);

		    }

		}

		return "redirect:/expenses?groupId=" + groupId + "&success=added";
	}

	@GetMapping("/edit-expense")
	public String editExpense(HttpServletRequest request, Model model) {

		User user = (User) request.getSession().getAttribute("sessionUser");

		if (user == null) {

			return "redirect:/login";

		}

		int expenseId = Integer.parseInt(request.getParameter("expenseId"));

		Expense expense = expenseService.findExpense(expenseId);

		if (expense == null) {

			return "redirect:/dashboard";

		}

		Groups group = expense.getGroup();

		model.addAttribute("expense", expense);
		model.addAttribute("groupSelected", group);
		model.addAttribute("groups", groupsService.findAllGroups());
		model.addAttribute("members", groupMembersService.getMembersByGroup(group));
		model.addAttribute("user", user);

		return "edit-expense";

	}

	@PostMapping("/update-expense")
	public String updateExpense(HttpServletRequest request) {

		User user = (User) request.getSession().getAttribute("sessionUser");

		if (user == null) {

			return "redirect:/login";

		}

		int expenseId = Integer.parseInt(request.getParameter("expenseId"));

		Expense expense = expenseService.findExpense(expenseId);

		expense.setExpenseName(request.getParameter("expenseName"));

		expense.setAmount(Double.parseDouble(request.getParameter("amount")));

		expense.setPaidBy(request.getParameter("paidBy"));

		expense.setCategory(request.getParameter("category"));

		expenseService.updateExpense(expense);

		return "redirect:/expenses?groupId=" + expense.getGroup().getId();

	}

	@PostMapping("/delete-expense")
	public String deleteExpense(HttpServletRequest request) {

		User user = (User) request.getSession().getAttribute("sessionUser");

		if (user == null) {

			return "redirect:/login";

		}

		int expenseId = Integer.parseInt(request.getParameter("expenseId"));

		Expense expense = expenseService.findExpense(expenseId);

		if (expense == null) {

			return "redirect:/dashboard";

		}

		int groupId = expense.getGroup().getId();

		expenseService.deleteExpense(expenseId);

		return "redirect:/expenses?groupId=" + groupId;

	}
}