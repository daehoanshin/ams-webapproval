<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>


<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/themes/default/style.min.css" />

<script src="https://code.jquery.com/jquery-2.2.1.js"></script>
<script src="https://code.jquery.com/jquery-2.2.1.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/jstree.min.js"></script>
<script type="text/javascript">
$(function () {
	
    // 6 create an instance when the DOM is ready
    $('#jstree').jstree();
    // 7 bind to events triggered on the tree
    $('#jstree').on("changed.jstree", function (e, data) {
      console.log(data.selected);
      var obj = $("#"+data.selected);
      var path = obj.find("input[name=filePath]").val();
      
      $("#nowPath").val(path);
	
      $.ajax({
    	  url : "/subFileList",
    	  data : {"filePath" : path},
    	  type : "post",
    	  dataType : "html",
    	  success : function(data){
    		  $("#fileList").html('');
    		  $("#fileList").append(data);
    	  }
      });
      
    });
    
    
  });

function searchFile(){
	var path = $("#nowPath").val();
	var name = $("#fileName").val();
	if(name == ''){
		alert("파일이름을 입력하세요.");
		$("#fileName").focus();
		return;
	}
	if(name.indexOf(".") == -1){
		alert("확장자를 입력해주세요");
		$("#fileName").focus();
		return;
	}
	
	$.ajax({
		url : "/subFileList",
		data : {"filePath" : path, "fileName":name},
		type : "post",
		dataType : "html",
		success : function(data){
			$("#fileList").html('');
  		  	$("#fileList").append(data);
		}
	});
}
 </script>
 
 
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>
<div>
파일경로 <input type="text" id="nowPath" name="nowPath" style="width: 300px;" value="${rootUrl }"/>
</div>
<div>
파일검색 <input type="text" id="fileName" name="fileName" style="width: 300px;"/> <button type="button" onclick="searchFile();">조회</button>
</div>
<div id="jstree">
    <!-- in this example the tree is populated from inline HTML -->
    <ul id="addNodeUl">
    	<c:forEach items="${directory }" var="map" varStatus="status">
  			<c:if test="${!fn:contains(map['dpth'], '+1') and status.index!=0}">
  				</li>
  			</c:if>
    		<c:if test="${fn:contains(map['dpth'], '+1') }">
    			<ul>
    		</c:if>
    		
    		<li><input type="hidden" id="filePath" name="filePath" value="${map['path']}"/>${map['name']}
    		
    		<c:if test="${fn:contains(map['dpth'], '-1') }">
    			</li></ul>
    		</c:if>
    	</c:forEach>
    </li></ul>
  </div>

  <hr>
  <div id="fileList">
  <c:forEach items="${file }" var="obj" varStatus="status">
  ${status.index+1 }. 
  <c:forEach items="${obj }" var="map">
  	<c:if test="${map.key=='path' }">
  		path = ${map.value },
  	</c:if>
  	<c:if test="${map.key=='name' }">
  		name = ${map.value }<br/>
  	</c:if>
  </c:forEach>
  </c:forEach>
  </div>
</body>
</html>
