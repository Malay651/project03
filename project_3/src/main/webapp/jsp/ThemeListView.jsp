<%@page import="in.co.rays.project_3.dto.ThemeDTO"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.project_3.controller.ThemeListCtl"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Theme List</title>

<style>
body {
	background-image: url('<%=ORSView.APP_CONTEXT%>/img/aq.jpg');
	background-size: cover;
	padding-top: 80px;
}
</style>
</head>

<body>

<%@include file="Header.jsp"%>

<form action="<%=ORSView.THEME_LIST_CTL%>" method="post">

	<jsp:useBean id="dto" class="in.co.rays.project_3.dto.ThemeDTO" scope="request"/>

	<%
		int pageNo = ServletUtility.getPageNo(request);
		int pageSize = ServletUtility.getPageSize(request);
		int index = ((pageNo - 1) * pageSize) + 1;

		List list = ServletUtility.getList(request);
		Iterator<ThemeDTO> it = list.iterator();
	%>

	<center>
		<h2>Theme List</h2>
	</center>

	<table class="table table-bordered table-dark">
		<tr>
			<th>Select</th>
			<th>S.No</th>
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
			<td><input type="checkbox" name="ids" value="<%=dto.getId()%>"></td>
			<td><%=index++%></td>
			<td><%=dto.getThemeCode()%></td>
			<td><%=dto.getThemeName()%></td>
			<td><%=dto.getColor()%></td>
			<td><%=dto.getStatus()%></td>
			<td><a href="ThemeCtl?id=<%=dto.getId()%>">Edit</a></td>
		</tr>

		<%
			}
		%>

	</table>

	<!-- Buttons -->
	<input type="submit" name="operation" value="<%=ThemeListCtl.OP_PREVIOUS%>">
	<input type="submit" name="operation" value="<%=ThemeListCtl.OP_NEW%>">
	<input type="submit" name="operation" value="<%=ThemeListCtl.OP_DELETE%>">
	<input type="submit" name="operation" value="<%=ThemeListCtl.OP_NEXT%>">

</form>

<%@include file="FooterView.jsp"%>

</body>
</html>