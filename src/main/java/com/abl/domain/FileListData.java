package com.abl.domain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FileListData extends AbstractFile {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "properties")
	private Properties properties;


	/**
	 * 파일목록에서 하위디렉토리 검색 체인지셋 내역 포함
	 * 
	 * @param filePath
	 * @param changesetList
	 * @return
	 */
	public List<Map<String, Object>> getSubDirList(String filePath, List<Map<String, String>> changesetList) {
		List<Map<String, Object>> dirList = new ArrayList<Map<String, Object>>();
		boolean folderCheck = false;

		try {
			File file = lastFileModified(properties.getProperty("repository.file.dir"));
			FileReader fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				Map<String, Object> itemInfo = new HashMap<String, Object>();

				// 해당 item이 디렉토리일 경우
				if (line.equals(filePath + ":")) {
					// root폴더
					if (line.startsWith("./") && line.endsWith(":")) {
						rootFloder = line.substring(0, line.length() - 1);
						folderCheck = true;
					}
					// 폴더인 경우
				} else if (folderCheck && line.endsWith("/")) {
					itemInfo.put("data", line.substring(0, line.length() - 1));
					itemInfo.put("path", rootFloder + "/" + line.substring(0, line.length() - 1));
					itemInfo.put("changeset",
							subDirChangeset(changesetList, rootFloder + "/" + line.substring(0, line.length() - 1)));

					dirList.add(itemInfo);
				} else if (folderCheck && line.equals("")) {
					return dirList;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());

		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		return dirList;
	}

	public List<Map<String, Object>> getFileList(String filePath, List<Map<String, String>> changesetList) {
		String ignoreFile = properties.getProperty("deploy.file.none");
		List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
		boolean folderCheck = false;

		try {
			File file = lastFileModified(properties.getProperty("repository.file.dir"));
			FileReader fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				Map<String, Object> itemInfo = new HashMap<String, Object>();

				// 해당 item이 디렉토리일 경우
				if (line.equals(filePath + ":")) {
					// root폴더
					if (line.startsWith("./") && line.endsWith(":")) {
						rootFloder = line.substring(0, line.length() - 1);
						folderCheck = true;
					}
					// 파일인 경우
				} else if (folderCheck && !line.startsWith(".") && !line.contains("/") && !line.equals("")) {

					if (!line.equals(ignoreFile)) {
						itemInfo.put("data", line.endsWith("*") ? line.substring(0, line.length() - 1) : line);
						itemInfo.put("path", rootFloder);
						itemInfo.put("changeset", fileChangeset(changesetList, rootFloder + "/" + line.substring(0, line.length() - 1)));
						fileList.add(itemInfo);
					}
				} else if (folderCheck && line.equals("")) {
					return fileList;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());

		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		return fileList;
	}

	/**
	 * 지정된 폴더에서 파일명 검색
	 * 
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public List<Map<String, Object>> getSearchFile(String filePath, String fileName, List<Map<String, String>> changesetList) {
		String ignoreFile = properties.getProperty("deploy.file.none");
		List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
		boolean folderCheck = false;

		// 파일명 포함한 전체 내용검색 ex web.xml*
		if (fileName.endsWith("*")) {
			return getSearchAllFile(filePath, fileName, changesetList);
		}
		try {
			File file = lastFileModified(properties.getProperty("repository.file.dir"));
			FileReader fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				Map<String, Object> itemInfo = new HashMap<String, Object>();

				// 해당 item이 디렉토리일 경우
				if (line.equals(filePath + ":")) {
					// root폴더
					if (line.startsWith("./") && line.endsWith(":")) {
						rootFloder = line.substring(0, line.length() - 1);
						folderCheck = true;
					}
					// 파일인 경우
				} else if (folderCheck && !line.startsWith(".") && !line.contains("/") && !line.equals("")) {
					line = line.endsWith("*") ? line.substring(0, line.length() - 1) : line;
					if (line.equals(fileName) && !line.equals(ignoreFile)) {
						itemInfo.put("data", line);
						itemInfo.put("path", rootFloder);
						itemInfo.put("changeset", fileChangeset(changesetList, rootFloder + "/" + line.substring(0, line.length())));
						fileList.add(itemInfo);
					} else if (fileName.endsWith("*") && line.startsWith(fileName.substring(0, fileName.length() - 1)) && !line.equals(ignoreFile)) {
						itemInfo.put("data", line);
						itemInfo.put("path", rootFloder);
						itemInfo.put("changeset", fileChangeset(changesetList, rootFloder + "/" + line.substring(0, line.length() - 1)));
						fileList.add(itemInfo);
					}
				} else if (folderCheck && line.equals("")) {
					return fileList;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());

		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		return fileList;
	}

	/**
	 * 전체 폴더와 파일 like검색
	 * 
	 * @param fileName
	 * @return
	 */
	public List<Map<String, Object>> getSearchAllFile(String filePath, String fileName, List<Map<String, String>> changesetList) {
		logger.info(">> getSearchAllFile >> ");
		String ignoreFile = properties.getProperty("deploy.file.none");
		List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
		boolean folderCheck = false;

		try {
			File file = lastFileModified(properties.getProperty("repository.file.dir"));
			FileReader fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				Map<String, Object> itemInfo = new HashMap<String, Object>();

				// 해당 item이 디렉토리일 경우
				if (line.startsWith("./") && line.endsWith(":")) {
					// root폴더
					rootFloder = line.substring(0, line.length() - 1);
					folderCheck = true;
					// 파일인 경우
				} else if (folderCheck && !line.startsWith(".") && !line.contains("/") && !line.equals("")) {
					line = line.endsWith("*") ? line.substring(0, line.length() - 1) : line;
					if (line.equals(fileName) && !line.equals(ignoreFile)) {
						itemInfo.put("data", line);
						itemInfo.put("path", rootFloder);
						itemInfo.put("changeset", SearchFileChangeset(changesetList, rootFloder + "/" + line.substring(0, line.length())));
						fileList.add(itemInfo);
					} else if (fileName.endsWith("*") && line.startsWith(fileName.substring(0, fileName.length() - 1)) && !line.equals(ignoreFile)) {
						if (filePath.endsWith("*") && rootFloder.startsWith(filePath.substring(0, filePath.length() - 1))) {
							itemInfo.put("data", line);
							itemInfo.put("path", rootFloder);
							itemInfo.put("changeset", SearchFileChangeset(changesetList, rootFloder + "/" + line.substring(0, line.length())));
							fileList.add(itemInfo);
						} else if (!filePath.endsWith("*") && rootFloder.equals(filePath)) {
							itemInfo.put("data", line);
							itemInfo.put("path", rootFloder);
							itemInfo.put("changeset", SearchFileChangeset(changesetList, rootFloder + "/" + line.substring(0, line.length())));
							fileList.add(itemInfo);
						} else if (filePath.equals("")) {
							itemInfo.put("data", line);
							itemInfo.put("path", rootFloder);
							itemInfo.put("changeset", SearchFileChangeset(changesetList, rootFloder + "/" + line.substring(0, line.length())));
							fileList.add(itemInfo);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());

		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		return fileList;
	}

	/**
	 * 체크인 파일 목록
	 * 
	 * @param fileName
	 * @return
	 */
	public List<Map<String, String>> getChangesetFileList() {
		List<Map<String, String>> changeList = new ArrayList<Map<String, String>>();

		try {
			File file = lastFileModified(properties.getProperty("checkin.file.dir"));
			FileReader fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				line = line.trim();
				Map<String, String> itemInfo = new HashMap<String, String>();
				if (line.contains("NGS_Deploy") && line.startsWith("-")) {
					// logger.info("data : {} ", line.substring(line.lastIndexOf("/") + 1));
					// logger.info("path : {} ", "." + line.substring(line.indexOf("- ") + 2,
					// line.lastIndexOf("/")));
					itemInfo.put("data", line.substring(line.lastIndexOf("/") + 1));
					itemInfo.put("path", "." + line.substring(line.indexOf("- ") + 2, line.lastIndexOf("/")));
					changeList.add(itemInfo);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());

		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		return changeList;
	}

	public String subDirChangeset(List<Map<String, String>> changesetList, String filePath) {

		for (Map<String, String> map : changesetList) {
			changesetPath = map.get("path").trim();
			if (changesetPath.equals(filePath)) {
				return "exist";
			}
		}
		return "";
	}

	public String fileChangeset(List<Map<String, String>> changesetList, String filePath) {

		for (Map<String, String> map : changesetList) {
			changesetPath = map.get("path").trim();
			changesetData = map.get("data").trim();
			line = changesetPath + "/" + changesetData;
			if (line.equals(filePath)) {
				return "exist";
			}
		}
		return "";
	}

	public String SearchFileChangeset(List<Map<String, String>> changesetList, String filePath) {

		for (Map<String, String> map : changesetList) {
			changesetPath = map.get("path").trim();
			changesetData = map.get("data").trim();
			line = changesetPath + "/" + changesetData;
			/*
			 * logger.info(">> SearchFileChangeset"); logger.info("line : {} ", line);
			 * logger.info("filePath : {} ", filePath);
			 */
			if (line.equals(filePath)) {
				/*
				 * logger.info(">> SearchFileChangeset"); logger.info("line : {} ", line);
				 * logger.info("filePath : {} ", filePath);
				 */
				return "exist";
			}
		}
		return "";
	}
}