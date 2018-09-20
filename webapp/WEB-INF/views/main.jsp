<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ page session="false" %>
<%request.setCharacterEncoding("UTF-8"); %>
<% String login = (String)request.getAttribute("login"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>Lock화면</title>

<!-- <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/coliff/bootstrap-ie7/css/bootstrap-ie7.min.css"> -->
<!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css"> -->
<!-- <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"> -->
<!-- <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.0.3/css/font-awesome.css"> -->
<!-- <script src='https://code.jquery.com/jquery-1.12.4.js'></script> -->

<link rel="stylesheet" href="/resources/css/bootstrap.min.css">
<link rel="stylesheet" href="/resources/css/font-awesome.css">
<link rel="stylesheet" href="/resources/css/font-awesome.min.css">
<link rel="stylesheet" href="/resources/css/style.css">

<script src="/resources/js/jquery-1.12.4.js"></script>
<script src="/resources/js/ui.js"></script>

<script>
	var flag = [];		// 폴더 클릭 여부 flag
	var lockList = [];
	var BtnFlag = [];	// 버튼 활성화 여부 flag
	var rootPath = ('${rootPath}').replace(/#/gi, '/');
	
	/* 하위 폴더 검색 function */
	function getChildrenNode(parent, data, path){
		loginCheck();
		
		// 폴더 클릭 시 해당 경로를 파일 경로에 입력
		$('#filePath').val(path.replace(/#/gi, '/').replace(rootPath, ''));
	
		// 클릭한 폴더의 element
		var thisElement = data;
		
		// 아코디언 이벤트
 		if(flag[thisElement] == 1) {
 			folderCss(thisElement, 'close');
			$('#' + thisElement).next().slideUp();
			flag[thisElement] = -1;
			return;
		} else if(flag[thisElement] == -1) {
 			folderCss(thisElement, 'open');
			$('#' + thisElement).next().slideDown();
			flag[thisElement] = 1;
			return;
		}
		
		// 폴더 클릭 시 flag를 1로 지정
		flag[thisElement] = 1;
		
		// form 데이터 설정
		$('#parent').val(data);	
		$('#path').val(path);
		var formData = $('#frm').serialize();

		$.ajax({
			type : 'POST',
			url: '/subDirList',
			data: formData,
			success: function(result){
				var initNode = $("[id^='" + thisElement + "']");
				var noneElement = $(result).find('#noneResult');
				
 				if(initNode.length > 1) {
					$($("[id^='" + thisElement + "']")[1]).empty();
				}
				$('#' + thisElement).after(result);

				// 클릭한 폴더 열림 CSS 적용
				folderCss(thisElement, 'open');
				// 더 이상 하위 폴더가 존재하지 않을 경우, 아이콘 CSS 제거
				$(result).each(function() {
					if($(this).attr('id') == 'noneResult') {
						$($('#' + thisElement).children()[0]).removeClass('fa-minus-square-o');
						return;
					}
				});
				
				// 아코디언 이벤트
				$('#' + thisElement).next().slideDown('fast', function(){
					$($('#' + thisElement).next()).css('display', '');
				});
			},
			error:function(request, status, error){
				console.log('code: ' + request.status + '\n');
			}
		}); 
	}

	/* 더블 클릭 시, 해당 폴더의 파일 목록 function */
	function getFileList(selectedNodeId, path) {
		loginCheck();

		// rootPath를 제거한 path를 파일경로에 지정
		setSearchPath(path);
		
		// form 데이터 설정
		$('#path').val(path);
		var formData = $('#frm').serialize();
		
		$.ajax({
			type : 'POST',
			url : '/fileList',
			data : formData,
			success: function(result){
				$('#deployListTable').empty();
				$('#deployListTable').append(result);
			}
		});
	}
	
	/* 파일 조회 function */
	function searchFile() {
		loginCheck();
		
		// 파일경로와 파일검색에 입력된 경로와 파일명을 form data로 입력
		var filePath = $('#filePath').val();
		var fileName = $('#fileName').val();
		var workspaceNm = $('#workspaceNm').val();

		$('#frmPath').val(filePath);
		$('#frmName').val(fileName);
		$('#frmworkspaceNm').val(workspaceNm);
		$('#workspaceNm2').val(workspaceNm);
		
		var formData = $('#searchFrm').serialize();
		
		// loading spinner 추가
		$('#deployListBody').empty();
		$('#deployListBody').append('<div class="loader"></div>');
		$('#DeployList').addClass('disabled');
		
		$.ajax({
			type : 'POST',
			url : '/searchFile',
			data : formData,
			success: function(result){
				$('.loader').remove();

				$('#deployListTable').empty();
				$('#deployListTable').append(result);
				$('#DeployList').removeClass('disabled');
			}
		});
	}

	/* 엔터키 입력 function */
	function enterKeyPress(){
		if (window.event.keyCode == 13) {
			return 1;
		} else {
			return 0;
		}
	}
	
	/* login 여부 체크 function
	 * 1: login 
	 * -1: login fail
	 * 0: default
	 */
	function loginCheck() {
		if(BtnFlag[login] != '1') {
			return;
		}
	}
	/* fileName 확장자 변경 function */
	function fillFileName(fileName){
		//fileName = fileName.replace('.java','.class');
		$("#fileName").val(fileName);
	}
	/* filePath 형식 function */
	function setSearchPath(filePath) {
		$('#filePath').val(filePath.replace(/#/gi, '/').replace(rootPath, ''));
	}
	/* login function */
	function login(){

		if(loginValidCheck()){
			// CSS
			$('#lginId').addClass('disabled');
			$('#lginId').attr('disabled', 'disabled');
			$('#lginPw').addClass('disabled');
			$('#lginPw').attr('disabled', 'disabled');
			
			$('#loginMsg').empty();
			$('#loginMsg').append('<span>로그인 중입니다.</span>');

			var formData = $('#loginFrm').serialize();

			//loginCheck();

			$.ajax({
				type : 'POST',
				url : '/loginCheck',
				data : {"lginId" : $('#lginId').val(), "lginPw" : $('#lginPw').val() },
				dataType: "json",
				success: function(result){
					// login 실패
					if(result.loginResult == 'fail'){
						BtnFlag['login'] = '-1';

						// CSS
						$('#lginId').attr('disabled', false);
						$('#lginPw').attr('disabled', false);
						$('#lginId').removeClass('disabled');
						$('#lginPw').removeClass('disabled');
						
						$('#loginMsg').text("로그인 정보가 맞지 않습니다.");
						$('#loginDiv').html();
						$("#lginPw").val('');
						$("#lginPw").focus();
						return;
						
					// 로그인 성공
					} else {
						BtnFlag['login'] = '1';

						// CSS
						$("#searchBtn").attr('disabled',false);
						$('#searchBtn').removeClass('btn-readonly');

						$('#filePath').attr('disabled', false);
						$('#fileName').attr('disabled', false);
						$('#filePath').removeClass('disabled');
						$('#fileName').removeClass('disabled');

						$('#folderList').attr('disabled', false);
						$('#folderList').removeClass('disabled');

						// login 문구
						$("#loginDiv").html('');
						var html = "";
						$('#usrNm').text(result.loginResult);
						html += '<div class="div3">';
						html += '<input type="hidden" id="user_id" name="user_id" value="'+result.user_id+'"/>';
						html += '<i class="fa fa-user-circle plus-icon"></i><span class="fnt-wgt-b">';
						html += result.loginResult;
						html += '</span>님 환영합니다.</div><hr id="hrLine" class="trans--grow">';
						$("#loginDiv").append($(html));

						// line 생성
						setTimeout(function(){
						    $('.trans--grow').addClass('grow');
						    getWorkList();
						}, 275);
					}
				},
				error:function(request,status,error){
			        alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			    }
			});
		}
	}
	
	/* 소스 수정정보 function */
	function getWorkList(){
		// loading 메세지
		var infoMsg = "<tr><td style='color:red;width:  1201px;text-align:  center;height:  150px;vertical-align:  middle;'>소스 수정 정보를 불러오는 중입니다.....</td></tr>";

		$('#workListBody').empty();
		$('#workListBody').append(infoMsg);
		
		$.ajax({
			type : 'POST',
			url : '/getWorkList',
			success: function(result){
				var fileElement = $(result).find('#result');
				
				$('#workListBody').empty();
				$('#workListBody').append(fileElement);
			},
			error:function(request,status,error){
		        alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		    }
		});
	}
	
	/* 로그인 validation function */
	function loginValidCheck(){
		var id = $("#lginId").val();
		var pw = $("#lginPw").val();
		
		if(id == ''){
			$('#loginMsg').text("아이디를 입력해주세요.");
			$("#lginId").focus();
			return false;
		}
		if(pw == ''){
			$('#loginMsg').text("비밀번호를 입력해주세요.");
			$("#lginPw").focus();
			return false;
		}
		return true;
	}
	
	/* Deploy -> Lock 파일 이동 function */
	function moveLock(){
		loginCheck();
		
		var lockList = $('#lockListBody').find('td>a');
		var checkedList = $("input[name=dplChk]:checked");
		
		// 재시도 버튼 hide
		$(".after-lock-btn").hide();
		$(".before-lock-btn").show();
		
		checkedList.each(function(){
			// 라인의 css 변경
			var tr_obj = $(this).parent().parent();
			var filePath = tr_obj.children()[2];
			$(filePath).attr('class', 'tb-w45');
			$(filePath).parent().append('<td class="tb-w20 lock-padding"></td>');
			
			// input name=lockChk로 변경
			$(this).attr('name','lockChk');
			
			//기존 lockList가 존재 할 때
			if(lockList.length != 0) {
				for(var i = 0; i < lockList.length ; i++) {
					var lockAfter = $(lockList[i]).text();
					var lockBefore = trimBlank($($(this).parent().next()).text());
					var lockBefore_obj = $(lockList[i]).parent().parent();
					
					//옮기려는 deployList에 있는 파일이 현재 lockList에 존재할 때
					if(lockAfter == lockBefore) {
						lockBefore_obj.remove();
					}
				}
			}
			tr_obj.appendTo('.lockTable_body');
		});
		
		if($('#DeployList').find('input').length == 0) {
			$('#dpl_checkAll').attr('checked', false);
		}
	}

	/* Lock -> Deploy 파일 이동 function */
	function moveDeploy(){
		loginCheck();
		
		var deployList = $('#deployListBody').find('td>a');
		var lockList = $('#lockListBody').find('td>a');
		var searchfilePath = $('#filePath').val();
		var checkedList = $("input[name=lockChk]:checked");
						
		checkedList.each(function(){
			var lockItem = $(this).parent().next();
			var lockItemData = trimBlank($(lockItem).text());
			var lockItemPath = $(lockItem.next()).text().replace(lockItemData, "");
			var lockItemPath2 = lockItemPath.substring(0, lockItemPath.length-1);

			// 라인의 css 변경
			var tr_obj = $(this).parent().parent();
			var filePath = tr_obj.children()[2];
 			$(filePath).attr('class', 'tb-w65');
			$($(filePath).parent().children()[3]).remove();

			// input name=dplChk로 변경
			$(this).attr('name','dplChk');
			for(var i = 0; i < deployList.length ; i++) {
				var deployAfter = $(deployList[i]).text();
				var deployBefore = $($(this).parent().next()).text().replace(/(^\s*)|(\s*$)/g, "");
				var deployBefore_obj = $(deployList[i]).parent().parent();
					
				//옮기려는 lockList에 있는 파일이 현재 deployList에 존재할 때
				if(deployAfter == deployBefore) {
					deployBefore_obj.remove();
				}
			}
			tr_obj.appendTo('#deployListBody');
		});
		
		if($('#lockListBody').find('input').length == 0) {
			$('#lock_checkAll').attr('checked', false);
		}
	}
	
	/* file Lock function */
	function setLock() {
		loginCheck();
		
		//BtnFlag['lock'] = '1';
		
		var lockItems = $('#LockList').find('input[name=lockChk]:checked');
		var lockFailItems = $('#LockList').find('input[name=lockFailChk]:checked');
		var workspaceNm = $('#workspaceNm2').val();
		
		if(workspaceNm.length < 5){
			alert("작업공간을 입력해주세요.");
			return;
		}
		var path = new Array();

		// lock 시도하는 파일들의 path 저장		
		for(var i = 0; i < lockItems.length; i++) {
			path.push($($(lockItems[i]).parent().next().next()).text());
		}
		// lock 실패한 파일들의 path 저장
		for(var i = 0; i < lockFailItems.length; i++) {
			path.push($($(lockFailItems[i]).parent().next().next()).text());
		}
		
		var lockOkItems = $('#LockList').find('input[name=lockOkChk]');
		var okPath = new Array();
		
		// lock 성공한 파일들의 path 저장
		for(var i = 0; i < lockOkItems.length; i++) {
			okPath.push($($(lockOkItems[i]).parent().next().next()).text());
		}
		
		$('#workspaceNmStr').val(workspaceNm);
		
		$('#lockListStr').val(path);
		$('#lockOkListStr').val(okPath);
		$('#lockUserId').val($('#user_id').val());
		var formData = $('#lockFrm').serialize();
		
		$('#LockList').append('<div class="loader lock-loader"></div>');
		$('#LockList').addClass('disabled');
		
		$.ajax({
			type : 'POST',
			//url: '/lockFile',
			url: '/deliverFile',
			data: formData,
			dataType: "json",
			success: function(result){
				// Button 이벤트
				$.each(result, function(key, value) {
					
					if(key=='error' && value=='error'){
						alert("로그인 정보가 유효하지 않습니다.\n다시 접속해주세요.");
						return;
					}
					
					if(key=='wrongPath' && value=='WX'){
						alert("딜리버할 파일이 지정되지 않았습니다.! \n관리자에게 문의해주세요.");
						return;
					}
					
					if(key=='createChangset' && value=='CX'){
						alert("체인지셋 생성 실패! \n관리자에게 문의해주세요.");
						return;
					}
					
					if(key=='moveChangset' && value=='MX'){
						alert("체인지셋 이동 실패! \n관리자에게 문의해주세요.");
						return;
					}
					
					if(key=='NotFoundMX'){
						alert("체인지셋 이동 실패! \n체크인리스트에 포함되지 않는 소스 포함 되어 있음.");
						
						var url= "";    //팝업창에 출력될 페이지 URL
						var winWidth = 900;
					    var winHeight = 600;
					    var popupOption= "width="+winWidth+", height="+winHeight;    //팝업창 옵션(optoin)
					    var myWindow = window.open(url,"TestName",popupOption);
					    myWindow.document.write("<h1>하기 소스리스트를 Deliver 파일선택 리스트에서 제외해주세요.</h1><br/>"+value+"");
						
						return;
					}				
					
					
					if(key=='deliver' && value=='DX'){
						alert("딜리버 실패! \n관리자에게 문의해주세요.");
						return;
					}
					
					$("input[name=lockChk]:checked").each(function(){
						var tr1_obj = $(this).parent();
						var lockItem = tr1_obj.next();
						var lockItemData = trimBlank($(lockItem).text());
						
						if(lockItemData == key) {
							$(this).css('display', 'none');
							$(tr1_obj).find('span').remove();
							
							// lock 성공 시
							if(value == 'O') {
								$(tr1_obj).append('<i class="fa fa-circle-o"></i>');
								$(this).prop('checked',false);
								$(this).attr('name','lockOkChk');
							// lock 실패 시
							} else if(value == 'X'){
								$(tr1_obj).find('i').remove();
								$(tr1_obj.parent().children()[3]).text('다른 사용자가 이미 잠궜습니다.');
								$(tr1_obj).append('<i class="fa fa-times unLocked"></i>');
								$(this).attr('name','lockFailChk');
							}else {
								
							}
						}
					});
				});
				lockUnselectedItem();

				$('.loader').remove();
				$('#LockList').removeClass('disabled');
			},
			error:function(request, status, error){
				console.log('code: ' + request.status + '\n');
			}
		});
	}
	
	/* 빈칸 제거 data */
	function trimBlank(data) {
		return data.replace(/(^\s*)|(\s*$)/g, "");
	}

	/* lock 버튼 function */
	function lockUnselectedItem() {
		var unCheckdInputLength = $('#LockList').find('input:checkbox:not(:checked)').length;
		var lockedInput = $($('#LockList').find('i'));
		var lockedInputLength = lockedInput.length;
		var failedLockCount = 0;
		
		lockedInput.each(function() {
			if($(this).attr('class') == 'fa fa-times unLocked') {
				failedLockCount ++;
			}
		});

		// 실패한 Lock 파일이 없을 때
		if(Number(unCheckdInputLength-lockedInputLength) > 0 && failedLockCount == 0) {
			$(".after-lock-btn").hide();
			$(".before-lock-btn").show();
		// 실패한 Lock 파일이 있을 때
		} else if(failedLockCount > 0) {
			$(".after-lock-btn").show();
			$(".before-lock-btn").hide();
		}
	}
	
	$(document).ready(function() {
		var login = '<%=login%>';

		// BtnFlag['login'] = '1';
		
		if(login == 'fail'){
			disabledCss();
			readonlyCss();
 			 			
 			$("button").attr('disabled','disabled');
			$('button').addClass('btn-readonly');
			
			$("button[name=loginBtn]").attr('disabled',false);
			$('button[name=loginBtn]').removeClass('btn-readonly');

 			$('#filePath').attr('disabled', 'disabled');
			$('#fileName').attr('disabled', 'disabled');
			
			$('#filePath').addClass('disabled');
			$('#fileName').addClass('disabled');
			
			$('#folderList').addClass('disabled');
		} else {
			$('#div3').trigger(hrLineCss());
			BtnFlag['login'] = '1';
		}
	});
</script>
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
		<c:if test="${login=='success' }">
			<div class="div3">
				<i class="fa fa-user-circle plus-icon" aria-hidden="true"></i>
				<input type="hidden" id="user_id" name="user_id" value="${user_id }"/>
				<span id="usrNm" class="fnt-wgt-b">${user_nm }</span>님 환영합니다.
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
			<span class="add-on">작업공간</span><input type="text" id="workspaceNm" class="width-400 vrtcl-mid"/>
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
			<td rowspan="2" class="tb-w30 height-100">
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
				<div id="deployTd" class="div-ml"><span> >> Deploy조회</span>
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
									<!-- <td class="tb-w15 td-ta-center">
									</td> -->
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div class="div-hb"></div>
				<div class="div-ta-right">
					<button type="button" id="moveLckBtn" class="btn1" onclick="moveLock(); checkInputBox();" disabled="disabled">추가</button>
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
					<input type="hidden" id="lockOkListStr" name="lockOkListStr"/>
					<input type="hidden" id="lockUserId" name="lockUserId"/>
					<input type="hidden" id="workspaceNmStr" name="workspaceNmStr"/>
				</form>
					
				<div class="div-hb"></div>
				<div class="div-ta-right before-lock-btn">
				<span class="add-on">작업공간</span><input type="text" id="workspaceNm2" class="width-400 vrtcl-mid"/>
					<button id="lockBtn" class="btn1" onClick="setLock();" disabled="disabled">Deliver</button>
					<button type="button" id="moveDepBtn" class="btn1" onclick="moveDeploy(); checkInputBox();" disabled="disabled">선택취소</button>
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