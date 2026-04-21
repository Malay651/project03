<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.project_3.dto.UsageDTO"%>
<%@page import="in.co.rays.project_3.controller.UsageListCtl"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Usage List</title>

<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/CheckBox11.js"></script>

<style>
.hm {
	background-image: url('<%=ORSView.APP_CONTEXT%>/img/12912.jpg');
	background-repeat: no-repeat;
	background-attachment: fixed;
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

<form action="<%=ORSView.USAGE_LIST_CTL%>" method="post">

<jsp:useBean id="dto" class="in.co.rays.project_3.dto.UsageDTO" scope="request" />

<%
	int pageNo = ServletUtility.getPageNo(request);
	int pageSize = ServletUtility.getPageSize(request);
	int index = ((pageNo - 1) * pageSize) + 1;

	int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

	List list = ServletUtility.getList(request);
	Iterator<UsageDTO> it = list.iterator();
%>

<% if (list.size() != 0) { %>

<center>
	<h1><u>Usage List</u></h1>
</center>

<!-- Search Panel -->
<div class="row">

	<div class="col-sm-3"></div>

	<div class="col-sm-2">
		<input type="text" name="usageCode" placeholder="Usage Code"
			class="form-control"
			value="<%=ServletUtility.getParameter("usageCode", request)%>">
	</div>

	<div class="col-sm-2">
		<input type="text" name="usageName" placeholder="Usage Name"
			class="form-control"
			value="<%=ServletUtility.getParameter("usageName", request)%>">
	</div>

	<div class="col-sm-3">
		<input type="submit" class="btn btn-primary"
			name="operation" value="<%=UsageListCtl.OP_SEARCH%>">
			
		<input type="submit" class="btn btn-dark"
			name="operation" value="<%=UsageListCtl.OP_RESET%>">
	</div>

</div>

<br>

<!-- Table -->
<table class="table table-bordered table-dark">

	<tr>
		<th><input type="checkbox" id="select_all"> Select All</th>
		<th>S.No</th>
		<th>Usage Code</th>
		<th>Usage Name</th>
		<th>Usage Type</th>
		<th>Status</th>
		<th>Edit</th>
	</tr>

	<%
		while (it.hasNext()) {
			dto = it.next();
	%>

	<tr>
		<td align="center">
			<input type="checkbox" name="ids" value="<%=dto.getId()%>">
		</td>

		<td><%=index++%></td>
		<td><%=dto.getUsageCode()%></td>
		<td><%=dto.getUsageName()%></td>
		<td><%=dto.getUsageType()%></td>
		<td><%=dto.getStatus()%></td>

		<td>
			<a href="UsageCtl?id=<%=dto.getId()%>">Edit</a>
		</td>
	</tr>

	<% } %>

</table>

<!-- Buttons -->
<table width="100%">
<tr>

<td>
	<input type="submit" name="operation"
		value="<%=UsageListCtl.OP_PREVIOUS%>"
		<%=pageNo > 1 ? "" : "disabled"%>>
</td>

<td>
	<input type="submit" name="operation"
		value="<%=UsageListCtl.OP_NEW%>">
</td>

<td>
	<input type="submit" name="operation"
		value="<%=UsageListCtl.OP_DELETE%>">
</td>

<td align="right">
	<input type="submit" name="operation"
		value="<%=UsageListCtl.OP_NEXT%>"
		<%=(nextPageSize != 0) ? "" : "disabled"%>>
</td>

</tr>
</table>

<% } else { %>

<center>
	<h2>No Record Found</h2>
</center>

<input type="submit" name="operation"
	value="<%=UsageListCtl.OP_BACK%>">

<% } %>

<input type="hidden" name="pageNo" value="<%=pageNo%>">
<input type="hidden" name="pageSize" value="<%=pageSize%>">

</form>

</body>

<%@include file="FooterView.jsp"%>

</html>