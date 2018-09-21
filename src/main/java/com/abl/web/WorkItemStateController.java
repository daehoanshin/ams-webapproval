package com.abl.web;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.abl.common.RunJava;

/**
 * @author dhshin
 *
 */
@Controller
public class WorkItemStateController {
	private final String RTC_WORK_HOME = "";
	private final String RTC_URL = "https://sdh.net:9443/ccm";
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String update(Model model, String userId, String password, String wiid, HttpSession session) {
		String os = System.getProperty("os.name");
		
		Map<String, Object> returnMap = RunJava.workItemCmd(os, RTC_WORK_HOME,
				"/dev_item_reader.jar " + RTC_URL + " " + userId + " " + password + " " + wiid);
		return "test1";
	}
}
