package com.example.demo.Expense;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.Groups.Groups;
import com.example.demo.Groups.GroupsService;
import com.example.demo.User.User;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private GroupsService groupsService;

    @GetMapping("/expenses")
    public String expenses(HttpServletRequest request, Model model) {

        User user = (User) request.getSession().getAttribute("sessionUser");

        if(user == null) {

            return "redirect:/login";
        }

        String idValue = request.getParameter("groupId");

        int groupId = Integer.parseInt(idValue);

        Groups group = groupsService.findGroup(groupId);

        model.addAttribute("user", user);
        model.addAttribute("groupSelected", group);
        model.addAttribute("groups", groupsService.findAllGroups());
        model.addAttribute("expenses", expenseService.findExpenses(groupId));

        return "expenses";
    }

}