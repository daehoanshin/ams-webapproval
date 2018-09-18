package com.abl.web;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.abl.domain.FileListData;
import com.abl.utils.CommonUtils;

@Controller
public class FileListController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "properties")
	private Properties properties;

	@Resource
	private FileListData FileListData;

	@PostConstruct
	public void init() {
		CommonUtils.setProperties(properties);
	}

	/*
	 * 파일 하위 디렉토리 가져옴(AJAX)
	 */
	@RequestMapping(value = "/subDirList", method = RequestMethod.POST)
	public String subDirList(Model model, @RequestParam Map<String, Object> paramMap) {
		//logger.info("paramMap :  {}", paramMap);

		String parent = (String) paramMap.get("data");
		String path = (String) paramMap.get("path");
		String directoryPath = CommonUtils.replacePath(path);
		String depth = CommonUtils.getDepth(path);

		List<Map<String, Object>> subDirList = FileListData.getSubDirList(directoryPath,
				FileListData.getChangesetFileList());
		model.addAttribute("subDirList", subDirList);
		model.addAttribute("depth", depth);
		model.addAttribute("parent", parent);

		return "/sub_dir_ajax";
	}

	/*
	 * 하위 디렉토리의 파일정보를 들고옴(AJAX)
	 */
	@RequestMapping(value = "/fileList", method = RequestMethod.POST)
	public String fileList(Model model, @RequestParam Map<String, Object> paramMap) {
		String filePath = CommonUtils.replacePath((String) paramMap.get("path"));
		List<Map<String, Object>> fileList = FileListData.getFileList(filePath, FileListData.getChangesetFileList());

		model.addAttribute("fileList", fileList);

		return "/sub_dir_ajax_2";
	}

	/**
	 * 디렉토리와 파일정보 검색으로 목록조회(AJAX)
	 * 
	 * @param model
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/searchFile", method = RequestMethod.POST)
	public String searchFile(Model model, @RequestParam Map<String, Object> paramMap) {
		String filePath = (String) paramMap.get("frmPath");
		String fileName = (String) paramMap.get("frmName");

		String returnMsg = "1";
		// 해당 path아래에서 파일 검색
		List<Map<String, Object>> fileList = fileName.equals("")
				? FileListData.getFileList(filePath, FileListData.getChangesetFileList())
				: FileListData.getSearchFile(filePath, fileName, FileListData.getChangesetFileList());

		model.addAttribute("fileList", fileList);
		model.addAttribute("returnMsg", returnMsg);

		return "/sub_dir_ajax_2";
	}
}
