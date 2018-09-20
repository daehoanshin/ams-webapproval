<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
 
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
  
</html>
