package com.abl.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.abl.common.RunJava;
import com.abl.dao.UserDao;
import com.abl.domain.FileListData;
import com.abl.domain.RtcWorkItem;
import com.abl.domain.User;
import com.abl.utils.CommonUtils;
import com.abl.utils.HttpSessionUtils;

/**
 * @author dhshin
 *
 */
@Controller
public class DeliverController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "properties")
	private Properties properties;

	@Resource(name = "userDao")
	private UserDao userDao;

	@Resource
	private User loginUser;
	
	@Resource
	private FileListData FileListData;

	@Resource
	private RtcWorkItem rtcWorkItem;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(String userId, String password, HttpSession session) {
		return "redirect:/deliver?userId=66000392&wiid=10982";
	}
	
	/**
	 * @param model
	 * @param userId
	 * @param wiid
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deliver", method = RequestMethod.GET)
	public String deliver(Model model, String userId, String wiid, HttpServletRequest request, HttpSession session) throws Exception {

		// login 처리후 처음접속 계정인 경우 session 등록
		boolean login = isLoginUser(request, session, userId, wiid);
		
		// property파일에 설정한 파일의 경로
		String rootPath = CommonUtils.replacePath(properties.getProperty("ROOT_PATH"));
		String targetPath = properties.getProperty("lock.target.dir");
		String returnMsg = "1";

		List<Map<String, Object>> dirList = FileListData.getTargetDirPath(targetPath);
		
		//소스 수정정보
		model.addAttribute("workList", login ? getWorkList(session, wiid) : "");
		model.addAttribute("changesetList", login ? getChangeFileList() : "");
		model.addAttribute("user_nm", loginUser.getUser_nm());
		model.addAttribute("user_id", userId);
		model.addAttribute("rootPath", rootPath);
		model.addAttribute("dirList", dirList);
		model.addAttribute("returnMsg", returnMsg);
		model.addAttribute("depth", "1");
		model.addAttribute("parent", "00");

		model.addAttribute("login", login ? "success" : "fail");
		return ("/deliver");
	}

	/*
	 * 로그인 후 소스수정정보 가져옴(AJAX)
	 */
	@RequestMapping(value = "/getWorkList", method = RequestMethod.POST)
	public ModelAndView getWorkListAjax(HttpServletRequest request, String wiid) {
		ModelAndView mav = new ModelAndView("/workList_add_ajax");
		List<Map<String, String>> workList = new ArrayList<Map<String, String>>();

		HttpSession session = request.getSession();
		// 소스수정정보 가져옴
		if (wiid != null && !wiid.equals("")) {
			workList = getWorkList(session, wiid);
		}

		mav.addObject("workList", workList);
		return mav;
	}
	
	@RequestMapping(value = "/deliverFile", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> deliverFile(@RequestParam Map<String, Object> paramMap, HttpServletRequest request) {
		logger.info("paramMap : {} ", paramMap);
		// lock 해야 할 list
		String lockListStr = (String) paramMap.get("lockListStr");
		// 이미 lock된 list -> rtc update시 같이 해야 함
		String lockUserId = (String) paramMap.get("lockUserId");

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("error", "no");
		String rtcFileName = lockListStr;
		// 세션에서 로그인 정보 불러옴
		HttpSession session = request.getSession();
		if (session == null) {
			resultMap.put("error", "error");
			return resultMap;
		}
		
		String userId = (String) session.getAttribute("rtcUserId");
		String userPw = (String) session.getAttribute("rtcUserPwd");
		String wiid = (String) session.getAttribute("rtc_id");
		int rtcreturn = RunJava.rtcUpdateCmd("windows", properties.getProperty("rtc.wcl.home"),"/ignoreErrors repository=" + properties.getProperty("rtc.url") + " user=" + userId + " password=" + userPw + " id=" + wiid + " com.ams.workitem.attribute.ch_lock_list=" + rtcFileName  + "");
		resultMap.put("success", rtcreturn);
		
		return resultMap;
	}
	
	private boolean isCheckUserId(String userId, String wiid) {
		if (userId != null && !userId.equals("")) {
			loginUser.setUser_id(userId);
			loginUser.setRtc_id(wiid);
			User user = userDao.getUserInfo(loginUser);
			logger.info("user : {} ", user);
			if (user == null) {
				return false;
			}
			loginUser = user;
			return true;
		}
		return false;
	}

	private boolean isLoginUser(HttpServletRequest request, HttpSession session, String userId, String wiid) {
		// 유저아이디가 DB에 있는경우
		if (isCheckUserId(userId, wiid)) {

			String sessionUserId = HttpSessionUtils.getUserFormSession(request.getSession());
			logger.info("sessionUserId = " + sessionUserId);

			if (!loginUser.matchUserId(sessionUserId)) {
				HttpSessionUtils.setUserSession(request.getSession(), loginUser, wiid);
			}
			return true;
		}
		return false;
	}
	
	
	/*
	 * 소스 수정정보를 가져옴
	 */
	public List<Map<String, String>> getWorkList(HttpSession session, String wiid) {
		return rtcWorkItem.getWorkList(session, wiid);
	}
	
	/*
	 * 해당 Change된 List 아래에 있는 file검색
	 */
	public List<Map<String, String>> getChangeFileList() {
		return FileListData.getChangesetFileList();
	}
}
