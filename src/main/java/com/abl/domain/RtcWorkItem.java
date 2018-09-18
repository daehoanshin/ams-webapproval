package com.abl.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.abl.common.RunJava;

@Service
public class RtcWorkItem {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name = "properties")
	private Properties properties;
	
	/*
	 * 소스 수정정보를 가져옴
	 */
	public List<Map<String, String>> getWorkList(HttpSession session, String wiid) {
		List<Map<String, String>> workList = new ArrayList<Map<String, String>>();

		String userId = (String) session.getAttribute("rtcUserId");
		String userPw = (String) session.getAttribute("rtcUserPwd");
		List<String> workListTmp = null;
		String os = System.getProperty("os.name");
		logger.info("userId : {}, wiid : {} ", userId , wiid);
		if(wiid != null && !wiid.equals("")) {
			// command line실행하여 소스수정정보를 가져옴
			Map<String, Object> returnMap = RunJava.workItemCmd(os, properties.getProperty("rtc.work.home"),
					"/dev_item_reader.jar " + properties.getProperty("rtc.url") + " " + userId + " " + userPw + " " + wiid);
			workListTmp = (List<String>) returnMap.get("workList");
		}
		

		// 소스수정정보가 있을 때
		if (workListTmp != null) {
			for (String tmp : workListTmp) {
				// pull path를 파일경로와 파일명으로 나누어 workList에 저장
				if (tmp.indexOf("/") > -1) {
					int lastIndex = tmp.lastIndexOf("/");
					Map<String, String> workMap = new HashMap<String, String>();
					String fileName = tmp.substring(lastIndex + 1);
					String ignoreFile = properties.getProperty("deploy.file.none");

					// jazzignore이 아닐때
					if (!fileName.equals(ignoreFile)) {
						workMap.put("path", tmp.substring(0, lastIndex + 1));
						workMap.put("fileName", fileName);

						workList.add(workMap);
					}

				}
			}
		}

		return workList;
	}
}