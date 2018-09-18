package com.abl.domain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.abl.utils.CommonUtils;

public class AbstractFile {
	String line = "";
	String rootFloder = "";
	String changesetData = "";
	String changesetPath = "";
	BufferedReader bufferedReader = null;
	
	public List<Map<String, Object>> getTargetDirPath(String path) {
		String[] splitPath = path.split(",");
		List<Map<String, Object>> dirList = new ArrayList<Map<String, Object>>();

		for (String pathTmp : splitPath) {
			Map<String, Object> pathMap = new HashMap<String, Object>();

			String[] getName = pathTmp.split("\\\\");

			int nameListLength = getName.length;

			String name = getName[nameListLength - 1];
			String newPath = CommonUtils.replacePath(pathTmp);

			pathMap.put("data", name);
			pathMap.put("path", newPath);

			dirList.add(pathMap);
		}
		return dirList;
	}

	public File lastFileModified(String dir) {
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