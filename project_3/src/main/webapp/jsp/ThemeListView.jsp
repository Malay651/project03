<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.project_3.dto.ThemeDTO"%>
<%@page import="in.co.rays.project_3.controller.ThemeListCtl"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>
<head>
<title>Theme List</title>

<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/CheckBox11.js"></script>

<style>
.hm {
	background-image: url('<%=ORSView.APP_CONTEXT%>/img/wallp.jpg');
	background-size: cover;
	padding-top: 85px;
}

.text {
	text-align: center;
}
</style>

</head>

<%@include file="Header.jsp"%>

<body class="hm">

<form action="<%=ORSView.THEME_LIST_CTL%>" method="post">

<jsp:useBean id="dto" class="in.co.rays.project_3.dto.ThemeDTO" scope="request"></jsp:useBean>

<%
int pageNo = ServletUtility.getPageNo(request);
int pageSize = ServletUtility.getPageSize(request);
int index = ((pageNo - 1) * pageSize) + 1;

int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

List list = ServletUtility.getList(request);
Iterator<ThemeDTO> it = list.iterator();
%>

<center>
	<h1 style="color:white;">Theme List</h1>
</center>

<!-- Messages -->
<center>
<font color="green"><%=ServletUtility.getSuccessMessage(request)%></font>
<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
</center>

<!-- Search Fields -->
<div class="row" style="margin:20px;">
	<div class="col-sm-3"></div>

	<div class="col-sm-2">
		<input type="text" name="themeName" placeholder="Theme Name"
			class="form-control"
			value="<%=ServletUtility.getParameter("themeName", request)%>">
	</div>

	<div class="col-sm-2">
		<input type="text" name="themeCode" placeholder="Theme Code"
			class="form-control"
			value="<%=ServletUtility.getParameter("themeCode", request)%>">
	</div>

	<div class="col-sm-3">
		<input type="submit" name="operation" class="btn btn-primary"
			value="<%=ThemeListCtl.OP_SEARCH%>">
		<input type="submit" name="operation" class="btn btn-dark"
			value="<%=ThemeListCtl.OP_RESET%>">
	</div>
</div>

<!-- Table -->
<div class="table-responsive">
<table class="table table-bordered table-dark table-hover">

<tr style="background-color:#8C8C8C;">
	<th><input type="checkbox" id="select_all"> Select All</th>
	<th>S.NO</th>
	<th>Theme Code</th>
	<th>Theme Name</th>
	<th>Color</th>
	<th>Status</th>
	<th>Edit</th>
</tr>

<%
while (it.hasNext()) {
	dto = it.next();
%>

<tr>
	<td align="center">
		<input type="checkbox" class="checkbox" name="ids" value="<%=dto.getId()%>">
	</td>

	<td class="text"><%=index++%></td>
	<td class="text"><%=dto.getThemeCode()%></td>
	<td class="text"><%=dto.getThemeName()%></td>
	<td class="text"><%=dto.getColor()%></td>
	<td class="text"><%=dto.getStatus()%></td>

	<td class="text">
		<a href="ThemeCtl?id=<%=dto.getId()%>">Edit</a>
	</td>
</tr>

<%
}
%>

</table>
</div>

<!-- Buttons -->
<table width="100%">
<tr>

<td>
<input type="submit" name="operation" class="btn btn-warning"
	value="<%=ThemeListCtl.OP_PREVIOUS%>"
	<%=pageNo > 1 ? "" : "disabled"%>>
</td>

<td>
<input type="submit" name="operation" class="btn btn-primary"
	value="<%=ThemeListCtl.OP_NEW%>">
</td>

<td>
<input type="submit" name="operation" class="btn btn-danger"
	value="<%=ThemeListCtl.OP_DELETE%>">
</td>

<td align="right">
<input type="submit" name="operation" class="btn btn-warning"
	value="<%=ThemeListCtl.OP_NEXT%>"
	<%=(nextPageSize != 0) ? "" : "disabled"%>>
</td>

</tr>
</table>

<input type="hidden" name="pageNo" value="<%=pageNo%>">
<input type="hidden" name="pageSize" value="<%=pageSize%>">

</form>

</body>

<%@include file="FooterView.jsp"%>
</html>