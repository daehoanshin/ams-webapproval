package com.abl.web;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author dhshin
 *
 */
@Controller
public class WorkItemStateController {

	
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(String userId, String password, HttpSession session) {
		return "redirect:/deliver?userId=66000392&wiid=10982";
	}
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test(Model model, String userId, String password, HttpSession session) {
		System.out.println("ddd");
		return "test";
	}
}
