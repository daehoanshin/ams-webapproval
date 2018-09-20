package com.abl.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunJava {

	static Logger logger = LoggerFactory.getLogger(RunJava.class);

	public static Map<String, Object> workItemCmd(String runType, String rtc_work_dir, String cmd) {
		String s = null;
		String returnmsg = "";
		int returncode = 1;
		List<String> workList = new ArrayList<String>();
		Map<String, Object> returnMap = new HashMap<String, Object>();

		try {
			Runtime oRuntime = Runtime.getRuntime();
			Process oProcess = null;

			if (runType.equalsIgnoreCase("windows")) {
				// DOS command
				logger.info("get work item list");
				logger.info("cmd /c java -jar " + rtc_work_dir + cmd);

				oProcess = oRuntime.exec("cmd /c java -jar " + rtc_work_dir + cmd);
			} else {
				oProcess = oRuntime.exec("java -jar " + rtc_work_dir + cmd);
			}

			BufferedReader stdOut = new BufferedReader(new InputStreamReader(oProcess.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(oProcess.getErrorStream()));

			while ((s = stdOut.readLine()) != null) {
				logger.info("result message : " + s);
				// System.out.println(s);
				workList.add(s);
				returnmsg = returnmsg + s;
			}

			while ((s = stdError.readLine()) != null) {
				logger.warn("error message : " + s);
				// System.err.println(s);
				returnmsg = returnmsg + s;
			}

			returncode = oProcess.exitValue();

		} catch (IOException e1) {
			logger.warn("There is an error of io");
		}

		returnMap.put("returncode", returncode + "");
		returnMap.put("workList", workList);
		logger.info("exit value : " + returncode);
		logger.info("work list size: " + workList.size());
		return returnMap;
	}

}
