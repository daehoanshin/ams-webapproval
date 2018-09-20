<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
</head>
<body>
<c:if test="${fn:length(fileList) > 0}">
<table class="table1">
	<tbody id="deployListBody"  class="thead-dwh tbody-h100">
			<c:forEach items="${fileList}" var="dirListItems" varStatus="status">
				<tr class="searchLine">
					<c:choose>
	      				<c:when test="${fileList[status.index].changeset == 'exist'}">
	      					<td class="tb-w100 td-ta-center"><input type="checkbox" name="dplChk"></td>
	      					<td style="background-color: #d5f4e6;" class="tb-w25">
								<a style="cursor: pointer;" onclick="fillFileName('${fileList[status.index].data}');">${fileList[status.index].data}</a>
							</td>
		            		<td id="dp_${status.index}" style="background-color: #d5f4e6;" class="tb-w65">
								<script>
									var path = '${fileList[status.index].path}';
									var replacedPath = path.replace(/#/gi, '/');
									$('#dp_${status.index}').html(replacedPath.replace(rootPath, ''));
								</script>
							</td>
		        		</c:when>
			        	<c:otherwise>
			        		<td class="tb-w100 td-ta-center"><input type="checkbox" name="dplChk" disabled="disabled"></td>
			        		<td class="tb-w25">
								<a style="cursor: pointer;" onclick="fillFileName('${fileList[status.index].data}');">${fileList[status.index].data}</a>
							</td>
			        		<td id="dp_${status.index}" class="tb-w65">
								<script>
									var path = '${fileList[status.index].path}';
									var replacedPath = path.replace(/#/gi, '/');
									$('#dp_${status.index}').html(replacedPath.replace(rootPath, ''));
								</script>
							</td>  		
			        	</c:otherwise>
			    	</c:choose>
					
				</tr>
			</c:forEach>
	</tbody>
</table>
</c:if>

<c:if test="${fn:length(fileList) == 0}">
	<div id="deployListBody" class="td-ta-center search-result">
		<i class="fa fa-search plus-icon search-result-color"></i>
		<span class="search-result-color">검색 결과가 없습니다.</span>
	</div>
</c:if>

</body>
</html>
