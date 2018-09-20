<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%@ page session="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>

<body>
<c:if test="${fn:length(subDirList) != 0}">
	<div id='${parent}_${depth}' class="ie-margin" style="margin-left:5%; display:none">
	    <ul id='addNodeUl'>
	    <c:forEach items="${subDirList}" var="dirListItems" varStatus="status">
	    	<li id="${parent}_${depth}${status.index}" class="fileName"
	    		oncontextmenu="return false"  ondragstart="return false"  onselectstart="return false"
	    		onClick="lineColorCss('${parent}_${depth}${status.index}'); setSearchPath('${subDirList[status.index].path}')"
	    		ondblClick="getFileList('${depth}${status.index}', '${subDirList[status.index].path}');">
	    			<i class="plus-icon fa fa-plus-square-o" 
	    				onClick="getChildrenNode('${parent}', '${parent}_${depth}${status.index}', '${subDirList[status.index].path}');"></i>
	    			<i class="fa fa-folder-o fileName"
	    				ondblClick="getFileList('i_${depth}${status.index}','${dirList[status.index].path}');"></i>
	    			<c:choose>
	      				<c:when test="${subDirList[status.index].changeset == 'exist'}">
		            		<span style="background-color: #d5f4e6;">${subDirList[status.index].data}</span><span style="background-color: #fefbd8;">  ${subDirList[status.index].changeset}</span>
		        		</c:when>
			        	<c:otherwise>
			        		<span>${subDirList[status.index].data}</span>  		
			        	</c:otherwise>
			    	</c:choose>	
	    		</li>
		</c:forEach>
	    </ul>
	</div>
</c:if>

<c:if test="${fn:length(subDirList) == 0}">
	<div id="noneResult" style="display:none">
	</div>
</c:if>

</body>
</html>
