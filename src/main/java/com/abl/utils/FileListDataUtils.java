package com.abl.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileListDataUtils extends CommonUtils {
	static String ignoreFile = ".jazz5";

	/**
	 * 파일목록에서 하위디렉토리 검색
	 * 
	 * @param filePath
	 * @return
	 */
	public static List<Map<String, Object>> getSubDirList(String filePath) {
		List<Map<String, Object>> dirList = new ArrayList<Map<String, Object>>();
		BufferedReader bufferedReader = null;
		String line = "";
		String rootFloder = "";
		boolean folderCheck = false;

		try {
			File file = lastFileModified(properties.getProperty("repository.file.dir"));
			FileReader fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				Map<String, Object> itemInfo = new HashMap<String, Object>();
				logger.info("line : {} ", line);
				// 해당 item이 디렉토리일 경우
				if (line.equals(filePath + ":") && filePath.equals("./")) {
					// root폴더
					if (line.startsWith("./") && line.endsWith(":")) {
						rootFloder = line.substring(0, line.length() - 1);
						folderCheck = true;
					}
					// 폴더인 경우
				} else if (folderCheck && line.endsWith("/")) {
					logger.info("line : {} " + line);
					itemInfo.put("data", line.substring(0, line.length() - 1));
					itemInfo.put("path", rootFloder + "/" + line.substring(0, line.length() - 1));

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

	/**
	 * 지정된 폴더에서 파일 목록 가져오기
	 * 
	 * @param filePath
	 * @return
	 */
	public static List<Map<String, Object>> getFileList(String filePath) {
		List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
		BufferedReader bufferedReader = null;
		String line = "";
		String rootFloder = "";
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
	public static List<Map<String, Object>> getSearchFile(String filePath, String fileName) {
		List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
		BufferedReader bufferedReader = null;
		String line = "";
		String rootFloder = "";
		boolean folderCheck = false;
		
		//전체파일 조회
		if (fileName.endsWith("*")) {
			return getSearchAllFile(filePath, fileName);
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
						fileList.add(itemInfo);
					}else if(fileName.endsWith("*") && line.startsWith(fileName.substring(0, fileName.length()-1)) && !line.equals(ignoreFile)) {
						itemInfo.put("data", line);
						itemInfo.put("path", rootFloder);
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
	 * 전체 폴더와 파일  like검색
	 * 
	 * @param fileName
	 * @return
	 */
	public static List<Map<String, Object>> getSearchAllFile(String filePath, String fileName) {
		List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
		BufferedReader bufferedReader = null;
		String line = "";
		String rootFloder = "";
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
						fileList.add(itemInfo);
					}else if(fileName.endsWith("*") && line.startsWith(fileName.substring(0, fileName.length()-1)) && !line.equals(ignoreFile)) {
						if(filePath.endsWith("*") && rootFloder.startsWith(filePath.substring(0, filePath.length()-1))) {
							itemInfo.put("data", line);
							itemInfo.put("path", rootFloder);
							fileList.add(itemInfo);
						}else if(!filePath.endsWith("*") && rootFloder.equals(filePath)) {
							itemInfo.put("data", line);
							itemInfo.put("path", rootFloder);
							fileList.add(itemInfo);
						}else if(filePath.equals("")) {
							itemInfo.put("data", line);
							itemInfo.put("path", rootFloder);
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
	 * @param fileName
	 * @return
	 */
	public static List<Map<String, String>> getChangeFileList(String filePath) {
		List<Map<String, String>> changeList = new ArrayList<Map<String, String>>();
		BufferedReader bufferedReader = null;
		String line = "";

		try {
			File file = lastFileModified(filePath);
			FileReader fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				line = line.trim();
				Map<String, String> itemInfo = new HashMap<String, String>();
				if(line.contains("NGS_Deploy") && line.startsWith("-")) {
					logger.info("data  : {} ", line.substring(line.lastIndexOf("/")+1));
					logger.info("path : {} ", "."+line.substring(line.indexOf("- ")+2, line.lastIndexOf("/")));
					itemInfo.put("data", line.substring(line.lastIndexOf("/")+1));
					itemInfo.put("path", "."+line.substring(line.indexOf("- ")+2, line.lastIndexOf("/")));
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
	public static List<Map<String, Object>> getTargetDirPath(String path) {
		logger.info(">> getTargetDirPath");
		logger.info("path : {} ", path);
		String[] splitPath = path.split(",");
		List<Map<String, Object>> dirList = new ArrayList<Map<String, Object>>();

		for (String pathTmp : splitPath) {
			Map<String, Object> pathMap = new HashMap<String, Object>();

			String[] getName = pathTmp.split("\\\\");

			int nameListLength = getName.length;

			String name = getName[nameListLength - 1];
			String newPath = replacePath(pathTmp);

			pathMap.put("data", name);
			pathMap.put("path", newPath);

			dirList.add(pathMap);
		}
		return dirList;
	}
	public static File lastFileModified(String dir) {
		File fl = new File(dir);
		File[] files = fl.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isFile();
			}
		});
		long lastMod = Long.MIN_VALUE;
		File choice = null;
		for (File file : files) {
			if (file.lastModified() > lastMod) {
				choice = file;
				lastMod = file.lastModified();
			}
		}
		return choice;
	}
}