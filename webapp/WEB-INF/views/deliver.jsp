<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ page session="false" %>
<%request.setCharacterEncoding("UTF-8"); %>
<% String login = (String)request.getAttribute("login"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
 response.setHeader("Cache-Control","no-cache");
 response.setHeader("Pragma","no-cache");
 response.setDateHeader("Expires",0);
%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>Deliver화면</title>

<link rel="stylesheet" href="/resources/css/bootstrap.min.css">
<link rel="stylesheet" href="/resources/css/font-awesome.css">
<link rel="stylesheet" href="/resources/css/font-awesome.min.css">
<link rel="stylesheet" href="/resources/css/style.css">

<script src="/resources/js/jquery-1.12.4.js"></script>
<script src="/resources/js/ui.js"></script>
<script src="/resources/js/javascript-1.1.js"></script>
<script src="/resources/js/autocomplete-data.js"></script>

<link rel="stylesheet" href="/resources/css/jquery-confirm.min.css">
<script src="/resources/js/jquery-confirm.min.js"></script>

<link rel="stylesheet" href="/resources/css/jquery-ui.css">
<script src="/resources/js/jquery-ui.min.js"></script>

</head>

<body>
	<div class="wdth-100">
		<c:if test="${login=='fail'}">
			<div id="loginDiv">
				<form id="loginFrm">
				<div class="div1">
					<input type="text" id="lginId" name="lginId" class="vrtcl-mid" placeholder="ID" onkeydown="if(enterKeyPress()) {login();}" value="${user_id }"/>
					<input type="password" id="lginPw" name="lginPw" class="vrtcl-mid" placeholder="PW" onkeydown="if(enterKeyPress()) {login();}"/>
					<button type="button" id="loginBtn" name="loginBtn" class="btn1" onclick="login();">로그인</button>
				</div>
				<div id="loginMsg" class="div2">
				</div>
				</form>
			</div>
		</c:if>
		<c:if test="${login=='success'}">
			<div class="div3">
				<i class="fa fa-user-circle plus-icon" aria-hidden="true"></i>
				<input type="hidden" id="user_id" name="user_id" value="${user_id}"/>
				<span id="usrNm" class="fnt-wgt-b">${user_nm}</span>님 환영합니다.
			</div>
			<hr class="trans--grow">
			<script></script>
		</c:if>
	<table class="table4">
		<tr class="tr-wh">
			<td class="td3"><span class="add-on">파일경로</span><input type="text" id="filePath" class="width-400 vrtcl-mid"
				onkeydown="if(enterKeyPress()) {searchFile();}">
			</td>
			<td rowspan="2" class="td2">
			<!-- <span class="add-on">작업공간</span><input type="text" id="workspaceNm" class="width-400 vrtcl-mid"/> -->
				<button type="button" id="searchBtn" class="btn1 btn2-height btn2-width" onClick="searchFile();"><i class="fa fa-search"></i></button>
			</td>
		</tr>
		<tr class="tr-wh">
			<td>
				<span>파일검색</span><input type="text" id="fileName" class="width-400 vrtcl-mid"
				onkeydown="if(enterKeyPress()) {searchFile();}">
			</td>
		</tr>
	</table>
	</div>
		<form id="searchFrm" name="searchFrm">
			<input type="hidden" id="frmPath" name="frmPath">
			<input type="hidden" id="frmName" name="frmName">
			<input type="hidden" id="frmworkspaceNm" name="frmworkspaceNm">
		</form>
	<input type="hidden" id="changesetList" value="${changesetList}"/>
	<div class="div-ml2">
		<span class="fnt-size-12"> >> 소스 수정 정보</span>
		<table class="table1 fnt-size-12">
			<thead class="thead-dwh">
				<tr class="bg-color">
					<th class="tb-w120 td-ta-center">순번</th>
					<th class="tb-w30 td-ta-center">파일명</th>
					<th class="tb-w62 td-ta-center">파일경로</th>
				</tr>
			</thead>
		</table>
		<div id="sourceModifyInfo" class="scrollbar-line1">
		 	<table class="table2">
				<tbody class="tbody-dw" id="workListBody">
				<c:if test="${fn:length(workList) >0}">
					<c:forEach items="${workList}" var="workItem" varStatus="status">
						<tr class="searchLine">
							<td class="tb-w120 td-ta-center">${status.index + 1}</td>
							<td class="tb-w30 td-pl-60">
								<a style="cursor: pointer;" onclick="fillFileName('${workList[status.index].fileName}');">${workList[status.index].fileName}</a>
							</td>
							<td id="sm_${status.index}" class="tb-w62 td-pl-60">
								<script>
									var path = '${workList[status.index].path}';
									$('#sm_${status.index}').html(path.replace(/#/gi, '\\'));
								</script>
							</td>
						</tr>
					</c:forEach>
				</c:if>
				</tbody>
			</table>
		</div>
		<div class="div-hb"></div>
	</div>

	<!-- 하단DIV -->

	<table class="table3">
		<tr>
			<td rowspan="3" class="tb-w30 height-100">
				<div id="folderList" class="div-wdvbp">
					<div id='${parent}_${depth}' class="jstree-custom">
						<ul id='addNodeUl' style="margin-bottom: 2px; margin-left: 0px;">
					    	<li class="fileName"
					    		onClick="lineColorCss('${depth}${status.index}')"
					    		ondblClick="getFileList('${depth}${status.index}', '${dirList[status.index].path}');"
					    		oncontextmenu="return false"
					    		ondragstart="return false"
					    		onselectstart="return false" >
					    		<i class="fa" ></i>
					    		<i class="fa fa-folder-open-o fileName" ></i>
					    		<span>RepositoryPath</span>
					    	</li>
					    </ul>
					    <ul id='addNodeUl' class="" style="margin-left:10px;">
					    	<c:forEach items="${dirList}" var="dirListItems" varStatus="status">
					    		<li id="${depth}${status.index}" class="fileName"
					    			ondblClick="getFileList('${depth}${status.index}', '${dirList[status.index].path}');"
					    			oncontextmenu="return false" ondragstart="return false" onselectstart="return false"
					    			onClick="lineColorCss('${depth}${status.index}'); setSearchPath('${dirList[status.index].path}');">
					    			<i class="plus-icon fa fa-plus-square-o"
					    				onClick="getChildrenNode('${parent}', '${depth}${status.index}', '${dirList[status.index].path}');"></i>
					    			<i id="i_${depth}${status.index}"
					    				class="fa fa-folder-o fileName"
					    				ondblClick="getFileList('i_${depth}${status.index}','${dirList[status.index].path}');"></i>
					    			<span>${dirList[status.index].data}</span>
					    		</li>
							</c:forEach>
					    </ul>
					</div>
				  	<form id='frm' method='post'>
				  		<input type='hidden' id='parent' name='data' />
				  		<input type='hidden' id='path' name='path' />
					</form>
				</div>
			</td>

			<td>
				<div id="deployTd" class="div-ml"><span> >> 목록조회</span>
				<table class="table1">
					<thead class="thead-dwh">
						<tr class="bg-color">
							<th class="tb-w100 td-ta-center"><input type="checkbox" id="dpl_checkAll" name="dpl_checkAll"></th>
							<th class="tb-w25 td-ta-center">파일명</th>
							<th class="tb-w65 td-ta-center">파일경로</th>
							<!-- <th class="tb-w20 td-ta-center"></th> -->
						</tr>
					</thead>
				</table>
				<div id="DeployList" class="scrollbar-line2">
					<table id="deployListTable" class="table1">
						<tbody id="deployListBody" class="thead-dwh tbody-h100">
							<c:forEach items="${fileList}" var="dirListItems" varStatus="status">
								<tr class="searchLine">
									<td class="tb-w100 td-ta-center">
										<input type="checkbox" name="dplChk"></td>
									<td class="tb-w25 td-pl-60">
										<a style="cursor: pointer;" onclick="fillFileName('${fileList[status.index].data}');">${fileList[status.index].data}</a>
									</td>
									<td id="dp_${status.index}" class="tb-w50">
										<script>
											var path = '${fileList[status.index].path}';
											$('#dp_${status.index}').html(path.replace(/#/gi, '\\'));
										</script>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div class="div-hb"></div>
				<div class="div-ta-right">
					<button type="button" id="moveLckBtn" class="btn1" onclick="moveDeliver(); checkInputBox();" disabled="disabled">추가</button>
				</div>
				</div>
			</td>
		</tr>

		<tr class="tr-wh">
			<td>
				<div id="lockTd" class="div-ml"><span> >> Deliver 파일 선택 List</span>
				<table class="table1">
					<thead class="thead-dwh">
						<tr class="bg-color">
						<th class="tb-w100 td-ta-center"><input type="checkbox" id="lock_checkAll" name="lock_checkAll"></th>
							<th class="tb-w25 td-ta-center">파일명</th>
							<th class="tb-w45 td-ta-center">파일경로</th>
							<th class="tb-w20 td-ta-center">비고</th>
						</tr>
					</thead>
				</table>
				<div id="LockList" class="scrollbar-line2">
					<table class="table2">
						<tbody id="lockListBody" class="tbody-dw tbody-h100 lockTable_body">
						</tbody>
					</table>
				</div>

				<form id="lockFrm">
					<input type="hidden" id="lockListStr" name="lockListStr">
					<input type="hidden" id="lockUserId" name="lockUserId"/>
				</form>

				<div class="div-hb"></div>
				<div class="div-ta-right before-lock-btn">
				<!-- <span class="add-on">작업공간</span><input type="text" id="workspaceNm2" class="width-400 vrtcl-mid"/> -->
					<button id="lockBtn" class="btn1" onClick="deliverFile();" disabled="disabled">Deliver</button>
					<button type="button" id="moveDepBtn" class="btn1" onclick="moveFileListSelector(); checkInputBox();" disabled="disabled">선택취소</button>
				</div>

				<div class="div-ta-right after-lock-btn" style="display: none">
				<span class="add-on">작업공간</span><input type="text" id="workspaceNm2" class="width-400 vrtcl-mid"/>
					<button id="lockRtryBtn" class="btn1" onClick="setLock();">재시도</button>
				</div>
			</div>
			</td>
		</tr>
	</table>
	<!-- //하단DIV -->
</body>

</html>
