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

	public static int loginCmd(String runType, String rtcscm_home_dir, String cmd) {
		String s = null;
		String returnmsg = "";
		int returncode = 1;
		
		try {
			Runtime oRuntime = Runtime.getRuntime();
			Process oProcess = null;
			if(runType.equalsIgnoreCase("windows")){
				// DOS command
				logger.info("login");
				logger.info("cmd /c " + rtcscm_home_dir + "/scm.exe" + " " + cmd);
				
				oProcess = oRuntime.exec("cmd /c " + rtcscm_home_dir + "/scm.exe" + " " + cmd);
			} else {
				logger.info("cmd : {} ", rtcscm_home_dir + "/lscm" + " " + cmd);
				oProcess = oRuntime.exec("cd /home/xbb123/IBM/RTC-Client-Linux64-5.0.2/workspace");
				oProcess = oRuntime.exec(rtcscm_home_dir + "/lscm" + " " + cmd);
			}
			
			BufferedReader stdOut = new BufferedReader(new InputStreamReader(
					oProcess.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					oProcess.getErrorStream()));

			while ((s = stdOut.readLine()) != null){
				logger.info("result message : " + s);
				//System.out.println(s);
				returnmsg = returnmsg+s;	
			}

				
			while ((s = stdError.readLine()) != null){
				logger.warn("error message : " + s);
				returnmsg = returnmsg+s;
			}
				
			returncode = oProcess.exitValue();
			
		} catch (IOException e1) {
			logger.warn("There is an error of io");
		}

		logger.info("exit value : " + returncode);
		return returncode;
	}
	
	
	
	
	
	public static Map<String, Object> workItemCmd(String runType, String rtc_work_dir, String cmd) {
		String s = null;
		String returnmsg = "";
		int returncode = 1;
		List<String> workList = new ArrayList<String>();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		try {
			Runtime oRuntime = Runtime.getRuntime();
			Process oProcess = null;

			if(runType.equalsIgnoreCase("windows")){
				// DOS command
				logger.info("get work item list");
				logger.info("cmd /c java -jar " + rtc_work_dir + cmd);
				
				oProcess = oRuntime.exec("cmd /c java -jar " + rtc_work_dir + cmd);
			} else {
				oProcess = oRuntime.exec("java -jar " + rtc_work_dir + cmd);
			}

			
			BufferedReader stdOut = new BufferedReader(new InputStreamReader(
					oProcess.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					oProcess.getErrorStream()));


			while ((s = stdOut.readLine()) != null){
				logger.info("result message : " + s);
				//System.out.println(s);
				workList.add(s);
				returnmsg = returnmsg+s;	
			}

				
			while ((s = stdError.readLine()) != null){
				logger.warn("error message : " + s);
				//System.err.println(s);
				returnmsg = returnmsg+s;
			}

			returncode = oProcess.exitValue();
			
		} catch (IOException e1) {
			logger.warn("There is an error of io");
			//System.out.println("There is an error of io");
		}

		returnMap.put("returncode", returncode+"");
		returnMap.put("workList", workList);
		logger.info("exit value : " + returncode);
		logger.info("work list size: " + workList.size());
		return returnMap;
	}
	
	
	
	public static int rtcUpdateCmd(String runType, String rtcwcl_dir, String cmd) {
		String s = null;
		String returnmsg = "";
		int returncode = 1;
		
		try {
			Runtime oRuntime = Runtime.getRuntime();
			Process oProcess = null;
			if(runType.equalsIgnoreCase("windows")){
				String javaHome_dir = rtcwcl_dir.substring(0,rtcwcl_dir.lastIndexOf("/")) + "\\jdk";
				String plainJava_dir = rtcwcl_dir.substring(0,rtcwcl_dir.lastIndexOf("/")) + "/RTC-Client-plainJavaLib-5.0.2";
				// DOS command
				logger.info("RTC Lock List Update");
				logger.info("cmd /c " + javaHome_dir + "\\jre\\bin\\java -Djava.security.policy=rmi_no.policy -Djava.ext.dirs=./lib;" + plainJava_dir + ";" + javaHome_dir + "/jre/lib/ext -cp ./lib;" + plainJava_dir + " -jar " + rtcwcl_dir + "/wcl.jar -update " + cmd);
				
				
				//System.out.println("cmd /c " + javaHome_dir + "\\jre\\bin\\java -Djava.security.policy=rmi_no.policy -Djava.ext.dirs=./lib;" + plainJava_dir + ";" + javaHome_dir + "/jre/lib/ext -cp ./lib;" + plainJava_dir + " -jar " + rtcwcl_dir + "/wcl.jar -update " + cmd);
				oProcess = oRuntime.exec("cmd /c " + javaHome_dir + "\\jre\\bin\\java -Djava.security.policy=rmi_no.policy -Djava.ext.dirs=./lib;" + plainJava_dir + ";" + javaHome_dir + "/jre/lib/ext -cp ./lib;" + plainJava_dir + " -jar " + rtcwcl_dir + "/wcl.jar -update " + cmd);
				
			}
			
			BufferedReader stdOut = new BufferedReader(new InputStreamReader(
					oProcess.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					oProcess.getErrorStream()));

			while ((s = stdOut.readLine()) != null){
				logger.info("result message : " + s);
				//System.out.println(s);
				returnmsg = returnmsg+s;	
			}

				
			while ((s = stdError.readLine()) != null){
				logger.warn("error message : " + s);
				//System.err.println(s);
				returnmsg = returnmsg+s;
			}
				
			returncode = oProcess.exitValue();
			
		} catch (IOException e1) {
			logger.warn("There is an error of io");
			//System.out.println("There is an error of io");
		}

		logger.info("exit value : " + returncode);
		return returncode;
	}
	
	public static int lockCmd(String runType, String rtcscm_home_dir, String cmd) {
		// 콘솔 실행결과 출력을 위한 변수
		String s = null;
		String returnmsg = "";
		int returncode = 1;

		
		try {
			// 런타임 객체 생성
			Runtime oRuntime = Runtime.getRuntime();
			Process oProcess = null;
			// 외부 프로그램 명령어 실행
			if(runType.equalsIgnoreCase("windows")){
				// DOS command
				logger.info("Lock");
				logger.info("cmd /c " + rtcscm_home_dir + "/scm.exe" + " " + cmd);
				
				
				//System.out.println("cmd /c " + rtcscm_home_dir + "/scm.exe" + " " + cmd);
				oProcess = oRuntime.exec("cmd /c " + rtcscm_home_dir + "/scm.exe" + " " + cmd);
			}
			else{
				// Linux command
				oProcess = oRuntime.exec("/bin/sh " + rtcscm_home_dir + "/scm.sh" + " " + cmd);
			}

			// 외부 프로그램 출력 읽기
			BufferedReader stdOut = new BufferedReader(new InputStreamReader(
					oProcess.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					oProcess.getErrorStream()));

			// "표준 출력"과 "표준 에러 출력"을 출력
			while ((s = stdOut.readLine()) != null){
				logger.info("result message : " + s);
				//System.out.println(s);
				returnmsg = returnmsg+s;	
			}

				
			while ((s = stdError.readLine()) != null){
				logger.warn("error message : " + s);
				//System.err.println(s);
				returnmsg = returnmsg+s;
			}
				
			
			returncode = oProcess.exitValue();
			
		} catch (IOException e1) {
			logger.warn("There is an error of io");
			//System.out.println("There is an error of io");
		}

		logger.info("exit value : " + returncode);
		return returncode;
	}
	
	
	public static Map<String, Object> changesetCmd(String runType, String rtcscm_home_dir, String cmd) {
		// 콘솔 실행결과 출력을 위한 변수
		String s = "";
		StringBuilder returnmsg = new StringBuilder();
		//String s = null;
		//String returnmsg = "";
		int returncode = 1;
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		try {
			// 런타임 객체 생성
			Runtime oRuntime = Runtime.getRuntime();
			Process oProcess = null;
			// 외부 프로그램 명령어 실행
			if(runType.equalsIgnoreCase("windows")){
				// DOS command
				logger.info("Lock");
				logger.info("cmd /c " + rtcscm_home_dir + "/scm.exe" + " " + cmd);
				
				//System.out.println("cmd /c " + rtcscm_home_dir + "/scm.exe" + " " + cmd);
				oProcess = oRuntime.exec("cmd /c " + rtcscm_home_dir + "/scm.exe" + " " + cmd);
			}
			else{
				oProcess = oRuntime.exec("cd /home/xbb123/IBM/RTC-Client-Linux64-5.0.2/workspace");
				// Linux command
				oProcess = oRuntime.exec("/bin/sh " + rtcscm_home_dir + "/scm.sh" + " " + cmd);
			}

			// 외부 프로그램 출력 읽기
			BufferedReader stdOut = new BufferedReader(new InputStreamReader(
					oProcess.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					oProcess.getErrorStream()));

			// "표준 출력"과 "표준 에러 출력"을 출력
			while ((s = stdOut.readLine()) != null){
				logger.info("result message : " + s);
				//System.out.println(s);
				returnmsg.append(s);
				//logger.info("result message sub: " + returnmsg.substring(7,11));
				
			}

				
			while ((s = stdError.readLine()) != null){
				logger.warn("error message : " + s);
				//System.err.println(s);
				returnmsg.append(s);
			}
			
			
			logger.info("returnmsg message : " + returnmsg);
			
			returncode = oProcess.exitValue();
			
			returnMap.put("returncode", returncode);
			returnMap.put("returnmsg", returnmsg);
			
		} catch (IOException e1) {
			logger.warn("There is an error of io");
			//System.out.println("There is an error of io");
		}

		logger.info("exit value : " + returncode);
		return returnMap;
	}
	
}
