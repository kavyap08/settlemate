package com.example.demo.User;

import java.util.List;
import com.example.demo.Groups.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

	@Autowired
	private UserService userservice;

	@Autowired
	private GroupsService groupsService;

	// LOGIN PAGE
	@GetMapping("/login")
	public String login(Model model, HttpServletRequest request) {

		User user = (User) request.getSession().getAttribute("sessionUser");

		// already logged in
		if (user != null) {
			return "redirect:/dashboard";
		}

		model.addAttribute("mode", "login");

		return "auth";
	}

	// SIGNUP PAGE
	@GetMapping("/register")
	public String register(Model model, HttpServletRequest request) {

		User user = (User) request.getSession().getAttribute("sessionUser");

		if (user != null) {
			return "redirect:/dashboard";
		}

		model.addAttribute("mode", "register");

		return "auth";
	}

	// LOGIN USER
	@PostMapping("/login-user")
	public String loginUser(Model model, HttpServletRequest request) {

		String email = request.getParameter("email");
		String password = request.getParameter("password");
		User user = userservice.findByEmail(email);

		if (user != null && user.getPassword().equals(password)) {

			HttpSession session = request.getSession();

			session.setAttribute("sessionUser", user);

			return "redirect:/dashboard";

		}

		model.addAttribute("msg", "Invalid Email or Password");
		model.addAttribute("mode", "login");

		return "auth";
	}

	// REGISTER USER
	@PostMapping("/register-user")
	public String registerUser(Model model, HttpServletRequest request) {

		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		User user = new User();

		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);

		if (userservice.existsByEmail(email)) {

			model.addAttribute("msg", "Email already registered");

			model.addAttribute("mode", "register");

			return "auth";

		}

		userservice.saveUser(user);

		model.addAttribute("msg", "Account Created Successfully");
		model.addAttribute("mode", "login");

		return "auth";
	}

	// DASHBOARD
	@GetMapping("/dashboard")
	public String dashboard(HttpServletRequest request, Model model) {

		User user = (User) request.getSession().getAttribute("sessionUser");

		if (user == null) {

			return "redirect:/login";

		}
		String error = request.getParameter("error");

		if(error != null){

		    model.addAttribute("error", error);

		}

		model.addAttribute("user", user);

		model.addAttribute("groups", groupsService.getGroupsForUser(user));

		return "dashboard";
	}

	// LOGOUT
	@PostMapping("/logout")
	public String logout(HttpServletRequest request) {

		HttpSession session = request.getSession();

		session.invalidate();

		return "redirect:/login";
	}

}