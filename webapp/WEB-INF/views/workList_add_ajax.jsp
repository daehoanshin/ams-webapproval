<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
</head>
<body>
<table class="table2">
			<tbody class="tbody-dw" id="result">
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
			<c:if test="${fn:length(workList) <1}">
				<tr>
					<td style="width:  1201px;text-align:  center;height:  150px;vertical-align:  middle;">소스 수정 정보가 없습니다.
				</tr>
			</c:if>
			</tbody>
		</table> 
</body>
</html>
